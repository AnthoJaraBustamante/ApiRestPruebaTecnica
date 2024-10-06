package com.anthojb.simple.apirestpruebatecnica.di

import com.anthojb.simple.apirestpruebatecnica.repository.ProductRepository
import com.anthojb.simple.apirestpruebatecnica.repository.ProductRepositoryImp
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {
    @Singleton
    @Binds
    abstract fun provideBaseUrl(repo: ProductRepositoryImp): ProductRepository
}