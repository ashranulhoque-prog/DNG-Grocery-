package com.dng.grocery.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import com.dng.grocery.data.model.*
import com.dng.grocery.data.repository.FirebaseRepository

class GroceryViewModel(
    private val repository: FirebaseRepository = FirebaseRepository()
) : ViewModel() {

    private val _products = MutableStateFlow<List<Product>>(emptyList())
    val products: StateFlow<List<Product>> = _products.asStateFlow()

    private val _categories = MutableStateFlow<List<Category>>(emptyList())
    val categories: StateFlow<List<Category>> = _categories.asStateFlow()

    private val _selectedCategory = MutableStateFlow<String?>(null)
    val selectedCategory: StateFlow<String?> = _selectedCategory.asStateFlow()

    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

    private val _cart = MutableStateFlow<Map<String, CartItem>>(emptyMap())
    val cart: StateFlow<Map<String, CartItem>> = _cart.asStateFlow()

    val cartItemsCount = _cart.map { it.values.sumOf { item -> item.quantity } }
        .stateIn(viewModelScope, SharingStarted.Lazily, 0)

    val cartTotal = _cart.map { it.values.sumOf { item -> item.product.price * item.quantity } }
        .stateIn(viewModelScope, SharingStarted.Lazily, 0)

    val filteredProducts = combine(_products, _selectedCategory, _searchQuery) { prods, cat, query ->
        prods.filter { p ->
            (cat == null || p.category == cat) &&
            (query.isBlank() || p.name.contains(query, ignoreCase = true) || p.description.contains(query, ignoreCase = true))
        }
    }.stateIn(viewModelScope, SharingStarted.Lazily, emptyList())

    init {
        loadInitialData()
    }

    private fun loadInitialData() {
        viewModelScope.launch {
            _categories.value = repository.getCategories()
            _products.value = repository.getProducts()
        }
    }

    fun selectCategory(categoryId: String?) {
        _selectedCategory.value = categoryId
    }

    fun setSearchQuery(query: String) {
        _searchQuery.value = query
    }

    fun addToCart(product: Product) {
        val current = _cart.value.toMutableMap()
        val existing = current[product.id]
        if (existing != null) {
            current[product.id] = existing.copy(quantity = existing.quantity + 1)
        } else {
            current[product.id] = CartItem(product = product, quantity = 1)
        }
        _cart.value = current
    }

    fun removeFromCart(productId: String) {
        val current = _cart.value.toMutableMap()
        current.remove(productId)
        _cart.value = current
    }

    fun updateCartQuantity(productId: String, quantity: Int) {
        if (quantity <= 0) {
            removeFromCart(productId)
            return
        }
        val current = _cart.value.toMutableMap()
        val existing = current[productId]
        if (existing != null) {
            current[productId] = existing.copy(quantity = quantity)
            _cart.value = current
        }
    }

    fun clearCart() {
        _cart.value = emptyMap()
    }
}