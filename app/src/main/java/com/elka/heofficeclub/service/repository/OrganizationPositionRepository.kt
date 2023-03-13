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
  }


}