package app.by.wildan.efisherystore

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import io.ktor.client.HttpClient
import io.ktor.client.engine.okhttp.OkHttp
import io.ktor.client.engine.okhttp.OkHttpConfig
import io.ktor.client.engine.okhttp.OkHttpEngine
import kotlinx.coroutines.*
import okhttp3.logging.HttpLoggingInterceptor
import timber.log.Timber
import kotlin.coroutines.CoroutineContext

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

    }

}