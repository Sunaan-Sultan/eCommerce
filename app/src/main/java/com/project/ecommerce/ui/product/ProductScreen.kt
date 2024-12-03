package com.project.ecommerce.ui.product

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.*
import androidx.compose.material3.*
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavController
import com.project.api.product.Product
import com.project.ecommerce.ui.cart.CartManager
import com.project.service.product.ProductServiceImpl
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

@Composable
fun ProductScreen(navController: NavController, category: String) {
    val context = LocalContext.current
    val productService = ProductServiceImpl()
    var products by remember { mutableStateOf<List<Product>>(emptyList()) }
    var paginatedProducts by remember { mutableStateOf<List<Product>>(emptyList()) }
    var page by remember { mutableStateOf(1) }
    val itemsPerPage = 5

    LaunchedEffect(Unit) {
        val loadedProducts = withContext(Dispatchers.IO) {
            productService.getProductService(context)
        }.filter { it.productCategory == category }
        products = loadedProducts
        paginatedProducts = loadedProducts.take(itemsPerPage)
    }

    Column(modifier = Modifier
        .fillMaxSize()
        .background(if (isSystemInDarkTheme()) Color.Black else Color.White)
        .padding(16.dp)) {
        Text(
            text = category,
            style = MaterialTheme.typography.headlineMedium,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        if (paginatedProducts.isEmpty()) {
            Text("No products available.", style = MaterialTheme.typography.bodyLarge)
        } else {
            ProductList(
                products = paginatedProducts,
                onLoadMore = {
                    if (page * itemsPerPage < products.size) {
                        page++
                        paginatedProducts = products.take(page * itemsPerPage)
                    }
                },
                onProductClick = { product ->
                    navController.navigate("productDetail/${product.id}")
                },
                onAddToCart = { product ->
                    CartManager.addToCart(product.id) // Add product to cart
                    Log.d("ProductScreen", "Added to cart: ${product.productName}")
                }
            )
        }
    }
}

