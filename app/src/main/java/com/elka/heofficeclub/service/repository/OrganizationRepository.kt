package com.elka.heofficeclub.service.repository

import com.elka.heofficeclub.other.ErrorApp
import com.elka.heofficeclub.other.Errors
import com.elka.heofficeclub.service.model.Organization
import com.google.firebase.FirebaseNetworkException
import kotlinx.coroutines.tasks.await

object OrganizationRepository {
  suspend fun addOrganization(
    organization: Organization,
    onSuccess: suspend (newOrganization: Organization) -> Unit
  ): ErrorApp? = try {
    val doc = FirebaseService.organizationsCollection.add(organization).await()
    organization.id = doc.id
    onSuccess(organization)

    null
  } catch (e: FirebaseNetworkException) {
    Errors.network
  } catch (e: java.lang.Exception) {
    Errors.unknown
  }

  suspend fun
      loadOrganization(organizationId: String, onSuccess: (Organization) -> Unit): ErrorApp? = try {
    val doc = FirebaseService.organizationsCollection.document(organizationId).get().await()
    val organization = doc.toObject(Organization::class.java)
    organization!!.id = doc.id

    // get org. head
    organization.organizationHeadLocal =
      UsersRepository.getUserById(organization.organizationHeadId)

    // get HRD head
    organization.organizationHumanResourcesDepartmentHeadLocal =
      UsersRepository.getUserById(organization.humanResourcesDepartmentHeadId)

    onSuccess(organization)
    null
  } catch (e: FirebaseNetworkException) {
    Errors.network
  } catch (e: java.lang.Exception) {
    Errors.unknown
  }
}