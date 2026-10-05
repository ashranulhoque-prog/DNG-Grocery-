package com.dng.grocery

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.navigation.compose.*
import com.dng.grocery.ui.theme.DngGroceryTheme
import com.dng.grocery.ui.screens.*
import com.dng.grocery.ui.components.DngBottomNavigation
import com.dng.grocery.viewmodel.GroceryViewModel

class MainActivity : ComponentActivity() {
    private val groceryViewModel: GroceryViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            DngGroceryTheme {
                val navController = rememberNavController()
                val currentBackStack by navController.currentBackStackEntryAsState()
                val currentRoute = currentBackStack?.destination?.route ?: "home"

                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                    bottomBar = {
                        DngBottomNavigation(
                            currentRoute = currentRoute,
                            onNavigate = { route ->
                                navController.navigate(route) {
                                    popUpTo("home") { saveState = true }
                                    launchSingleTop = true
                                    restoreState = true
                                }
                            },
                            cartBadgeCount = groceryViewModel.cartItemsCount.collectAsState().value
                        )
                    }
                ) { innerPadding ->
                    NavHost(
                        navController = navController,
                        startDestination = "home",
                        modifier = Modifier.padding(innerPadding)
                    ) {
                        composable("home") {
                            HomeScreen(viewModel = groceryViewModel, onNavigateToProduct = { id ->
                                navController.navigate("product/$id")
                            })
                        }
                        composable("categories") {
                            CategoriesScreen(viewModel = groceryViewModel)
                        }
                        composable("cart") {
                            CartScreen(viewModel = groceryViewModel, onCheckout = {
                                navController.navigate("checkout")
                            })
                        }
                        composable("checkout") {
                            CheckoutScreen(viewModel = groceryViewModel, onOrderPlaced = { orderId ->
                                navController.navigate("order_confirmation/$orderId")
                            })
                        }
                        composable("orders") {
                            MyOrdersScreen(viewModel = groceryViewModel)
                        }
                        composable("admin") {
                            AdminDashboardScreen(viewModel = groceryViewModel)
                        }
                    }
                }
            }
        }
    }
}