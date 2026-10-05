package com.dng.grocery.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun OrderConfirmationScreen(
    orderId: String,
    onContinueShopping: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "Order Confirmed!",
            style = MaterialTheme.typography.headlineMedium
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text("Your order has been placed successfully.")

        Spacer(modifier = Modifier.height(8.dp))

        Text("Order ID: $orderId")

        Spacer(modifier = Modifier.height(24.dp))

        Button(
            onClick = onContinueShopping
        ) {
            Text("Continue Shopping")
        }
    }
}
