package app.by.wildan.efisherystore.pages

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import app.by.wildan.efisherystore.data.entity.OptionArea
import app.by.wildan.efisherystore.data.entity.OptionSize
import app.by.wildan.efisherystore.data.entity.Product
import app.by.wildan.efisherystore.data.repository.product.ProductRepository
import kotlinx.coroutines.launch

class ProductViewModel(private val productRepository: ProductRepository) : ViewModel() {

    fun updateData() {
        viewModelScope.launch {
            productRepository.getProduct()
            productRepository.getArea()
            productRepository.getSize()
        }
    }

    fun getProduct(continuation: (LiveData<List<Product>>) -> Unit) {
        viewModelScope.launch {
            continuation(productRepository.getProduct())
        }
    }

    fun getArea(continuation: (LiveData<List<OptionArea>>) -> Unit) {
        viewModelScope.launch {
            continuation(productRepository.getArea())
        }
    }

    fun getSize(continuation: (LiveData<List<OptionSize>>) -> Unit) {
        viewModelScope.launch {
            continuation(productRepository.getSize())
        }
    }
}