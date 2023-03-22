package com.elka.heofficeclub.service.repository

import android.net.Uri
import android.util.Log
import com.elka.heofficeclub.other.*
import com.elka.heofficeclub.other.documents.WorkExperience
import com.elka.heofficeclub.service.model.documents.forms.T1
import com.elka.heofficeclub.service.model.documents.forms.T2
import com.google.firebase.FirebaseNetworkException
import com.google.firebase.firestore.FieldValue
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.tasks.await

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
    EmployeesRepository.addT2(employerId, t2.id)

    onSuccess(t2)
    null
  } catch (e: FirebaseNetworkException) {
    Errors.network
  } catch (e: java.lang.Exception) {
    Errors.unknown
  }

  suspend fun setFile(orgId: String, fileUri: Uri, onSuccess: suspend (Uri) -> Unit): ErrorApp? =
    try {
      val time = getCurrentTime().time
      val doc = FirebaseService.storage.reference
        .child("${DOCUMENTS_FOLDER}/${orgId}/${time}.pdf")
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

    Log.d("changeT2AfterT1", "t2_id: ${t2.id}, exp: ${experience}")

    FirebaseService.docsCollection.document(t2.id)
      .update(FIELD_WORKS, FieldValue.arrayUnion(experience))
      .await()

    FirebaseService.docsCollection.document(t2.id).update(FIELD_NATURE_OF_WORK, t1.natureOfWork)
      .await()

    null
  } catch (e: FirebaseNetworkException) {
    Errors.network
  } catch (e: java.lang.Exception) {
    Errors.unknown
  }

  private const val DOCUMENTS_FOLDER = "documents"
  private const val FIELD_WORKS = "works"
  private const val FIELD_NATURE_OF_WORK = "natureOfWork"
}