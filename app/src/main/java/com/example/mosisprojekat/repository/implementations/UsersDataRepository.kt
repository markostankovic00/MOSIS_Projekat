package com.example.mosisprojekat.repository.implementations

import com.example.mosisprojekat.models.UserData
import com.example.mosisprojekat.repository.interactors.UsersDataRepositoryInteractor
import com.example.mosisprojekat.util.Resource
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.ListenerRegistration
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow

const val USERS_DATA_COLLECTION_REF = "users_data"

class UsersDataRepository: UsersDataRepositoryInteractor {

    private val usersDataRef: CollectionReference =
        Firebase.firestore.collection(USERS_DATA_COLLECTION_REF)

    override fun getUserData(userId: String): Flow<Resource<List<UserData>>> = callbackFlow {

        var snapshotStateListener: ListenerRegistration? = null

        try {
            snapshotStateListener = usersDataRef
                .whereEqualTo("userId", userId)
                //.limit(1)
                .addSnapshotListener { snapshot, e ->
                    val response = if (snapshot != null) {
                        val userData = snapshot.toObjects(UserData::class.java)
                        Resource.Success(data = userData)
                    } else {
                        Resource.Error(message = e?.message ?: "Something went wrong")
                    }
                    trySend(response)
                }
        } catch (e:Exception) {
            trySend(Resource.Error(e.message ?: "Something went wrong"))
            e.printStackTrace()
        }

        awaitClose {
            snapshotStateListener?.remove()
        }
    }

    override fun getAllUsersData(): Flow<Resource<List<UserData>>> = callbackFlow {

        var snapshotStateListener: ListenerRegistration? = null

        try {
            snapshotStateListener = usersDataRef
                .whereNotEqualTo("userId", "")
                .addSnapshotListener { snapshot, e ->
                    val response = if (snapshot != null) {
                        val userData = snapshot.toObjects(UserData::class.java)
                        Resource.Success(data = userData)
                    } else {
                        Resource.Error(message = e?.message ?: "Something went wrong")
                    }
                    trySend(response)
                }
        } catch (e:Exception) {
            trySend(Resource.Error(e.message ?: "Something went wrong"))
            e.printStackTrace()
        }

        awaitClose {
            snapshotStateListener?.remove()
        }
    }

    override fun addUserData(
        userId: String,
        name: String,
        surname: String,
        email: String,
        points: Int,
        onComplete: (Boolean) -> Unit
    ) {
        val documentId = usersDataRef.document().id
        val userData = UserData(
            userId = userId,
            name = name,
            surname = surname,
            email = email,
            points = points,
            documentId = documentId
        )

        usersDataRef
            .document(documentId)
            .set(userData)
            .addOnCompleteListener { result ->
                onComplete.invoke(result.isSuccessful)
            }
    }

    override fun updateUserPoints(
        userDataId: String,
        points: Int,
        onComplete: (Boolean) -> Unit
    ) {
        usersDataRef.document(userDataId)
            .update("points", points)
            .addOnCompleteListener { result ->
                onComplete.invoke(result.isSuccessful)
            }
    }
}