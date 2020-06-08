package app.by.wildan.efisherystore.data.repository.product

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import app.by.wildan.efisherystore.data.entity.OptionArea
import app.by.wildan.efisherystore.data.entity.OptionSize
import app.by.wildan.efisherystore.data.entity.Product

@Dao
interface ProductDao {
    @Query("SELECT * from product")
    fun getProduct(): LiveData<List<Product>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertProduct(product: Product)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertProduct(product: List<Product>)

    @Query("SELECT * from optionarea ORDER BY province ASC")
    fun getArea(): LiveData<List<OptionArea>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertArea(area: OptionArea)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertArea(areas: List<OptionArea>)


    @Query("SELECT * from optionsize ORDER BY size ASC")
    fun getSize(): LiveData<List<OptionSize>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSize(size: OptionSize)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSize(sizes: List<OptionSize>)

}