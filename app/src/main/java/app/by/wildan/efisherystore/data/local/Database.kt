package app.by.wildan.efisherystore.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import app.by.wildan.efisherystore.data.entity.OptionArea
import app.by.wildan.efisherystore.data.entity.OptionSize
import app.by.wildan.efisherystore.data.entity.Product
import app.by.wildan.efisherystore.data.repository.product.ProductDao

@Database(
    entities = [Product::class, OptionSize::class, OptionArea::class],
    version = 1,
    exportSchema = false
)
abstract class ProductDatabase : RoomDatabase() {

    abstract fun productDao(): ProductDao

    companion object {
        // Singleton prevents multiple instances of database opening at the
        // same time.
        @Volatile
        private var INSTANCE: ProductDatabase? = null

        fun getDatabase(context: Context): ProductDatabase {
            val tempInstance = INSTANCE
            if (tempInstance != null) {
                return tempInstance
            }
            synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    ProductDatabase::class.java,
                    "product_database"
                ).build()
                INSTANCE = instance
                return instance
            }
        }
    }
}