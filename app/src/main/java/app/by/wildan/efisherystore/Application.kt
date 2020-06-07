package app.by.wildan.efisherystore

import android.app.Application
import com.facebook.shimmer.BuildConfig
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.loadKoinModules
import org.koin.core.context.startKoin
import org.koin.core.context.unloadKoinModules
import timber.log.Timber


class Application : Application() {

    override fun onCreate() {
        super.onCreate()

        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }

        startKoin {
            androidContext(this@Application)
            modules(appModule)
        }
    }

    fun resetKoin() {
        unloadKoinModules(appModule)
        loadKoinModules(appModule)
    }

}