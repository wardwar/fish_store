package app.by.wildan.efisherystore.pages

import app.by.wildan.efisherystore.utils.toDecimal

data class Product(val komuditi: String?, val price: String?, val size: String?)

val Product.priceGram: String
    get() {
        val decimal = this.price?.toDecimal()
        return "Rp $decimal / $size gr"
    }