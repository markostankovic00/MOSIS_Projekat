package com.example.mosisprojekat.repository.interactors

import com.example.mosisprojekat.models.UserData
import com.example.mosisprojekat.util.Resource
import kotlinx.coroutines.flow.Flow

interface UsersDataRepositoryInteractor {

    fun getUserData(userId: String): Flow<Resource<List<UserData>>>

    fun getAllUsersData(): Flow<Resource<List<UserData>>>

    fun addUserData(
        userId: String,
        name: String,
        surname: String,
        email: String,
        points: Int,
        onComplete: (Boolean) -> Unit
    )

    fun updateUserPoints(
        userDataId: String,
        points: Int,
        onComplete: (Boolean) -> Unit
    )
}