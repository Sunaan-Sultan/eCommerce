package com.project.api.product

import android.content.Context

interface ProductService {
    fun getProductService(context: Context): List<Product>
}