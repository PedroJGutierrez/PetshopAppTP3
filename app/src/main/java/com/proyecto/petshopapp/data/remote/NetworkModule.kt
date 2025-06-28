package com.proyecto.petshopapp.data.remote


import com.proyecto.petshopapp.data.remote.AuthApiService
import com.proyecto.petshopapp.data.remote.NetworkClient
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Provides
    @Singleton
    fun provideAuthApiService(): AuthApiService {
        return NetworkClient.authApiService
    }
}
