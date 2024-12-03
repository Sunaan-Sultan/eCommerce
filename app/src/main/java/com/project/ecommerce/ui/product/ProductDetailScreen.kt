package com.project.ecommerce.ui.product

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.project.repository.R
import com.project.api.product.Product
import com.project.ecommerce.ui.cart.CartManager

@Composable
fun ProductDetailScreen(product: Product, navController: NavHostController) {
    val context = LocalContext.current
    val resourceId = context.resources.getIdentifier(
        product.imageUrl,
        "drawable",
        context.packageName
    )

    val showDialog = remember { mutableStateOf(false) }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(if (isSystemInDarkTheme()) Color.Black else Color.White)
            .padding(16.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.TopStart),
            verticalArrangement = Arrangement.Top
        ) {
            if (resourceId != 0) {
                Image(
                    painter = painterResource(resourceId),
                    contentDescription = product.productName,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp)
                )
            } else {
                Image(
                    painter = painterResource(R.drawable.football),
                    contentDescription = "Default Image",
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp)
                )
            }
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = product.productName,
                style = MaterialTheme.typography.headlineMedium,
                modifier = Modifier.padding(bottom = 8.dp)
            )
            Text(
                text = product.productDescription,
                style = MaterialTheme.typography.bodyLarge
            )
        }

        Text(
            text = "${product.productPrice} $",
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.primary,
            fontSize = 30.sp,
            modifier = Modifier
                .align(Alignment.BottomStart)
                .padding(start = 8.dp, bottom = 14.dp)
        )
        Spacer(modifier = Modifier.height(86.dp))

        Button(
            onClick = {
                // Add product to cart using CartManager
                CartManager.addToCart(product.id)
                showDialog.value = true // Show confirmation dialog
                Log.d("ProductDetailScreen", "Product added to cart: ${product.productName}")
            },
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFF4CAF50),
                contentColor = Color.White
            ),
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(8.dp)
        ) {
            Icon(
                imageVector = Icons.Filled.ShoppingCart,
                contentDescription = "Add to Cart",
                modifier = Modifier.padding(end = 8.dp)
            )
            Text(text = "Add to Cart")
        }
    }

    if (showDialog.value) {
        AlertDialog(
            onDismissRequest = { showDialog.value = false },
            title = { Text(text = "Product Added") },
            text = { Text(text = "${product.productName} has been added to your cart.") },
            confirmButton = {
                Button(
                    onClick = {
                        showDialog.value = false // Dismiss the dialog
                        navController.navigate("myCart") // Navigate to the My Cart screen
                    }
                ) {
                    Text(text = "OK")
                }
            }
        )
    }
}





