package com.elka.heofficeclub.service.repository

import com.elka.heofficeclub.other.Action
import com.elka.heofficeclub.other.ErrorApp
import com.elka.heofficeclub.other.Errors
import com.elka.heofficeclub.other.documents.Gift
import com.elka.heofficeclub.other.documents.TypeOfChangeWork
import com.elka.heofficeclub.other.documents.Vacation
import com.elka.heofficeclub.service.model.Division
import com.elka.heofficeclub.service.model.Employer
import com.elka.heofficeclub.service.model.OrganizationPosition
import com.elka.heofficeclub.service.model.documents.forms.*
import com.google.firebase.FirebaseNetworkException
import com.google.firebase.firestore.FieldValue
import kotlinx.coroutines.tasks.await
import java.util.*

object EmployeesRepository {
  suspend fun getCountOfEmployerWithPosition(
    organizationId: String, positionId: String, onSuccess: suspend (Int) -> Unit
  ): ErrorApp? = try {
    // TODO: WRITE CODE
    onSuccess(0)
    null
  } catch (_: FirebaseNetworkException) {
    Errors.network
  } catch (_: java.lang.Exception) {
    Errors.unknown
  }

  suspend fun createEmployer(employer: Employer, onSuccess: suspend (Employer) -> Unit): ErrorApp? =
    try {
      // create employer
      val id = "${employer.organizationId}_${employer.tableNumber}"
      FirebaseService.employeesCollection.document(id).set(employer).await()
      employer.id = id

      // add employer id to org node
      OrganizationRepository.addEmployer(employer.organizationId, employer.id)

      onSuccess(employer)
      null
    } catch (_: FirebaseNetworkException) {
      Errors.network
    } catch (_: java.lang.Exception) {
      Errors.unknown
    }

  suspend fun addT2(employerId: String, t2: T2) {
    // add doc id to employer docs
    addDoc(employerId, t2.id)

    // add doc to organization list
    OrganizationRepository.addDocId(t2.orgId, t2.id)

    // set doc id to employer T2
    FirebaseService.employeesCollection.document(employerId).update(FIELD_T2, t2.id).await()
  }

  private fun changeList(field: String, employerId: String, value: Any, action: Action) {
    val fv = when (action) {
      Action.REMOVE -> FieldValue.arrayRemove(value)
      Action.ADD -> FieldValue.arrayUnion(value)
      else -> return
    }

    FirebaseService.employeesCollection.document(employerId).update(field, fv)
  }

  suspend fun addT1(employerId: String, t1: T1) {
    // add doc to organization list
    OrganizationRepository.addDocId(t1.orgId, t1.id)

    // add doc id to employer docs
    addDoc(employerId, t1.id)

    // add employer id to division employers []
    DivisionsRepository.addEmployer(t1.division!!.id, employerId)

    // Change division, position id, and T1
    val ref = FirebaseService.employeesCollection.document(employerId)
    val position = t1.position!!
    val division = t1.division!!
    val premium = t1.premium

    ref.update(FIELD_T1, t1.id).await()
    setWork(employerId, position, division, premium, null)
  }

  suspend fun addT5(employerId: String, t5: T5) {
    // add doc to organization list
    OrganizationRepository.addDocId(t5.organization!!.id, t5.id)

    // add doc to employer`s docs list
    addDoc(employerId, t5.id)

    val employerId = t5.employer!!.id

    val position = t5.newPosition!!
    val division = t5.newDivision!!
    val startTime = t5.transferStart
    val endTime = t5.transferEnd

    val premium = t5.premium

    // update employer
    if (t5.typeOfChangeWork == TypeOfChangeWork.PERMANENT) {
      setWork(employerId, position, division, premium, t5.oldDivision!!)
    } else {
      setTempWork(employerId, position, division, startTime!!, endTime!!, premium)
    }
  }

  private suspend fun setTempWork(
    employerId: String,
    position: OrganizationPosition?,
    division: Division?,
    startTime: Date?,
    endTime: Date?,
    premium: Double
  ) {
    val ref = FirebaseService.employeesCollection.document(employerId)
    ref.update(FIELD_POSITION_TMP_ID, position?.id).await()
    ref.update(FIELD_DIVISION_TMP_ID, division?.id).await()
    ref.update(FIELD_START_WORK_TMP, startTime).await()
    ref.update(FIELD_END_WORK_TMP, endTime).await()
    ref.update(FIELD_PREMIUM_TMP, premium).await()
  }

  private suspend fun setWork(
    employerId: String,
    position: OrganizationPosition?,
    newDivision: Division?,
    premium: Double,
    oldDivision: Division?
  ) {
    val ref = FirebaseService.employeesCollection.document(employerId)
    ref.update(FIELD_POSITION_ID, position?.id).await()
    ref.update(FIELD_DIVISION_ID, newDivision?.id).await()
    ref.update(FIELD_PREMIUM, premium).await()
    ref.update(FIELD_POSITION_TMP_ID, "").await()
    ref.update(FIELD_DIVISION_TMP_ID, "").await()
    ref.update(FIELD_START_WORK_TMP, null).await()
    ref.update(FIELD_END_WORK_TMP, null).await()
    ref.update(FIELD_PREMIUM_TMP, 0.0).await()

    // delete employer id from oldDivision
    if (oldDivision != null) DivisionsRepository.removeEmployer(oldDivision.id, employerId)

    // add employer id to newDivision
    newDivision?.id?.let { DivisionsRepository.addEmployer(it, employerId) }
  }

