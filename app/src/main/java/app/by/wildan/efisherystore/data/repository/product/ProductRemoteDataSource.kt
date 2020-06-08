package app.by.wildan.efisherystore.data.repository.product

import app.by.wildan.efisherystore.Result
import app.by.wildan.mobile.ApiClient
import app.by.wildan.mobile.OptionArea
import app.by.wildan.mobile.OptionSize
import app.by.wildan.mobile.Product
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.HttpException

class ProductRemoteDataSource(
    private val api: ApiClient,
    private val dispatcher: CoroutineDispatcher = Dispatchers.IO
) {

    suspend fun getProduct(): Result<List<Product>> {
        return withContext(dispatcher) {
            try {
                val result = api.fetchStoreItemList()
                Result.Success(result)
            } catch (e: Exception) {
                Result.Error(e)
            }
        }
    }

    suspend fun getArea(): Result<List<OptionArea>> {
        return withContext(dispatcher) {
            try {
                val result = api.fetchArea()
                Result.Success(result)
            } catch (e: HttpException) {
                Result.Error(e)
            }
        }
    }

    suspend fun getSize(): Result<List<OptionSize>> {
        return withContext(dispatcher) {
            try {
                val result = api.fetchSize()
                Result.Success(result)
            } catch (e: HttpException) {
                Result.Error(e)
            }
        }
    }
}