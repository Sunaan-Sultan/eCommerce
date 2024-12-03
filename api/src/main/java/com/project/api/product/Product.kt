package com.project.api.product
import kotlinx.serialization.Serializable

@Serializable
data class Product(
    val id: Int,
    val productName: String,
    val productCategory: String,
    val productDescription: String,
    val productPrice: Double,
    val imageUrl: String,
    var isInCart: Boolean = false
)
