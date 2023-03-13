package com.elka.heofficeclub.service.repository

import com.elka.heofficeclub.other.ErrorApp
import com.elka.heofficeclub.other.Errors
import com.elka.heofficeclub.service.model.OrganizationPosition
import com.google.firebase.FirebaseNetworkException
import kotlinx.coroutines.tasks.await

class OrganizationPositionRepository {
  companion object {
    suspend fun addPosition(
      position: OrganizationPosition,
      organizationId: String,
      onSuccess: (OrganizationPosition) -> Unit
    ): ErrorApp? = try {
      // create org. position
      val doc = FirebaseService.orgPositionsCollection.add(position).await()
      position.id = doc.id

      // add org position to organization note
      OrganizationRepository.addPosition(organizationId, position.id) {
        onSuccess(position)
      }

      null
    } catch (_: FirebaseNetworkException) {
      Errors.network
    } catch (_: java.lang.Exception) {
      Errors.unknown
    }

    suspend fun loadPositions(
      positionsId: List<String>,
      onSuccess: (List<OrganizationPosition>) -> Unit
    ): ErrorApp? = try {
      val list = positionsId.mapNotNull { loadPosition(it) }
      onSuccess(list)
      null
    } catch (_: FirebaseNetworkException) {
      Errors.network
    } catch (_: java.lang.Exception) {
      Errors.unknown
    }

    private suspend fun loadPosition(id: String): OrganizationPosition? = try {
      val doc = FirebaseService.orgPositionsCollection.document(id).get().await()
      val position = doc.toObject(OrganizationPosition::class.java)
      position!!.id = doc.id
      position
    } catch (e: java.lang.Exception) {
      null
    }
  }


}