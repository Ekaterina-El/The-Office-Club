package com.elka.heofficeclub.service.repository

import com.elka.heofficeclub.other.Action
import com.elka.heofficeclub.other.ErrorApp
import com.elka.heofficeclub.other.Errors
import com.elka.heofficeclub.other.Role
import com.elka.heofficeclub.other.Role.*
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

  private fun changeList(field: String, organizationId: String, value: Any, action: Action) {
    val fv = when (action) {
      Action.REMOVE -> FieldValue.arrayRemove(value)
      Action.ADD -> FieldValue.arrayUnion(value)
      else -> return
    }

    FirebaseService.organizationsCollection.document(organizationId).update(field, fv)
  }

  fun addDivision(organizationId: String, divisionId: String) {
    changeList(FIELD_DIVISIONS, organizationId, divisionId, Action.ADD)
  }

  fun removeDivision(organizationId: String, divisionId: String) {
    changeList(FIELD_DIVISIONS, organizationId, divisionId, Action.REMOVE)
  }

  fun addEditor(organizationId: String, editorId: String, onSuccess: () -> Unit): ErrorApp? = try {
    changeList(FIELD_EDITORS, organizationId, editorId, Action.ADD)
    onSuccess()
    null
  } catch (e: FirebaseNetworkException) {
    Errors.network
  } catch (e: java.lang.Exception) {
    Errors.unknown
  }

  suspend fun changeHead(
    organizationId: String,
    headRole: Role,
    currentHeadId: String,
    newHeadId: String,
    onSuccess: () -> Unit
  ): ErrorApp? = try {

    // add currentHeadId to editors list
    addEditor(organizationId, currentHeadId) {}

    // say to currentHead what you`re editor
    UsersRepository.changeUserRole(currentHeadId, Role.EDITOR)

    // set headId to field
    val field = when(headRole) {
      HUMAN_RESOURCES_DEPARTMENT_HEAD -> FIELD_HRD_HEAD
      ORGANIZATION_HEAD -> FIELD_ORG_HEAD
      EDITOR -> ""
    }

    FirebaseService.organizationsCollection.document(organizationId).update(
      field, newHeadId
    ).await()

    // say to newHeadId what you`re <headRole>
    UsersRepository.changeUserRole(newHeadId, headRole)

    onSuccess()
    null
  } catch (_: FirebaseNetworkException) {
    Errors.network
  } catch (_: Exception) {
    Errors.unknown
  }


  fun addPosition(organizationId: String, positionId: String, onSuccess: () -> Unit): ErrorApp? = try {
    changeList(FIELD_POSITIONS, organizationId, positionId, Action.ADD)
    onSuccess()
    null
  } catch (e: FirebaseNetworkException) {
    Errors.network
  } catch (e: java.lang.Exception) {
    Errors.unknown
  }

  private const val FIELD_HRD_HEAD = "humanResourcesDepartmentHeadId"
  private const val FIELD_ORG_HEAD = "organizationHeadId"

  private const val FIELD_ADDRESS = "address"
  private const val FIELD_POSITIONS = "positions"
  private const val FIELD_EDITORS = "editors"
  private const val FIELD_SHORT_NAME = "shortName"
  private const val FIELD_FULL_NAME = "fullName"
  private const val FIELD_DIVISIONS = "divisionsId"
}