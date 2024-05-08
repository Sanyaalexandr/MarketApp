package com.example.marketapp.di

import com.example.marketapp.data.repository.categories.CategoriesRepository
import com.example.marketapp.data.repository.categories.CategoriesRepositoryImpl
import com.example.marketapp.data.repository.products.ProductsRepository
import com.example.marketapp.data.repository.products.ProductsRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {
    @Binds
    @Singleton
    abstract fun bindProductsRepository(
        productsRepository: ProductsRepositoryImpl,
    ): ProductsRepository

    @Binds
    @Singleton
    abstract fun bindCategoriesRepository(
        categoriesRepository: CategoriesRepositoryImpl,
    ): CategoriesRepository
}