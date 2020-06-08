package app.by.wildan.efisherystore.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import app.by.wildan.efisherystore.utils.toDecimal


@Entity
data class Product(
    @PrimaryKey val uuid: String, val komoditas: String, val area_provinsi: String, val area_kota: String,
    val size: String, val price: String, val tgl_parsed: String, val timestamp: String
)

val Product.priceGram: String
    get() {
        val decimal = this.price.toDecimal()
        return "Rp $decimal / $size gr"
    }

@Entity
data class OptionArea(
    @PrimaryKey val id:String,
    val province: String?,
    val city: String?
)

@Entity
data class OptionSize(
    @PrimaryKey val id:String,
    val size: String?
)