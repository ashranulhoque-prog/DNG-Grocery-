package com.dng.grocery.data.repository

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import kotlinx.coroutines.tasks.await
import com.dng.grocery.data.model.*

class FirebaseRepository(
    private val firestore: FirebaseFirestore = FirebaseFirestore.getInstance(),
    private val auth: FirebaseAuth = FirebaseAuth.getInstance()
) {
    // Current Authenticated User UID
    val currentUserId: String?
        get() = auth.currentUser?.uid

    // 1. Fetch Categories
    suspend fun getCategories(): List<Category> {
        return try {
            val snapshot = firestore.collection("categories").get().await()
            snapshot.toObjects(Category::class.java)
        } catch (e: Exception) {
            emptyList()
        }
    }

    // 2. Fetch All Products
    suspend fun getProducts(): List<Product> {
        return try {
            val snapshot = firestore.collection("products").get().await()
            snapshot.toObjects(Product::class.java)
        } catch (e: Exception) {
            emptyList()
        }
    }

    // 3. Create Customer Order (Firebase-ready)
    suspend fun placeOrder(order: Order): Result<String> {
        return try {
            val docRef = firestore.collection("orders").document(order.id)
            docRef.set(order).await()
            Result.success(order.id)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    // 4. Admin: Update Order Status
    suspend fun updateOrderStatus(orderId: String, newStatus: String): Result<Unit> {
        return try {
            firestore.collection("orders")
                .document(orderId)
                .update("orderStatus", newStatus)
                .await()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    // 5. Admin: Save or Update Product
    suspend fun saveProduct(product: Product): Result<Unit> {
        return try {
            firestore.collection("products")
                .document(product.id)
                .set(product)
                .await()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    // 6. Admin: Delete Product
    suspend fun deleteProduct(productId: String): Result<Unit> {
        return try {
            firestore.collection("products")
                .document(productId)
                .delete()
                .await()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
