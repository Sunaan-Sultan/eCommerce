package com.project.ecommerce.ui.home

import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.project.api.product.Product
import com.project.ecommerce.ui.category.CategoryGrid
import com.project.ecommerce.ui.product.ProductList
import com.project.service.product.ProductServiceImpl
import androidx.compose.runtime.*
import androidx.compose.ui.graphics.Color
import com.project.ecommerce.ui.cart.CartManager
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

@Composable
fun HomeScreen(
    context: Context,
    onCategoryClick: (String) -> Unit,
    onProductClick: (Product) -> Unit
) {
    val productService = ProductServiceImpl()
    var products by remember { mutableStateOf<List<Product>>(emptyList()) }
    var showAllCategories by remember { mutableStateOf(false) }

    // Load products asynchronously
    LaunchedEffect(Unit) {
        val loadedProducts = withContext(Dispatchers.IO) {
            productService.getProductService(context)
        }
        products = loadedProducts
    }

    Column(modifier = Modifier
        .fillMaxSize()
        .background(if (isSystemInDarkTheme()) Color.Black else Color.White)
        .padding(start = 16.dp, end = 16.dp, top = 16.dp, bottom = 0.dp )
    ) {

        // Header row
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = "Categories",
                style = MaterialTheme.typography.headlineSmall,
                modifier = Modifier.padding(bottom = 16.dp)
            )
            Text(
                text = "See All",
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier
                    .padding(top = 8.dp)
                    .clickable { showAllCategories = !showAllCategories }
            )
        }

        if (products.isEmpty()) {
            Text("No products available.", style = MaterialTheme.typography.bodyLarge)
        } else {
            // Group products by category and calculate the number of products in each category
            val categoryCounts = products.groupBy { it.productCategory }
                .mapValues { it.value.size }

            // Get unique categories and their respective product count
            val uniqueCategories = categoryCounts.keys.toList()
            val displayedCategories = if (showAllCategories) uniqueCategories else uniqueCategories.take(2)

            CategoryGrid(
                categories = displayedCategories,
                categoryCounts = categoryCounts,
                onCategoryClick = onCategoryClick
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "Products",
                style = MaterialTheme.typography.headlineSmall,
                modifier = Modifier.padding(bottom = 16.dp)
            )
            ProductList(
                products = products,
                onLoadMore = {
                    Log.d("HomeScreen", "Load more triggered.")
                },
                onProductClick = onProductClick,
                onAddToCart = { product ->
                    CartManager.addToCart(product.id)
                    Log.d("HomeScreen", "Added to cart: ${product.productName}")

                    Toast.makeText(context, "${product.productName} added to cart", Toast.LENGTH_SHORT).show()
                }
            )
        }
    }
}





