package app.by.wildan.efisherystore.data.repository.product

import androidx.lifecycle.LiveData
import app.by.wildan.efisherystore.Result
import app.by.wildan.efisherystore.data.entity.OptionArea
import app.by.wildan.efisherystore.data.entity.OptionSize
import app.by.wildan.efisherystore.data.entity.Product


class ProductRepositoryImp(
    private val localDataSource: ProductLocalDataSource,
    private val remoteDataSource: ProductRemoteDataSource
) : ProductRepository {

    override suspend fun getProduct(): LiveData<List<Product>> {
        val data = remoteDataSource.getProduct()
        if (data is Result.Success) {
            val transform = data.data.filter { !it.uuid.isNullOrEmpty() }.map {
                Product(
                    it.uuid ?: "",
                    it.komoditas ?: "",
                    it.area_provinsi ?: "",
                    it.area_kota ?: "",
                    it.size ?: "",
                    it.price ?: "",
                    it.tgl_parsed ?: "",
                    it.timestamp ?: ""
                )
            }
            localDataSource.insertProduct(transform)
        }
        return localDataSource.productList
    }

    override suspend fun getSize(): LiveData<List<OptionSize>> {
        val data = remoteDataSource.getSize()
        if (data is Result.Success) {
            val transform = data.data.filter { !it.size.isNullOrEmpty() }.withIndex().map {
                OptionSize(
                    it.index.toString(),
                    it.value.size
                )
            }
            localDataSource.insertSize(transform)
        }
        return localDataSource.sizeList
    }

    override suspend fun getArea(): LiveData<List<OptionArea>> {
        val data = remoteDataSource.getArea()
        if (data is Result.Success) {
            val transform = data.data.filter { !it.province.isNullOrEmpty() }.withIndex().map {
                OptionArea(
                    it.index.toString(),
                    it.value.province,
                    it.value.city
                )
            }
            localDataSource.insertArea(transform)
        }
        return localDataSource.areaList
    }

    override suspend fun addProduct(product: app.by.wildan.mobile.Product): Result<String> {
        return remoteDataSource.postProduct(product)
    }
}