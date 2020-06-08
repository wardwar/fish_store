package app.by.wildan.efisherystore.data

import android.content.Context
import app.by.wildan.efisherystore.data.local.ProductDatabase
import app.by.wildan.efisherystore.data.repository.product.ProductDao
import app.by.wildan.mobile.ApiClient
import io.ktor.client.HttpClient
import io.ktor.client.engine.okhttp.OkHttp
import timber.log.Timber

fun getService(): ApiClient {
    return ApiClient(client.engine)
}

fun getProductDao(productDatabase: ProductDatabase): ProductDao{
    return productDatabase.productDao()
}
val client = HttpClient(OkHttp) {
    engine {
        config { // this: OkHttpClient.Builder ->
            followRedirects(true)
        }

    }
}