package com.project.ecommerce.ui.cart

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.ui.platform.LocalContext
import com.project.api.product.Product


@Composable
fun MyCartScreen(
    products: List<Product>,
    onCheckoutClick: (List<Pair<Product, Int>>, Double) -> Unit
) {
    val cartItems = CartManager.getCartItems()
    var checkedItems by remember { mutableStateOf<Map<Int, Boolean>>(emptyMap()) }
    var totalPrice by remember { mutableStateOf(0.0) }
    val context = LocalContext.current

    // Recalculate total price when the checked items or cart items change
    LaunchedEffect(cartItems, checkedItems) {
        totalPrice = cartItems.entries.sumOf { (productId, quantity) ->
            val product = products.find { it.id == productId }
            if (checkedItems[productId] == true && product != null) {
                product.productPrice * quantity // Add price for checked items
            } else {
                0.0
            }
        }
    }

    Column(
        modifier = Modifier
            .background(if (isSystemInDarkTheme()) Color.Black else Color.White)
            .fillMaxSize()
            .padding(16.dp)
    ) {
        LazyColumn(modifier = Modifier.weight(1f)) {
            items(cartItems.entries.toList()) { (productId, quantity) ->
                val product = products.find { it.id == productId }
                product?.let {
                    val isChecked = checkedItems[productId] ?: false
                    CartItem(
                        product = it,
                        quantity = quantity,
                        onIncrease = {
                            CartManager.addToCart(productId)
                        },
                        onDecrease = {
                            CartManager.removeFromCart(productId)
                        },
                        onCheckedChange = { checked ->
                            checkedItems = checkedItems.toMutableMap().apply {
                                put(productId, checked)
                            }
                        },
                        isChecked = isChecked
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Total: $${"%.2f".format(totalPrice)}",
                style = MaterialTheme.typography.bodyLarge
            )

            Button(
                onClick = {
                    if (totalPrice == 0.0 || checkedItems.isEmpty()) {
                        Toast.makeText(context, "Cart is empty. Please add items to the cart.", Toast.LENGTH_SHORT).show()
                    } else {
                        val selectedProducts = cartItems.filter { (productId, _) ->
                            checkedItems[productId] == true
                        }.mapNotNull { (productId, quantity) ->
                            products.find { it.id == productId }?.let { it to quantity }
                        }
                        onCheckoutClick(selectedProducts, totalPrice) // Proceed to checkout
                    }
                },
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    contentColor = Color.White
                )
            ) {
                Text("Checkout")
            }
        }
    }
}







