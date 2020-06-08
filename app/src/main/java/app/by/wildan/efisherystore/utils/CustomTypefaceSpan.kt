package app.by.wildan.efisherystore.utils

import android.graphics.Paint
import android.graphics.Typeface
import android.text.TextPaint
import android.text.style.TypefaceSpan

/**
 * Created by Satya Dwi Anggiawan on 25/07/2019.
 */
class CustomTypefaceSpan(family: String?, private val type: Typeface) : TypefaceSpan(family) {

    override fun updateDrawState(ds: TextPaint) {
        applyCustomTypeFace(ds, type)
    }

    override fun updateMeasureState(paint: TextPaint) {
        applyCustomTypeFace(paint, type)
    }

    private fun applyCustomTypeFace(paint: Paint, tf: Typeface) {
        val oldStyle: Int
        val old = paint.typeface
        if (old == null) {
            oldStyle = 0
        } else {
            oldStyle = old.style
        }

        val fake = oldStyle and tf.style.inv()
        if (fake and Typeface.BOLD != 0) {
            paint.isFakeBoldText = true
        }

        if (fake and Typeface.ITALIC != 0) {
            paint.textSkewX = -0.25f
        }

        paint.typeface = tf
    }
}