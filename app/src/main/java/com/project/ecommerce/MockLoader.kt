package com.project.ecommerce

import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import com.project.api.product.Product
import com.project.service.MockService
import kotlinx.serialization.json.Json

class MockLoader(private val context: Context) {

    var isLoaded = false
        private set

    fun init() {
        if (isLoaded) return

        val service = MockService(context)

        val products = Json.decodeFromString<List<Product>>(loadJson("product")).toMutableList()
        Log.d("MockLoader", "Loaded JSON: $products")
        service.loadProduct(products)
        Log.d("MockLoader", "Products successfully loaded into repository.")

        Log.d("MockLoader", "Initializing mock data")
        Log.d("MockLoader", "Products initialized: ${products.size}")

        isLoaded = true
    }

    @SuppressLint("DiscouragedApi")
    fun loadJson(fileName: String): String {
        val packageName = context.packageName
        val resourceId = context.resources.getIdentifier(fileName, "raw", packageName)
        val inputStream = context.resources.openRawResource(resourceId)
        return inputStream.bufferedReader().use { it.readText() }
    }
}

