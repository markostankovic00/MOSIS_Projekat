package com.example.mosisprojekat.repository.interactors

import com.example.mosisprojekat.models.UserData
import com.example.mosisprojekat.util.Resource
import kotlinx.coroutines.flow.Flow

interface UsersDataRepositoryInteractor {

    fun getUserData(
        userId: String,
        onError: (Throwable?) -> Unit,
        onSuccess: (UserData?) -> Unit
    )

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
        userId: String,
        points: Int,
        onComplete: (Boolean) -> Unit
    )
}