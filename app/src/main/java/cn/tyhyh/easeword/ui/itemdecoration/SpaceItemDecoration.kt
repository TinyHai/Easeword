package cn.tyhyh.easeword.ui.itemdecoration

import android.graphics.Rect
import android.view.View
import android.widget.LinearLayout
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import cn.tyhyh.easeword.util.DisplayUtil

class SpaceItemDecoration(
    private val dpVertical: Int,
    private val dpHorizontal: Int = 0,
    private val orientation: Int = LinearLayout.VERTICAL
) : RecyclerView.ItemDecoration() {

    companion object {
        private const val TAG = "SpaceItemDecoration"
    }

    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        when (val layoutManager = parent.layoutManager) {
            is GridLayoutManager -> handleGridLayoutManager(layoutManager, view, outRect)

            is LinearLayoutManager -> handleLinearLayoutManager(layoutManager, view, outRect)
        }
    }

    private fun handleLinearLayoutManager(
        layoutManager: LinearLayoutManager,
        view: View,
        outRect: Rect
    ) {
        if (orientation == LinearLayout.VERTICAL) {
            handleVertical(outRect, layoutManager, view)
        } else {
            handleHorizontal(outRect, layoutManager, view)
        }
    }

    private fun handleVertical(
        outRect: Rect,
        layoutManager: LinearLayoutManager,
        view: View
    ) {
        val horizontalPx = DisplayUtil.dp2px(dpHorizontal)
        val verticalPx = DisplayUtil.dp2px(dpVertical) / 2

        outRect.apply {
            left = horizontalPx
            right = horizontalPx
        }

        val position = layoutManager.getPosition(view)
        val firstPosition = 0
        val lastPosition = layoutManager.itemCount - 1

        when (position) {
            firstPosition -> {
                outRect.top = verticalPx
                outRect.bottom = verticalPx
            }
            lastPosition -> {
                outRect.top = verticalPx
                outRect.bottom = verticalPx
            }
            else -> {
                outRect.top = verticalPx
                outRect.bottom = verticalPx
            }
        }
    }

    private fun handleHorizontal(
        outRect: Rect,
        layoutManager: LinearLayoutManager,
        view: View
    ) {
        val verticalPx = DisplayUtil.dp2px(dpVertical)
        val horizontalPx = DisplayUtil.dp2px(dpHorizontal) / 2

        outRect.apply {
            top = verticalPx
            bottom = verticalPx
        }

        val position = layoutManager.getPosition(view)
        val firstPosition = 0
        val lastPosition = layoutManager.itemCount - 1

        when (position) {
            firstPosition -> {
                outRect.left = horizontalPx * 2
                outRect.right = horizontalPx
            }
            lastPosition -> {
                outRect.left = horizontalPx
                outRect.right = horizontalPx * 2
            }
            else -> {
                outRect.left = horizontalPx
                outRect.right = horizontalPx
            }
        }
    }

    private fun handleGridLayoutManager(
        layoutManager: GridLayoutManager,
        view: View,
        outRect: Rect
    ) {
        val position = layoutManager.getPosition(view)
        val spanSizeLookup = layoutManager.spanSizeLookup
        val spanSize = spanSizeLookup.getSpanSize(position)

        val spanCount = layoutManager.spanCount

        val spanIndex = spanSizeLookup.getSpanIndex(position, spanCount)

        if (spanSize == spanCount) {
            outRect.apply {
                left = 0
                right = 0
                if (position == 1) {
                    top = 0
                }
            }
        } else {
            val halfHorizontalSpace = DisplayUtil.dp2px(dpHorizontal) / 2

            when (spanIndex) {
                0 -> outRect.right = halfHorizontalSpace

                1 -> outRect.apply {
                    left = halfHorizontalSpace
                    right = halfHorizontalSpace
                }

                2 -> outRect.apply {
                    left = halfHorizontalSpace
                    right = halfHorizontalSpace
                }

                3 -> outRect.left = halfHorizontalSpace
            }
        }
    }
}