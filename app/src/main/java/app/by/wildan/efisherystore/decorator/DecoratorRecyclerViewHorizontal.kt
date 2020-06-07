package app.by.wildan.efisherystore.decorator

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView

class DecoratorRecyclerViewHorizontal(
    private val firstStartMargin: Int,
    private val lastEndMargin: Int,
    private val defaultTopMargin: Int,
    private val defaultBottomMargin: Int = defaultTopMargin,
    private val defaultStartMargin: Int = defaultTopMargin,
    private val defaultEndMargin: Int = defaultTopMargin
) :
    RecyclerView.ItemDecoration() {
    override fun getItemOffsets(
        outRect: Rect, view: View,
        parent: RecyclerView, state: RecyclerView.State
    ) {
        with(outRect) {
            val position = parent.getChildAdapterPosition(view)
                when {
                    position == 0 -> {
                        left = firstStartMargin
                        right = defaultEndMargin
                        bottom = defaultBottomMargin
                        top = defaultBottomMargin
                    }
                    state.itemCount > 0 && position == state.itemCount - 1 -> {
                        left = defaultStartMargin
                        right = lastEndMargin
                        bottom = defaultEndMargin
                        top = defaultTopMargin
                    }
                    else -> {
                        left = defaultStartMargin
                        right = defaultEndMargin
                        bottom = defaultBottomMargin
                        top = defaultTopMargin
                    }
                }
        }
    }
}