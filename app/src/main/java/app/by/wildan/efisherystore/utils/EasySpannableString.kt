package app.by.wildan.efisherystore.utils

import android.content.Context
import android.text.SpannableString
import android.text.SpannableStringBuilder
import android.text.Spanned
import android.text.style.ForegroundColorSpan
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import app.by.wildan.efisherystore.R

class EasySpannableString private constructor(
    val context: Context,
    val stringBuilder: SpannableStringBuilder
) {
    fun get() = stringBuilder

    data class Builder(
        private var context: Context
    ) {
        private val stringBuilder = SpannableStringBuilder()
        private val flag = Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
        private val mediumTypeface = ResourcesCompat.getFont(context, R.font.poppins_medium)
        private val boldTypeface = ResourcesCompat.getFont(context, R.font.poppins_bold)
        private val regularTypeface = ResourcesCompat.getFont(context, R.font.poppins)

        private fun appendString(
            string: SpannableString,
            weight: SpannableStringWeight = SpannableStringWeight.REGULAR,
            color: Int? = null
        ) = apply {
            if (color != null) {
                string.setSpan(
                    ForegroundColorSpan(ContextCompat.getColor(context, color)),
                    0,
                    string.length,
                    flag
                )
            }
            when (weight) {
                SpannableStringWeight.BOLD -> {
                    boldTypeface?.let { tf ->
                        string.setSpan(
                            CustomTypefaceSpan("poppins", tf),
                            0,
                            string.length,
                            flag
                        )
                    }
                }
                SpannableStringWeight.MEDIUM -> {
                    mediumTypeface?.let { tf ->
                        string.setSpan(
                            CustomTypefaceSpan("poppins", tf),
                            0,
                            string.length,
                            flag
                        )
                    }
                }
                SpannableStringWeight.REGULAR -> {
                    regularTypeface?.let { tf ->
                        string.setSpan(
                            CustomTypefaceSpan("poppins", tf),
                            0,
                            string.length,
                            flag
                        )
                    }
                }
            }

            stringBuilder.append(string)
        }



        fun appendStringDefault(  stringResource: Int, color: Int? = null) = apply{
            val string = SpannableString(context.getString(stringResource))
            appendString(string,SpannableStringWeight.REGULAR,color)
        }

        fun appendStringMedium(  stringResource: Int, color: Int? = null) = apply{
            val string = SpannableString(context.getString(stringResource))
            appendString(string,SpannableStringWeight.MEDIUM,color)
        }

        fun appendStringBold(  stringResource: Int, color: Int? = null) = apply{
            val string = SpannableString(context.getString(stringResource))
            appendString(string,SpannableStringWeight.BOLD,color)
        }

        fun appendStringDefault(  string: String, color: Int? = null) = apply{
            appendString(SpannableString(string),SpannableStringWeight.REGULAR,color)
        }

        fun appendStringMedium( string: String, color: Int? = null) = apply{
            appendString(SpannableString(string),SpannableStringWeight.MEDIUM,color)
        }

        fun appendStringBold( string: String, color: Int? = null) = apply{
            appendString(SpannableString(string),SpannableStringWeight.BOLD,color)
        }


        fun appendSpace() = apply {
            stringBuilder.append(" ")
        }

        fun appendNewLine() = apply {
            stringBuilder.append(System.getProperty("line.separator"))
        }

        fun build() = (EasySpannableString(context, stringBuilder))
    }
}

enum class SpannableStringWeight {
    BOLD, MEDIUM, REGULAR
}