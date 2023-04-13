package com.elka.heofficeclub.service.repository

import android.net.Uri
import com.elka.heofficeclub.other.*
import com.elka.heofficeclub.other.documents.Gift
import com.elka.heofficeclub.other.documents.Vacation
import com.elka.heofficeclub.other.documents.WorkExperience
import com.elka.heofficeclub.service.model.Division
import com.elka.heofficeclub.service.model.OrganizationPosition
import com.elka.heofficeclub.service.model.documents.forms.*
import com.google.firebase.FirebaseNetworkException
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FieldValue
import kotlinx.coroutines.tasks.await
import java.util.*

object DocumentsRepository {
  suspend fun setT1(t1: T1, onSuccess: suspend () -> Unit): ErrorApp? = try {
    val doc = FirebaseService.docsCollection.add(t1).await()
    t1.id = doc.id

    val employerId = "${t1.orgId}_${t1.employerTableNumber}"
    EmployeesRepository.addT1(employerId, t1)

    onSuccess()
    null
  } catch (e: FirebaseNetworkException) {
    Errors.network
  } catch (e: java.lang.Exception) {
    Errors.unknown
  }

  suspend fun setT2(t2: T2, onSuccess: suspend (T2) -> Unit): ErrorApp? = try {
    // add doc
    val doc = FirebaseService.docsCollection.add(t2).await()
    t2.id = doc.id

    val employerId = "${t2.orgId}_${t2.tableNumber}"
    EmployeesRepository.addT2(employerId, t2)

    onSuccess(t2)
    null
  } catch (e: FirebaseNetworkException) {
    Errors.network
  } catch (e: java.lang.Exception) {
    Errors.unknown
  }

  suspend fun setT3(t3: T3, onSuccess: () -> Unit ): ErrorApp? = try {
    val doc = FirebaseService.docsCollection.add(t3).await()
    t3.id = doc.id

    OrganizationRepository.addDocId(t3.organization!!.id, t3.id)
    onSuccess()
    null
  } catch (e: FirebaseNetworkException) {
    Errors.network
  } catch (e: java.lang.Exception) {
    Errors.unknown
  }

  suspend fun setT7(t7: T7, onSuccess: () -> Unit ): ErrorApp? = try {
    val doc = FirebaseService.docsCollection.add(t7).await()
    t7.id = doc.id

    OrganizationRepository.addDocId(t7.organization!!.id, t7.id)
    onSuccess()
    null
  } catch (e: FirebaseNetworkException) {
    Errors.network
  } catch (e: java.lang.Exception) {
    Errors.unknown
  }

  suspend fun setT5(t5: T5, onSuccess: () -> Unit): ErrorApp? = try {
    // add doc
    val doc = FirebaseService.docsCollection.add(t5).await()
    t5.id = doc.id

    EmployeesRepository.addT5(t5.employer!!.id, t5)
    onSuccess()

    null
  } catch (e: FirebaseNetworkException) {
    Errors.network
  } catch (e: java.lang.Exception) {
    Errors.unknown
  }

  suspend fun setT6(t6: T6, onSuccess: (T6, List<Vacation>) -> Unit): ErrorApp? = try {
    // add doc
    val doc = FirebaseService.docsCollection.add(t6).await()
    t6.id = doc.id

    val listOfVacations = EmployeesRepository.addT6(t6.employer!!.id, t6)
    onSuccess(t6, listOfVacations)
    null
  } catch (e: FirebaseNetworkException) {
    Errors.network
  } catch (e: java.lang.Exception) {
    Errors.unknown
  }

  suspend fun setT8(t8: T8, onSuccess: () -> Unit): ErrorApp? = try {
    // add doc
    val doc = FirebaseService.docsCollection.add(t8).await()
    t8.id = doc.id

    val employer = t8.employer
    val employerId = employer.id
    val divisionId = t8.division!!.id
    val orgId = t8.organization!!.id
    val docId = doc.id

    // delete on division
    DivisionsRepository.removeEmployer(divisionId, employerId)

    // change employer`s T2
    EmployeesRepository.dismissal(t8)

    // add doc to user
    EmployeesRepository.addDoc(employerId, docId)

    // add doc to organization
    OrganizationRepository.addDocId(orgId, docId)

    // add doc to division
    DivisionsRepository.addDoc(divisionId, docId)


    onSuccess()
    null
  } catch (e: FirebaseNetworkException) {
    Errors.network
  } catch (e: java.lang.Exception) {
    Errors.unknown
  }

