package com.elka.heofficeclub.service.repository

import com.elka.heofficeclub.other.ErrorApp
import com.elka.heofficeclub.other.Errors
import com.elka.heofficeclub.service.model.Division
import com.google.firebase.FirebaseNetworkException
import kotlinx.coroutines.tasks.await

object DivisionsRepository {
  suspend fun addDivisionToOrganization(
    division: Division,
    organizationId: String,
    onSuccess: (division: Division) -> Unit
  ): ErrorApp? = try {
    val doc = FirebaseService.divisionsCollection.add(division).await()
    division.id = doc.id

    // added id of division to organization list of divisions
    OrganizationRepository.addDivisionToOrganization(organizationId, division.id)

    onSuccess(division)
    null
  } catch (e: FirebaseNetworkException) {
    Errors.network
  } catch (e: java.lang.Exception) {
    Errors.unknown
  }
}