package app.by.wildan.mobile

import kotlinx.serialization.Serializable

@Serializable
data class StoreItem(
    val uuid: String?, val komoditas: String?, val area_provinsi: String?, val area_kota: String?,
    val size: String?, val price: String?, val tgl_parsed: String?, val timestamp: String?
)