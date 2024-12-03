package com.project.ecommerce

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.project.ecommerce.ui.appbar.AppBar
import com.project.ecommerce.ui.appbar.BottomNavigationBar
import com.project.ecommerce.ui.appbar.Navigation
import com.project.ecommerce.ui.theme.ECommerceTheme
import kotlinx.coroutines.runBlocking

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val mockLoader = MockLoader(this)
        runBlocking {
            mockLoader.init()
        }

        setContent {
            ECommerceTheme {
                val navController = rememberNavController()

                val currentRoute = navController.currentBackStackEntryAsState().value?.destination?.route

                // Debugging to confirm the current route
                LaunchedEffect(currentRoute) {
                    println("Current Route: $currentRoute")
                }

                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                    topBar = {
                        AppBar(
                            title =
                            if (currentRoute == "productDetail/{productId}") "Product Detail"
                            else if (currentRoute == "profile") "Profile"
                            else if (currentRoute == "myCart") "My Cart"
                            else "Snap Mart",
                            showCartIcon = currentRoute != "productDetail/{productId}",
                            navController
                        )
                    },
                    bottomBar = {
                        if (currentRoute != "productDetail/{productId}" && currentRoute != "myCart" && currentRoute != "checkout") {
                            BottomNavigationBar(navController)
                        }
                    }
                ) { innerPadding ->
                    Navigation(
                        navController = navController,
                        innerPadding = innerPadding
                    )
                }
            }
        }
    }
}



