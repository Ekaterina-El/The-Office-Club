package com.elka.heofficeclub.service.repository

import com.elka.heofficeclub.other.Action
import com.elka.heofficeclub.other.ErrorApp
import com.elka.heofficeclub.other.Errors
import com.elka.heofficeclub.service.model.Employer
import com.elka.heofficeclub.service.model.documents.forms.T1
import com.elka.heofficeclub.service.model.documents.forms.T2
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

  suspend fun createEmployer(employer: Employer, onSuccess: suspend (Employer) -> Unit): ErrorApp? =
    try {
      // create employer
      val id = "${employer.organizationId}_${employer.tableNumber}"
      FirebaseService.employeesCollection.document(id).set(employer).await()
      employer.id = id

      // add employer id to org node
      OrganizationRepository.addEmployer(employer.organizationId, employer.id)

      // add employer id to division node
      DivisionsRepository.addEmployer(employer.organizationId, employer.id)

      onSuccess(employer)
      null
    } catch (_: FirebaseNetworkException) {
      Errors.network
    } catch (_: java.lang.Exception) {
      Errors.unknown
    }

  suspend fun addT2(employerId: String, docId: String) {
    // add doc id to employer docs
    addDoc(employerId, docId)

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

  fun addT1(employerId: String, t1: T1) {
    // add doc id to employer docs
    addDoc(employerId, t1.id)

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
      employer.divisionLocal = DivisionsRepository.loadDivisionInfo(employer.divisionId)
      employer.positionLocal = OrganizationPositionRepository.loadPosition(employer.positionId)
    }

    return employer
  }

  const val FIELD_T2 = "t2"
  const val FIELD_DOCS = "docs"
}