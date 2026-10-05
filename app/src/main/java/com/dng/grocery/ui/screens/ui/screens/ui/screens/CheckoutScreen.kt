package com.dng.grocery.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.dng.grocery.viewmodel.GroceryViewModel

@Composable
fun CheckoutScreen(
    viewModel: GroceryViewModel,
    onOrderPlaced: (String) -> Unit
) {
    val total by viewModel.cartTotal.collectAsState()

    var name by remember { mutableStateOf("") }
    var phone by remember { mutableStateOf("") }
    var address by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(
            text = "Checkout",
            style = MaterialTheme.typography.headlineMedium
        )

        Spacer(modifier = Modifier.height(20.dp))

        OutlinedTextField(
            value = name,
            onValueChange = { name = it },
            label = { Text("Name") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(12.dp))

        OutlinedTextField(
            value = phone,
            onValueChange = { phone = it },
            label = { Text("Phone Number") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(12.dp))

        OutlinedTextField(
            value = address,
            onValueChange = { address = it },
            label = { Text("Delivery Address") },
            modifier = Modifier.fillMaxWidth(),
            minLines = 3
        )

        Spacer(modifier = Modifier.height(20.dp))

        Text(
            text = "Total: ₹$total",
            style = MaterialTheme.typography.titleLarge
        )

        Spacer(modifier = Modifier.height(20.dp))

        Button(
            onClick = {
                if (name.isNotBlank() &&
                    phone.isNotBlank() &&
                    address.isNotBlank()
                ) {
                    viewModel.placeOrder(
                        name = name,
                        phone = phone,
                        address = address
                    ) { orderId ->
                        onOrderPlaced(orderId)
                    }
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Place Order")
        }
    }
}
