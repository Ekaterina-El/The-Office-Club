package com.elka.heofficeclub.service.repository

import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

object FirebaseService {
    private const val USERS_COLLECTION = "users"
    private const val ORGANIZATION_COLLECTION = "organizations"
    private const val DIVISIONS_COLLECTION = "divisions"
    private const val ORG_POSITIONS_COLLECTION = "org_positions"

    val usersCollection by lazy { Firebase.firestore.collection(USERS_COLLECTION) }
    val organizationsCollection by lazy { Firebase.firestore.collection(ORGANIZATION_COLLECTION) }
    val divisionsCollection by lazy { Firebase.firestore.collection(DIVISIONS_COLLECTION) }
    val orgPositionsCollection by lazy { Firebase.firestore.collection(ORG_POSITIONS_COLLECTION) }
}