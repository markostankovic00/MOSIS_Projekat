package com.example.mosisprojekat.repository.di

import com.example.mosisprojekat.repository.implementations.AuthRepository
import com.example.mosisprojekat.repository.implementations.UsersDataRepository
import com.example.mosisprojekat.repository.interactors.AuthRepositoryInteractor
import com.example.mosisprojekat.repository.interactors.UsersDataRepositoryInteractor
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
open class RepoModule {

    @Provides
    @Singleton
    fun provideAuthRepository(): AuthRepositoryInteractor {
        return AuthRepository()
    }

    @Provides
    @Singleton
    fun provideUserDataRepository(): UsersDataRepositoryInteractor {
        return UsersDataRepository()
    }

}