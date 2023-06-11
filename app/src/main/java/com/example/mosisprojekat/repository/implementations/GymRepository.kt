package com.example.mosisprojekat.repository.implementations

import com.example.mosisprojekat.models.Gym
import com.example.mosisprojekat.models.Review
import com.example.mosisprojekat.repository.interactors.GymRepositoryInteractor
import com.example.mosisprojekat.util.Resource
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.ListenerRegistration
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow

const val GYMS_COLLECTION_REF = "gyms"

class GymRepository: GymRepositoryInteractor {

    private val gymsRef: CollectionReference =
        Firebase.firestore.collection(GYMS_COLLECTION_REF)
    private fun getUserId(): String = Firebase.auth.currentUser?.uid ?: ""

    override fun getAllGyms(): Flow<Resource<List<Gym>>> = callbackFlow {

        var snapshotStateListener: ListenerRegistration? = null

        try {
            snapshotStateListener = gymsRef
                .whereNotEqualTo("name", "")
                .addSnapshotListener { snapshot, e ->
                    val response = if (snapshot != null) {
                        val gym = snapshot.toObjects(Gym::class.java)
                        Resource.Success(data = gym)
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

    override fun getGym(
        gymId: String,
        onError: (Throwable?) -> Unit,
        onSuccess: (Gym?) -> Unit
    ) {
        gymsRef
            .document(gymId)
            .get()
            .addOnSuccessListener {
                onSuccess.invoke(it.toObject(Gym::class.java))
            }
            .addOnFailureListener { result ->
                onError.invoke(result.cause)
            }
    }

    override fun addGym(
        userId: String,
        name: String,
        lat: Double,
        lng: Double,
        onComplete: (Boolean) -> Unit
    ) {
        val documentId = gymsRef.document().id

        val gym = Gym(
            userId = userId,
            name = name,
            lat = lat,
            lng = lng,
            documentId = documentId
        )

        gymsRef
            .document(documentId)
            .set(gym)
            .addOnCompleteListener { result ->
                onComplete.invoke(result.isSuccessful)
            }
    }

    override fun deleteGym(gymId: String, onComplete: (Boolean) -> Unit) {
        gymsRef
            .document(gymId)
            .delete()
            .addOnCompleteListener {
                onComplete.invoke(it.isSuccessful)
            }
    }

    override fun addGymReview(
        gymId: String,
        comment: String,
        mark: Int,
        onComplete: (Boolean) -> Unit
    ) {

        val newReview = Review(
            userId = getUserId(),
            comment = comment,
            mark = mark
        )

        gymsRef
            .document(gymId)
            .update("reviews", FieldValue.arrayUnion(newReview))
            .addOnCompleteListener { result ->
                onComplete.invoke(result.isSuccessful)
            }

    }

    override fun updateGymName(gymId: String, name: String, onComplete: (Boolean) -> Unit) {
        gymsRef
            .document(gymId)
            .update("name", name)
            .addOnCompleteListener {
                onComplete(it.isSuccessful)
            }
    }
}