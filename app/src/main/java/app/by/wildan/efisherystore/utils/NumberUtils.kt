package app.by.wildan.efisherystore.utils

import java.text.DecimalFormat

fun String.toDecimal() : String {
    return try {
        DecimalFormat().format(this.toInt())
    }catch (e: Exception){
        "0"
    }
}