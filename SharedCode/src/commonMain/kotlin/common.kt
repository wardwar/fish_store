package app.by.wildan.mobile

import io.ktor.client.HttpClient
import kotlinx.coroutines.CoroutineDispatcher
import com.squareup.sqldelight.db.SqlDriver


expect fun platformName():String
expect val driver : SqlDriver

fun createApplicationScreenMessage() : String {
    return "Kotlin code base on ${platformName()}"
}