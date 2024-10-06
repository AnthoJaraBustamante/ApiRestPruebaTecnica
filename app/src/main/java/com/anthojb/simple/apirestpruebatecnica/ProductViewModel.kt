package com.anthojb.simple.apirestpruebatecnica

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.anthojb.simple.apirestpruebatecnica.repository.ProductRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProductViewModel @Inject constructor(
    private  val productRepositoryImp: ProductRepository
):ViewModel() {
    fun getProducts() {
        viewModelScope.launch {
            val products = productRepositoryImp.getProducts()
            Log.d("ProductViewModel", products.toString())
        }
    }
}