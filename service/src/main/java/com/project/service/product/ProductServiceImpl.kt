package com.project.service.product

import android.content.Context
import com.project.api.product.Product
import com.project.api.product.ProductService

class ProductServiceImpl : ProductService{
    override fun getProductService(context: Context): List<Product> {
        val repository= ProductFactory.getRepository(context)
        return repository.getProduct(context)
    }
}
