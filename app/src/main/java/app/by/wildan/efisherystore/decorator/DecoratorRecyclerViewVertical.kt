import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView

class DecoratorRecyclerViewVertical(
    private val firstTopMargin: Int,
    private val lastBottomMargin: Int,
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
                    left = defaultStartMargin
                    right = defaultEndMargin
                    bottom = defaultBottomMargin
                    top = firstTopMargin
                }
                state.itemCount > 0 && position == state.itemCount - 1 -> {
                    left = defaultStartMargin
                    right = defaultEndMargin
                    bottom = lastBottomMargin
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