package com.elka.heofficeclub.service.repository

import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

object FirebaseService {
    private const val USERS_COLLECTION = "users"
    private const val ORGANIZATION_COLLECTION = "organizations"

    val usersCollection by lazy { Firebase.firestore.collection(USERS_COLLECTION) }
    val organizationsCollection by lazy { Firebase.firestore.collection(ORGANIZATION_COLLECTION) }
}