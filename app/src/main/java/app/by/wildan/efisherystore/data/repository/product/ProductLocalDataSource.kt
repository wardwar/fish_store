package app.by.wildan.efisherystore.data.repository.product

import androidx.lifecycle.LiveData
import app.by.wildan.efisherystore.data.entity.OptionArea
import app.by.wildan.efisherystore.data.entity.OptionSize
import app.by.wildan.efisherystore.data.entity.Product

class ProductLocalDataSource(private val productDao: ProductDao) {

    val productList: LiveData<List<Product>> = productDao.getProduct()
    val sizeList: LiveData<List<OptionSize>> = productDao.getSize()
    val areaList: LiveData<List<OptionArea>> = productDao.getArea()

    suspend fun insertProduct(product: Product) {
        productDao.insertProduct(product)
    }

    suspend fun insertProduct(product: List<Product>) {
        productDao.insertProduct(product)
    }

    suspend fun insertSize(size: OptionSize) {
        productDao.insertSize(size)
    }

    suspend fun insertSize(sizes: List<OptionSize>) {
        productDao.insertSize(sizes)
    }


    suspend fun insertArea(area: OptionArea) {
        productDao.insertArea(area)
    }

    suspend fun insertArea(area: List<OptionArea>) {
        productDao.insertArea(area)
    }
}