  suspend fun addT6(employerId: String, t6: T6): List<Vacation> {
    // add doc to organization list
    OrganizationRepository.addDocId(t6.organization!!.id, t6.id)

    // add doc to employer`s docs list
    addDoc(employerId, t6.id)

    // add vacation (or -s) to employer`s t2
    val t2Id = t6.employer!!.T2

    val vacationA = Vacation(
      type = t6.vacationADescription,
      workStart = t6.startWork ?: Date(0),
      workEnd = t6.endWork ?: Date(0),
      vacationStart = t6.startVacationA ?: Date(0),
      vacationEnd = t6.endVacationA ?: Date(0)
    )

    val vacationB = Vacation(
      type = t6.vacationBDescription,
      workStart = t6.startWork ?: Date(0),
      workEnd = t6.endWork ?: Date(0),
      vacationStart = t6.startVacationB ?: Date(0),
      vacationEnd = t6.endVacationB ?: Date(0)
    )

    val vacations = mutableListOf<Vacation>()
    if (DocumentsRepository.addVacation(t2Id, vacationA) != null) vacations.add(vacationA)
    if (DocumentsRepository.addVacation(t2Id, vacationB) != null) vacations.add(vacationB)
    return vacations
  }

  suspend fun addT11(employerId: String, t11: T11): Gift {
    // add doc to organization list
    OrganizationRepository.addDocId(t11.organization!!.id, t11.id)

    // add doc to employer`s docs list
    addDoc(employerId, t11.id)

    // add gift to employer`s t2
    val t2Id = t11.employer!!.T2
    val gift = Gift(
      name = t11.description,
      docType = t11.giftType,
      docDate = t11.dataCreated,
      docSerialNumber = t11.number.toString()
    )

    DocumentsRepository.addGift(t2Id, gift)
    return gift
  }

  fun addDoc(employerId: String, docId: String) {
    changeList(FIELD_DOCS, employerId, docId, Action.ADD)
  }

  suspend fun loadEmployers(
    employeesIdx: List<String>, onSuccess: (List<Employer>) -> Unit
  ): ErrorApp? = try {
    val items = employeesIdx.mapNotNull { loadEmployer(it) }
    onSuccess(items)
    null
  } catch (_: FirebaseNetworkException) {
    Errors.network
  } catch (_: java.lang.Exception) {
    Errors.unknown
  }

  private suspend fun loadEmployer(id: String): Employer? {
    val doc = FirebaseService.employeesCollection.document(id).get().await()
    val employer = doc.toObject(Employer::class.java)

    if (employer != null) {
      employer.id = doc.id
      employer.T2Local = DocumentsRepository.loadT2(employer.T2)
      employer.T1Local = DocumentsRepository.loadT1(employer.T1)
      employer.T8Local = DocumentsRepository.loadT8(employer.T8)
      employer.divisionLocal =
        employer.divisionId?.let { DivisionsRepository.loadDivisionInfo(it) }
      employer.positionLocal = employer.positionId?.let {
        OrganizationPositionRepository.loadPosition(
          it
        )
      }

      if (employer.endWorkTmp != null && employer.endWorkTmp!!.time >= Calendar.getInstance().time.time) {
        val divisionId = employer.divisionTempId
        if (divisionId != "") {
          if (divisionId != null) employer.divisionTempLocal =
            DivisionsRepository.loadDivisionInfo(divisionId)
        }

        val positionId = employer.positionTempId
        if (positionId != "") {
          if (positionId != null) employer.positionTempLocal =
            OrganizationPositionRepository.loadPosition(positionId)
        }
      }
    }

    return employer
  }

  suspend fun dismissal(t8: T8) {

    val employerId = t8.employer.id
    val ref = FirebaseService.employeesCollection.document(employerId)

    // delete position and division on employer`s T2
    setWork(employerId, null, null, 0.0, null)
    setTempWork(employerId, null, null, null, null, 0.0)

    // set t8 to employer`s T2
    ref.update(FIELD_T8, t8.id).await()
  }

  const val FIELD_T8 = "t8"
  const val FIELD_T2 = "t2"
  const val FIELD_T1 = "t1"
  const val FIELD_DOCS = "docs"
  const val FIELD_POSITION_ID = "positionId"
  const val FIELD_DIVISION_ID = "divisionId"

  const val FIELD_PREMIUM = "premium"
  const val FIELD_PREMIUM_TMP = "tempPremium"

  const val FIELD_POSITION_TMP_ID = "positionTempId"
  const val FIELD_DIVISION_TMP_ID = "divisionTempId"
  const val FIELD_START_WORK_TMP = "startWorkTmp"
  const val FIELD_END_WORK_TMP = "endWorkTmp"
}