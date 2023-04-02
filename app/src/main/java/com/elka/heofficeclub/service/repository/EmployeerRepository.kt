package com.elka.heofficeclub.service.repository

import com.elka.heofficeclub.other.Action
import com.elka.heofficeclub.other.ErrorApp
import com.elka.heofficeclub.other.Errors
import com.elka.heofficeclub.other.documents.Vacation
import com.elka.heofficeclub.service.model.Employer
import com.elka.heofficeclub.service.model.documents.forms.T1
import com.elka.heofficeclub.service.model.documents.forms.T2
import com.elka.heofficeclub.service.model.documents.forms.T6
import com.google.firebase.FirebaseNetworkException
import com.google.firebase.firestore.FieldValue
import kotlinx.coroutines.tasks.await
import java.util.Date

object EmployeesRepository {
  suspend fun getCountOfEmployerWithPosition(
    organizationId: String,
    positionId: String,
    onSuccess: suspend (Int) -> Unit
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
    ref.update(FIELD_POSITION_ID, t1.position!!.id).await()
    ref.update(FIELD_DIVISION_ID, t1.division!!.id).await()
    ref.update(FIELD_T1, t1.id).await()
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

  private fun addDoc(employerId: String, docId: String) {
    changeList(FIELD_DOCS, employerId, docId, Action.ADD)
  }

  suspend fun loadEmployers(
    employeesIdx: List<String>,
    onSuccess: (List<Employer>) -> Unit
  ): ErrorApp? =
    try {
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
      employer.divisionLocal = DivisionsRepository.loadDivisionInfo(employer.divisionId)
      employer.positionLocal = OrganizationPositionRepository.loadPosition(employer.positionId)
    }

    return employer
  }

  const val FIELD_T2 = "t2"
  const val FIELD_T1 = "t1"
  const val FIELD_DOCS = "docs"
  const val FIELD_POSITION_ID = "positionId"
  const val FIELD_DIVISION_ID = "divisionId"
}