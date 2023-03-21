package com.elka.heofficeclub.service.repository

import com.elka.heofficeclub.other.Action
import com.elka.heofficeclub.other.ErrorApp
import com.elka.heofficeclub.other.Errors
import com.elka.heofficeclub.service.model.Employer
import com.google.firebase.FirebaseNetworkException
import com.google.firebase.firestore.FieldValue
import kotlinx.coroutines.tasks.await

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

  suspend fun createEmployer(employer: Employer, onSuccess: suspend (Employer) -> Unit): ErrorApp? = try {
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

  suspend fun addT2(employerId: String, docId: String) {
    // add doc id to employer docs
    changeList(FIELD_DOCS, employerId, docId, Action.ADD)

    // set doc id to employer T2
    FirebaseService.employeesCollection.document(employerId).update(FIELD_T2, docId).await()
  }

  private fun changeList(field: String, employerId: String, value: Any, action: Action) {
    val fv = when (action) {
      Action.REMOVE -> FieldValue.arrayRemove(value)
      Action.ADD -> FieldValue.arrayUnion(value)
      else -> return
    }

    FirebaseService.employeesCollection.document(employerId).update(field, fv)
  }

  const val FIELD_T2 = "T2"
  const val FIELD_DOCS = "DOCS"
}