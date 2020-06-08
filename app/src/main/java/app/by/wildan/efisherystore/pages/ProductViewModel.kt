package app.by.wildan.efisherystore.pages

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import app.by.wildan.efisherystore.Event
import app.by.wildan.efisherystore.Result
import app.by.wildan.efisherystore.data.entity.OptionArea
import app.by.wildan.efisherystore.data.entity.OptionSize
import app.by.wildan.efisherystore.data.entity.Product
import app.by.wildan.efisherystore.data.repository.product.ProductRepository
import kotlinx.coroutines.launch

class ProductViewModel(private val productRepository: ProductRepository) : ViewModel() {

    private val _eventPostProduct = MutableLiveData<Event<String>>()
     val eventPostProduct :LiveData<Event<String>> = _eventPostProduct

    private val _loadingState = MutableLiveData<Event<Boolean>>()
     val loadingState :LiveData<Event<Boolean>> = _loadingState

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

    fun postProduct(payload : app.by.wildan.mobile.Product) {
        viewModelScope.launch {
            _loadingState.value = Event(true)
            val result = productRepository.addProduct(payload)
            _loadingState.value = Event(false)

            when(result){
                is Result.Success ->{
                    println(result.data)
                    _eventPostProduct.value = Event("success")
                }
                is Result.Error -> {
                    println("error")
                    _eventPostProduct.value = Event("error")

                }
            }
        }
    }
}