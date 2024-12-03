package com.project.ecommerce.ui.product

import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.project.api.product.Product
import com.project.ecommerce.ui.cart.CartManager
import com.project.service.product.ProductServiceImpl
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

@Composable
fun ProductView(
    context: Context,
    onCategoryClick: (String) -> Unit,
    onProductClick: (Product) -> Unit
) {
    val productService = ProductServiceImpl()
    var products by remember { mutableStateOf<List<Product>>(emptyList()) }
    var filteredProducts by remember { mutableStateOf<List<Product>>(emptyList()) }
    var searchQuery by remember { mutableStateOf("") }

    // Load products asynchronously
    LaunchedEffect(Unit) {
        val loadedProducts = withContext(Dispatchers.IO) {
            productService.getProductService(context)
        }
        products = loadedProducts
        filteredProducts = loadedProducts // Initially show all products
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(if (isSystemInDarkTheme()) Color.Black else Color.White)
            .padding(start = 16.dp, end = 16.dp, top = 16.dp, bottom = 0.dp)
    ) {
        // Search Bar
        TextField(
            value = searchQuery,
            onValueChange = { query ->
                searchQuery = query
                // Filter products based on the search query
                filteredProducts = if (query.isBlank()) {
                    products
                } else {
                    products.filter {
                        it.productName.contains(query, ignoreCase = true)
                    }
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp),
            placeholder = { Text(text = "Search products...") },
            singleLine = true,
            colors = TextFieldDefaults.colors(
//                backgroundColor = MaterialTheme.colorScheme.surface,
                focusedContainerColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.1f),
                unfocusedContainerColor  = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.1f)
            )
        )

        // Products Header
        Text(
            text = "Products",
            style = MaterialTheme.typography.headlineSmall,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        // Product List
        if (filteredProducts.isEmpty()) {
            Text("No products found.", style = MaterialTheme.typography.bodyLarge)
        } else {
            ProductList(
                products = filteredProducts,
                onLoadMore = {
                    Log.d("ProductView", "Load more triggered.")

                },
                onProductClick = onProductClick,
                onAddToCart = { product ->
                    CartManager.addToCart(product.id)
                    Log.d("ProductView", "Added to cart: ${product.productName}")

                    Toast.makeText(context, "${product.productName} added to cart", Toast.LENGTH_SHORT).show()
                }
            )
        }
    }
}
