package com.anthojb.simple.apirestpruebatecnica.model

data class ApiResponse (
    val results: List<Product> = emptyList(),
)

data class Product(
    val id: Int,
    val title: String,
    val price: Double,
    val description: String,
    val images: List<String>,
    val category: Category
)

data class Category(
    val id: Int,
    val name: String,
    val image: String
)