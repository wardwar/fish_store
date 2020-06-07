package app.by.wildan.efisherystore.decorator

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView

class GridDecorator(
    private val spaceBody: Int
    ) :
    RecyclerView.ItemDecoration() {
    override fun getItemOffsets(
        outRect: Rect, view: View,
        parent: RecyclerView, state: RecyclerView.State
    ) {
        outRect.top = spaceBody
        outRect.left = spaceBody
        outRect.right = spaceBody
        outRect.bottom = spaceBody

    }
}