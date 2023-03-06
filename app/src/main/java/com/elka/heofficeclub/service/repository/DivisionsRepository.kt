package com.elka.heofficeclub.service.repository

import com.elka.heofficeclub.other.ErrorApp
import com.elka.heofficeclub.other.Errors
import com.elka.heofficeclub.service.model.Division
import com.google.firebase.FirebaseNetworkException
import kotlinx.coroutines.tasks.await

object DivisionsRepository {
  suspend fun addDivision(
    division: Division,
    organizationId: String,
    onSuccess: (division: Division) -> Unit
  ): ErrorApp? = try {
    val doc = FirebaseService.divisionsCollection.add(division).await()
    division.id = doc.id

    // added id of division to organization list of divisions
    OrganizationRepository.addDivision(organizationId, division.id)

    onSuccess(division)
    null
  } catch (e: FirebaseNetworkException) {
    Errors.network
  } catch (e: java.lang.Exception) {
    Errors.unknown
  }

  suspend fun getDivisions(
    divisionsId: List<String>,
    onSuccess: (divisions: List<Division>) -> Unit
  ): ErrorApp? = try {
    val divisions = divisionsId.map { getDivision(it) }
    onSuccess(divisions)
    null
  } catch (e: FirebaseNetworkException) {
    Errors.network
  } catch (e: Exception) {
    Errors.unknown
  }

  private suspend fun getDivision(id: String): Division {
    val doc = FirebaseService.divisionsCollection.document(id).get().await()
    val division = doc.toObject(Division::class.java)!!
    division.id = doc.id
    return division
  }

  suspend fun deleteDivision(division: Division, onSuccess: () -> Unit): ErrorApp? = try {
    // delete note of division
    FirebaseService.divisionsCollection.document(division.id).delete().await()

    // change list of division on note organization
    OrganizationRepository.removeDivision(division.organization, division.id)

    // remove users of division
    // todo: remove users of division

    onSuccess()
    null
  } catch (e: FirebaseNetworkException) {
    Errors.network
  } catch (e: java.lang.Exception) {
    Errors.unknown
  }
}