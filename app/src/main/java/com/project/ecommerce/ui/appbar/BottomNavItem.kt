package com.project.ecommerce.ui.appbar

import android.util.Log
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.runtime.Composable
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.project.api.product.Product
import com.project.ecommerce.ui.cart.CheckoutScreen
import com.project.ecommerce.ui.cart.MyCartScreen
import com.project.ecommerce.ui.home.HomeScreen
import com.project.ecommerce.ui.product.ProductDetailScreen
import com.project.ecommerce.ui.product.ProductScreen
import com.project.ecommerce.ui.product.ProductView
import com.project.ecommerce.ui.profile.ProfileScreen
import com.project.service.product.ProductServiceImpl

sealed class BottomNavItem(val route: String, val label: String, val icon: ImageVector) {
    object Home : BottomNavItem("home", "Home", Icons.Filled.Home)
    object Product : BottomNavItem("products", "Product", Icons.Filled.ShoppingCart)
    object Profile : BottomNavItem("profile", "Profile", Icons.Filled.Person)
    object MyCart : BottomNavItem("myCart", "MyCart", Icons.Filled.Person)
}

@Composable
fun Navigation(navController: NavHostController, innerPadding: PaddingValues) {
    val cartItems = remember { mutableStateListOf<Product>() }
    val context = LocalContext.current
    val productService = ProductServiceImpl()
    val products = remember { productService.getProductService(context) } // Fetch products

    NavHost(
        navController = navController,
        startDestination = "home",
        modifier = Modifier.padding(innerPadding)
    ) {
        composable("home") {
            HomeScreen(
                context = LocalContext.current,
                onCategoryClick = { category ->
                    navController.navigate("products/$category")
                },
                onProductClick = { product ->
                    navController.navigate("productDetail/${product.id}")
                }
            )
        }

        composable("products/{category}") { backStackEntry ->
            val category = backStackEntry.arguments?.getString("category") ?: ""
            ProductScreen(navController, category)
        }

        composable("products") {
            ProductView(
                context = LocalContext.current,
                onCategoryClick = { category ->
                    navController.navigate("products/$category")
                },
                onProductClick = { product ->
                    navController.navigate("productDetail/${product.id}")
                }
            )
        }

        composable("productDetail/{productId}") { backStackEntry ->
            val productId = backStackEntry.arguments?.getString("productId")?.toInt() ?: -1
            val context = LocalContext.current
            val productService = ProductServiceImpl()
            val product = productService.getProductService(context).find { it.id == productId }
            product?.let {
                ProductDetailScreen(product = it, navController = navController)
            }
        }

        composable("profile") {
            ProfileScreen()
        }

        composable("myCart") {
            MyCartScreen(
                products = products,
                onCheckoutClick = { selectedProducts, totalPrice ->
                    // Navigate to the CheckoutScreen
                    navController.currentBackStackEntry?.savedStateHandle?.set(
                        "selectedProducts",
                        selectedProducts
                    )
                    navController.currentBackStackEntry?.savedStateHandle?.set("totalPrice", totalPrice)
                    navController.navigate("checkout")
                }
            )
        }

        composable("checkout") {
            val selectedProducts = navController.previousBackStackEntry
                ?.savedStateHandle
                ?.get<List<Pair<Product, Int>>>("selectedProducts") ?: emptyList()

            val totalPrice = navController.previousBackStackEntry
                ?.savedStateHandle
                ?.get<Double>("totalPrice") ?: 0.0

            CheckoutScreen(
                selectedProducts = selectedProducts,
                totalPrice = totalPrice,
                onMakePayment = {
                    Log.d("CheckoutScreen", "Payment made for $${"%.2f".format(totalPrice)}")
                    // Navigate back to home after payment
                    navController.popBackStack("home", inclusive = false)
                }
            )
        }


    }
}

