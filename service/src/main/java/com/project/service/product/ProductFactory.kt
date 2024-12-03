package com.project.service.product

import android.content.Context
import com.project.api.product.ProductRepository
import com.project.api.product.ProductService
import com.project.repository.product.ProductLocalRepositoryImpl
import com.project.repository.product.ProductRepositoryImpl
import com.project.service.RuntimeProfile

object ProductFactory {
    fun getService(): ProductService {
        return ProductServiceImpl()
    }

    fun getRepository(context: Context): ProductRepository {
        if (RuntimeProfile.getCurrentRuntime() == RuntimeProfile.LIVE_RUNTIME) {
            return ProductRepositoryImpl()
        } else {
            return ProductLocalRepositoryImpl.getInstance(context)
        }
    }
}