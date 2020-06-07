package app.by.wildan.mobile
import com.squareup.sqldelight.db.SqlDriver
import com.squareup.sqldelight.android.AndroidSqliteDriver

actual fun platformName():String {
    return "Android"
}

actual val driver: SqlDriver = AndroidSqliteDriver(Database.Schema, context, "test.db")
