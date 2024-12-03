package com.project.ecommerce.ui.cart

import android.util.Log
import androidx.compose.runtime.mutableStateMapOf

object CartManager {
    private val cartItems = mutableStateMapOf<Int, Int>() // Map of Product ID to quantity

    fun addToCart(productId: Int) {
        cartItems[productId] = (cartItems[productId] ?: 0) + 1
        Log.d("CartManager", "Added product $productId. Current quantity: ${cartItems[productId]}")
    }

    // Remove a product from the cart, decrease quantity or remove it if the quantity is 1
    fun removeFromCart(productId: Int) {
        val currentQuantity = cartItems[productId] ?: return
        if (currentQuantity > 1) {
            cartItems[productId] = currentQuantity - 1
        } else {
            cartItems.remove(productId)
        }
        Log.d("CartManager", "Removed product $productId. Current quantity: ${cartItems[productId]}")
    }

    fun getCartItems(): Map<Int, Int> {
        return cartItems.toMap()
    }

}
