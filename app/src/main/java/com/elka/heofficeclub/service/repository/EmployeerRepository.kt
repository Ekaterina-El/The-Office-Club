package com.elka.heofficeclub.service.repository

import com.elka.heofficeclub.other.ErrorApp
import com.elka.heofficeclub.other.Errors
import com.elka.heofficeclub.service.model.Employer
import com.google.firebase.FirebaseNetworkException
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

  suspend fun createEmployer(employer: Employer, onSuccess: (Employer) -> Unit): ErrorApp? = try {
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
}