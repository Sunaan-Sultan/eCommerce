package com.project.api.product

import android.content.Context

interface ProductRepository {
    fun getProduct(context: Context): List<Product>
    fun setProduct(product: List<Product>)
}