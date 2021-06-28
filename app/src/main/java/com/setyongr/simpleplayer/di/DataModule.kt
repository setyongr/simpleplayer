package com.setyongr.simpleplayer.di

import com.setyongr.simpleplayer.data.repository.ItunesRepository
import com.setyongr.simpleplayer.data.repository.ItunesRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

@Module
@InstallIn(ViewModelComponent::class)
abstract class DataModule {
    @Binds
    abstract fun bindItunesRepository(itunesRepositoryImpl: ItunesRepositoryImpl): ItunesRepository
}