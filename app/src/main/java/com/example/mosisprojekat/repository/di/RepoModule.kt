package com.example.mosisprojekat.repository.di

import com.example.mosisprojekat.repository.implementations.AuthRepository
import com.example.mosisprojekat.repository.interactors.AuthRepositoryInteractor
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

}