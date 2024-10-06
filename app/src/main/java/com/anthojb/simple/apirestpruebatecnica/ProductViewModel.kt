package com.anthojb.simple.apirestpruebatecnica

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.anthojb.simple.apirestpruebatecnica.model.Product
import com.anthojb.simple.apirestpruebatecnica.repository.ProductRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProductViewModel @Inject constructor(
    private val productRepository: ProductRepository
) : ViewModel() {
 
    private val _products = MutableLiveData<List<Product>>()
    val products: LiveData<List<Product>> = _products
 
    fun getProducts() {
        viewModelScope.launch {
            val fetchedProducts = productRepository.getProducts()
            _products.postValue(fetchedProducts)
        }
    }
}