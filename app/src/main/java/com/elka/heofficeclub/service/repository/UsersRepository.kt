package com.elka.heofficeclub.service.repository

import com.elka.heofficeclub.other.ErrorApp
import com.elka.heofficeclub.other.Errors
import com.elka.heofficeclub.service.model.User
import com.google.firebase.FirebaseNetworkException
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import com.google.firebase.auth.FirebaseAuthWeakPasswordException
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.tasks.await
import java.lang.Exception

object UsersRepository {
    private val auth = Firebase.auth

    suspend fun registrationUser(email: String, password: String, onSuccess: suspend (uid: String) -> Unit): ErrorApp? = try {
        val uid = auth.createUserWithEmailAndPassword(email, password).await().user!!.uid
        onSuccess(uid)
        null
    } catch (e: FirebaseNetworkException) {
        Errors.network
    } catch (e: FirebaseAuthWeakPasswordException) {
        Errors.weakPassword
    } catch (e: FirebaseAuthUserCollisionException) {
        Errors.userCollision
    } catch (e: Exception) {
        Errors.unknown
    }
}
