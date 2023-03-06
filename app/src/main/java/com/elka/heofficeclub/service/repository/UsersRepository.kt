package com.elka.heofficeclub.service.repository

import com.elka.heofficeclub.other.ErrorApp
import com.elka.heofficeclub.other.Errors
import com.elka.heofficeclub.service.model.User
import com.google.firebase.FirebaseNetworkException
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthInvalidUserException
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import com.google.firebase.auth.FirebaseAuthWeakPasswordException
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.tasks.await
import java.lang.Exception

object UsersRepository {
    private val auth = Firebase.auth
    val currentUid get() = auth.currentUser?.uid


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

    suspend fun addUser(user: User, onSuccess: (() -> Unit) = {} ): ErrorApp? = try {
        FirebaseService.usersCollection.document(user.id).set(user).await()
        onSuccess()
        null
    } catch (e: FirebaseNetworkException) {
        Errors.network
    } catch (e: Exception) {
        Errors.unknown
    }

    suspend fun logout(onSuccess: suspend () -> Unit) {
        auth.signOut()
        onSuccess()
    }

    suspend fun isUniqEmail(email: String): Boolean {
        val docs = FirebaseService.usersCollection.whereEqualTo(FIELD_EMAIL, email).limit(1).get().await()
        return docs.size() == 0
    }

    suspend fun login(email: String, password: String, onSuccess: () -> Unit): ErrorApp? = try {
        auth.signInWithEmailAndPassword(email, password).await()
        onSuccess()
        null
    } catch (e: FirebaseNetworkException) {
        Errors.network
    } catch (e: FirebaseAuthInvalidUserException) {
        Errors.invalidEmailPassword
    } catch (e: Exception) {
        Errors.unknown
    }

    suspend fun loadCurrentUserProfile(onSuccess: (User) -> Unit): ErrorApp? = try {
        val profile = getUserById(currentUid!!)
        onSuccess(profile)
        null
    } catch (e: FirebaseNetworkException) {
        Errors.network
    } catch (e: Exception) {
        Errors.unknown
    }

    suspend fun getUserById(id: String): User {
        val doc = FirebaseService.usersCollection.document(id).get().await()
        val user = doc.toObject(User::class.java)
        user!!.id = doc.id
        return user
    }

    const val FIELD_EMAIL = "email"
}
