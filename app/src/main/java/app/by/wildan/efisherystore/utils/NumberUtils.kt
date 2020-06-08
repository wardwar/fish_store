package app.by.wildan.efisherystore.utils

import java.text.DecimalFormat
import java.text.NumberFormat

fun String.toDecimal() : String {
    return DecimalFormat().format(this.toInt())
}