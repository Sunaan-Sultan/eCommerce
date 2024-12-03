package com.project.service

import android.content.Context
import android.util.Log
import com.project.api.product.Product
import com.project.service.product.ProductFactory

class MockService(private val context: Context) {
    fun loadProduct(products: List<Product>) {
        val repo = ProductFactory.getRepository(context)
        repo.setProduct(products)
        Log.d("MockService", "Products loaded into repository: $products")
    }
}