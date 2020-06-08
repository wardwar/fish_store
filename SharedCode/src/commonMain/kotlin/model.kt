package app.by.wildan.mobile

import kotlinx.serialization.Serializable

@Serializable
data class Product(
    val uuid: String?, val komoditas: String?, val area_provinsi: String?, val area_kota: String?,
    val size: String?, val price: String?, val tgl_parsed: String?, val timestamp: String?
)

@Serializable
data class OptionArea(
    val province: String?,
    val city: String?
)

@Serializable
data class OptionSize(
    val size: String?
)