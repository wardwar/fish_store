package app.by.wildan.mobile

import io.ktor.client.HttpClient
import io.ktor.client.engine.HttpClientEngine
import io.ktor.client.features.json.JsonFeature
import io.ktor.client.features.json.serializer.KotlinxSerializer
import io.ktor.client.request.forms.FormDataContent
import io.ktor.client.request.get
import io.ktor.client.request.post
import io.ktor.client.request.url
import io.ktor.http.ContentType
import io.ktor.http.Parameters
import io.ktor.http.contentType
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonConfiguration
import kotlin.random.Random


class ApiClient(private val engine: HttpClientEngine) {

    private val client by lazy {
        HttpClient(engine) {
            install(JsonFeature) {
                serializer = KotlinxSerializer(Json(JsonConfiguration.Stable.copy(prettyPrint = true,ignoreUnknownKeys = true)))
            }
        }
    }

    suspend fun fetchStoreItemList(): List<Product> {
        return client.get {
            url("$baseUrl/list")
        }
    }

    suspend fun fetchArea(): List<OptionArea> {
        return client.get {
            url("$baseUrl/option_area")
        }
    }

    suspend fun fetchSize(): List<OptionSize> {
        return client.get {
            url("$baseUrl/option_size")
        }
    }

    suspend fun postProduct(product:Product) :String{
        return client.post{
            println("Sedang Request Data")
            url("$baseUrl/list")
            contentType(ContentType.Application.Json)
            body = listOf(product)
        }
    }

    companion object {
        private const val baseUrl =
            "https://stein.efishery.com/v1/storages/5e1edf521073e315924ceab4"
    }
}