package com.example.mosisprojekat.repository.interactors

import com.example.mosisprojekat.models.Gym
import com.example.mosisprojekat.util.Resource
import kotlinx.coroutines.flow.Flow

interface GymRepositoryInteractor {

    fun getAllGyms(): Flow<Resource<List<Gym>>>

    fun getGym(
        gymId: String,
        onError: (Throwable?) -> Unit,
        onSuccess: (Gym?) -> Unit
    )

    fun addGym(
        userId: String,
        name: String,
        lat: Double,
        lng: Double,
        onComplete: (Boolean) -> Unit
    )

    fun deleteGym(
        gymId: String,
        onComplete: (Boolean) -> Unit
    )

    fun addGymReview(
        gymId: String,
        comment: String,
        mark: Int,
        onComplete: (Boolean) -> Unit
    )

    fun updateGymName(
        gymId: String,
        name:String,
        onComplete: (Boolean) -> Unit
    )

}