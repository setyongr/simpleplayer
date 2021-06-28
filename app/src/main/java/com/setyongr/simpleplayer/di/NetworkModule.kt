package com.setyongr.simpleplayer.di

import com.setyongr.simpleplayer.data.ItunesService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {
    @Provides
    fun provideItunesService(): ItunesService {
        return Retrofit.Builder()
            .baseUrl(ITUNES_URL)
            .addConverterFactory(MoshiConverterFactory.create())
            .build()
            .create(ItunesService::class.java)
    }

    private const val ITUNES_URL = "https://itunes.apple.com"
}