package com.elka.heofficeclub.service.repository

import com.elka.heofficeclub.other.ErrorApp
import com.elka.heofficeclub.other.Errors
import com.elka.heofficeclub.service.model.documents.forms.T2
import com.google.firebase.FirebaseNetworkException
import kotlinx.coroutines.tasks.await

object DocumentsRepository {
  suspend fun setT2(t2: T2, onSuccess: () -> Unit): ErrorApp? = try {
    // add doc
    val doc = FirebaseService.docsCollection.add(t2).await()
    t2.id = doc.id

    val employerId = "${t2.orgId}_${t2.tableNumber}"
    EmployeesRepository.addT2(employerId, t2.id)

    onSuccess()
     null
  } catch (e: FirebaseNetworkException) {
    Errors.network
  } catch (e: java.lang.Exception) {
    Errors.unknown
  }


}