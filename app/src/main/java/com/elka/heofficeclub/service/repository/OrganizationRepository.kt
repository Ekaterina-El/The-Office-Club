package com.elka.heofficeclub.service.repository

import com.elka.heofficeclub.other.Action
import com.elka.heofficeclub.other.ErrorApp
import com.elka.heofficeclub.other.Errors
import com.elka.heofficeclub.service.model.Organization
import com.google.firebase.FirebaseNetworkException
import com.google.firebase.firestore.FieldValue
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

  suspend fun updateOrganization(
    organizationId: String,
    newOrganization: Organization,
    onSuccess: (newOrganization: Organization) -> Unit
  ): ErrorApp? = try {
    val data = mutableMapOf(
      Pair(FIELD_FULL_NAME, newOrganization.fullName),
      Pair(FIELD_SHORT_NAME, newOrganization.shortName),
      Pair(FIELD_ADDRESS, newOrganization.address),
    )
    FirebaseService.organizationsCollection.document(organizationId).update(data).await()
    onSuccess(newOrganization)
    null
  } catch (e: FirebaseNetworkException) {
    Errors.network
  } catch (e: java.lang.Exception) {
    Errors.unknown
  }

  private fun changeListOfDivision(organizationId: String, divisionId: String, action: Action) {
    val value = when (action) {
      Action.REMOVE -> FieldValue.arrayRemove(divisionId)
      Action.ADD -> FieldValue.arrayUnion(divisionId)
      else -> return
    }

    FirebaseService.organizationsCollection.document(organizationId).update(
      FIELD_DIVISIONS, value
    )
  }

  fun addDivision(organizationId: String, divisionId: String) {
    changeListOfDivision(organizationId, divisionId, Action.ADD)
  }

  fun removeDivision(organizationId: String, divisionId: String) {
    changeListOfDivision(organizationId, divisionId, Action.REMOVE)
  }

  private const val FIELD_ADDRESS = "address"
  private const val FIELD_SHORT_NAME = "shortName"
  private const val FIELD_FULL_NAME = "fullName"
  private const val FIELD_DIVISIONS = "divisionsId"
}