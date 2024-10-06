package com.anthojb.simple.apirestpruebatecnica.repository

import com.anthojb.simple.apirestpruebatecnica.datasource.RestDataSource
import com.anthojb.simple.apirestpruebatecnica.model.Product
import javax.inject.Inject

interface ProductRepository{
    suspend fun getProducts():List<Product>
}

class ProductRepositoryImp @Inject constructor(
    private val dataSource: RestDataSource
): ProductRepository {
    override suspend fun getProducts(): List<Product> {
        return dataSource.getProducts()
    }


}