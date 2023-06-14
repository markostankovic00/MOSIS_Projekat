package com.example.mosisprojekat.repository.implementations

import android.net.Uri
import com.example.mosisprojekat.models.UserData
import com.example.mosisprojekat.repository.interactors.UsersDataRepositoryInteractor
import com.example.mosisprojekat.util.Resource
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.ListenerRegistration
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.ktx.storage
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow

const val USERS_DATA_COLLECTION_REF = "users_data"

const val USER_IMAGES_STORAGE_REF = "users_images"

class UsersDataRepository: UsersDataRepositoryInteractor {

    private val usersDataRef: CollectionReference =
        Firebase.firestore.collection(USERS_DATA_COLLECTION_REF)

    private val usersImagesRef: StorageReference =
        Firebase.storage.reference.child(USER_IMAGES_STORAGE_REF)


    override fun getUserData(
        userId: String,
        onError: (Throwable?) -> Unit,
        onSuccess: (UserData?) -> Unit
    ) {
        usersDataRef
            .document(userId)
            .get()
            .addOnSuccessListener { onSuccess.invoke(it.toObject(UserData::class.java)) }
            .addOnFailureListener { onError.invoke(it.cause) }
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

        val userData = UserData(
            userId = userId,
            name = name,
            surname = surname,
            email = email,
            points = points
        )

        usersDataRef
            .document(userId)
            .set(userData)
            .addOnCompleteListener { result ->
                onComplete.invoke(result.isSuccessful)
            }
    }

    override fun updateUserPoints(
        userId: String,
        points: Int,
        onComplete: (Boolean) -> Unit
    ) {

        usersDataRef.document(userId)
            .update("points", FieldValue.increment(points.toLong()))
            .addOnCompleteListener { result ->
                onComplete.invoke(result.isSuccessful)
            }
    }

    override fun uploadUserPhoto(
        userId: String,
        photoUri: Uri,
        onSuccess: () -> Unit,
        onError: (Throwable?) -> Unit
    ) {
        val imageRef = usersImagesRef.child(userId)
        imageRef.putFile(photoUri).addOnCompleteListener { result ->
            if (result.isSuccessful) {
                imageRef.downloadUrl.addOnSuccessListener { uri ->
                    usersDataRef.document(userId)
                        .update("photoUrl", uri.toString())
                        .addOnSuccessListener {
                            onSuccess.invoke()
                        }
                        .addOnFailureListener {
                            onError.invoke(it.cause)
                        }
                }
            } else {
                onError.invoke(result.exception?.cause)
            }
        }
    }
}