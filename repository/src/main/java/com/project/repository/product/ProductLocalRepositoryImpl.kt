package com.project.repository.product

import android.content.Context
import android.util.Log
import com.project.api.product.Product
import com.project.api.product.ProductRepository

class ProductLocalRepositoryImpl private constructor(private val context: Context) : ProductRepository {

    private var products: List<Product> = emptyList()

    override fun getProduct(context: Context): List<Product> {
        if (products.isEmpty()) {
            Log.w("ProductLocalRepositoryImpl", "Product list is empty! Ensure products are loaded.")
        }
        Log.d("ProductLocalRepositoryImpl", "Returning products: $products")
        return products
    }

    override fun setProduct(product: List<Product>) {
        products = product
        Log.d("ProductLocalRepositoryImpl", "Products set successfully: $products")
    }

    companion object {
        private var INSTANCE: ProductLocalRepositoryImpl? = null

        fun getInstance(context: Context): ProductLocalRepositoryImpl {
            return INSTANCE ?: synchronized(this) {
                INSTANCE ?: ProductLocalRepositoryImpl(context).also { INSTANCE = it }
            }
        }
    }
}
