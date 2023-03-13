package com.elka.heofficeclub.service.repository

import com.elka.heofficeclub.other.ErrorApp
import com.elka.heofficeclub.other.Errors
import com.google.firebase.FirebaseNetworkException

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
}