package app.by.wildan.mobile

import platform.UIKit.UIDevice
import platform.darwin.dispatch_async
import platform.darwin.dispatch_get_main_queue
import platform.darwin.dispatch_queue_t
import kotlin.coroutines.CoroutineContext
//import kotlinx.coroutines.CoroutineDispatcher



actual fun platformName(): String{
    return UIDevice.currentDevice.systemName() + " " + UIDevice.currentDevice.systemVersion
}

val driver: SqlDriver = NativeSqliteDriver(Database.Schema, "efisheryStore.db")