  suspend fun setT11(t11: T11, onSuccess: (T11, Gift) -> Unit): ErrorApp? = try {
    // add doc
    val doc = FirebaseService.docsCollection.add(t11).await()
    t11.id = doc.id

    val gift = EmployeesRepository.addT11(t11.employer!!.id, t11)
    onSuccess(t11, gift)

    null
  } catch (e: FirebaseNetworkException) {
    Errors.network
  } catch (e: java.lang.Exception) {
    Errors.unknown
  }

  fun addVacation(t2Id: String, vacation: Vacation): Vacation? {
    val nullDate = Date(0)

    if (vacation.vacationStart == nullDate || vacation.vacationEnd == nullDate || vacation.workStart == nullDate || vacation.workEnd == nullDate) return null
    changeList(FIELD_VACATIONS, t2Id, vacation, Action.ADD)
    return vacation
  }

  fun addGift(t2Id: String, gift: Gift) {
    changeList(FIELD_GIFT, t2Id, gift, Action.ADD)

  }

  private fun changeList(field: String, t2Id: String, value: Any, action: Action) {
    val fv = when (action) {
      Action.REMOVE -> FieldValue.arrayRemove(value)
      Action.ADD -> FieldValue.arrayUnion(value)
      else -> return
    }

    FirebaseService.docsCollection.document(t2Id).update(field, fv)
  }


  suspend fun setFile(orgId: String, fileUri: Uri, onSuccess: suspend (Uri) -> Unit): ErrorApp? =
    try {
      val time = getCurrentTime().time
      val doc = FirebaseService.storage.reference.child("${DOCUMENTS_FOLDER}/${orgId}/${time}.pdf")
        .putFile(fileUri).await()

      val file = doc.storage.downloadUrl.await()
      onSuccess(file)

      null
    } catch (e: FirebaseNetworkException) {
      Errors.network
    } catch (e: java.lang.Exception) {
      Errors.unknown
    }

  suspend fun changeT2AfterT1(t1: T1, t2: T2): ErrorApp? = try {
    val experience = WorkExperience(
      date = getCurrentTime(),
      divisionName = t1.division!!.name,
      place = t1.position!!.name,
      salary = t1.position!!.salary.toRub(),
      doc = "Приказ №${t1.number} от ${t1.dataCreated.toDocFormat()}"
    )

    FirebaseService.docsCollection.document(t2.id)
      .update(FIELD_WORKS, FieldValue.arrayUnion(experience)).await()

    FirebaseService.docsCollection.document(t2.id).update(FIELD_NATURE_OF_WORK, t1.natureOfWork)
      .await()

    null
  } catch (e: FirebaseNetworkException) {
    Errors.network
  } catch (e: java.lang.Exception) {
    Errors.unknown
  }

  private suspend fun loadDocument(docId: String): DocumentSnapshot? {
    return if (docId.isEmpty()) null else FirebaseService.docsCollection.document(docId).get()
      .await()
  }

  suspend fun loadT2(docId: String): T2? {
    val doc = loadDocument(docId) ?: return null
    val t2 = doc.toObject(T2::class.java)
    t2?.id = doc.id

    return t2
  }


  suspend fun loadT8(docId: String?): T8? {
    if (docId == null || docId == "") return null

    val doc = loadDocument(docId) ?: return null
    val t8 = doc.toObject(T8::class.java)
    t8?.id = doc.id

    return t8
  }

  suspend fun loadT1(docId: String): T1? {
    val doc = loadDocument(docId) ?: return null
    val t1 = doc.toObject(T1::class.java)
    t1?.id = doc.id

    return t1
  }

  suspend fun updateT2(docId: String, newT2: T2, onSuccess: () -> Unit): ErrorApp? = try {
    FirebaseService.docsCollection.document(docId).set(newT2).await()
    onSuccess()
     null
  } catch (e: FirebaseNetworkException) {
    Errors.network
  } catch (e: java.lang.Exception) {
    Errors.unknown
  }

  private const val DOCUMENTS_FOLDER = "documents"
  private const val FIELD_WORKS = "works"
  private const val FIELD_NATURE_OF_WORK = "natureOfWork"
  private const val FIELD_VACATIONS = "vocations"
  private const val FIELD_GIFT = "gifts"
}