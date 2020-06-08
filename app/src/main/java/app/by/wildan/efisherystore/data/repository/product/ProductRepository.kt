package app.by.wildan.efisherystore.data.repository.product

import androidx.lifecycle.LiveData
import app.by.wildan.efisherystore.Result
import app.by.wildan.efisherystore.data.entity.OptionArea
import app.by.wildan.efisherystore.data.entity.OptionSize
import app.by.wildan.efisherystore.data.entity.Product

interface ProductRepository {
    suspend fun getProduct() : LiveData<List<Product>>
    suspend fun getSize():  LiveData<List<OptionSize>>
    suspend fun getArea():  LiveData<List<OptionArea>>
    suspend fun addProduct(product: app.by.wildan.mobile.Product) : Result<String>
}