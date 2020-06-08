package app.by.wildan.efisherystore.decorator

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ItemDecoration

class GridDecorator(private val spaceBody: Int, private val gridSize: Int) : ItemDecoration() {
    private var mNeedLeftSpacing = false
    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        val frameWidth =
            ((parent.width - spaceBody.toFloat() * (gridSize - 1)) / gridSize).toInt()
        val padding = parent.width / gridSize - frameWidth
        val itemPosition =
            (view.layoutParams as RecyclerView.LayoutParams).viewAdapterPosition
        if (itemPosition < gridSize) {
            outRect.top = 0
        } else {
            outRect.top = spaceBody
        }
        if (itemPosition % gridSize == 0) {
            outRect.left = 0
            outRect.right = padding
            mNeedLeftSpacing = true
        } else if ((itemPosition + 1) % gridSize == 0) {
            mNeedLeftSpacing = false
            outRect.right = 0
            outRect.left = padding
        } else if (mNeedLeftSpacing) {
            mNeedLeftSpacing = false
            outRect.left = spaceBody - padding
            if ((itemPosition + 2) % gridSize == 0) {
                outRect.right = spaceBody - padding
            } else {
                outRect.right = spaceBody / 2
            }
        } else if ((itemPosition + 2) % gridSize == 0) {
            mNeedLeftSpacing = false
            outRect.left = spaceBody / 2
            outRect.right = spaceBody - padding
        }
        else {
            mNeedLeftSpacing = false
            outRect.left = spaceBody / 2
            outRect.right = spaceBody / 2
        }
        outRect.bottom = 0
    }

}