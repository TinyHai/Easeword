package cn.tyhyh.easeword.ui.itemdecoration

import android.graphics.Canvas
import android.graphics.Rect
import android.graphics.drawable.Drawable
import android.widget.LinearLayout
import androidx.core.graphics.withSave
import androidx.recyclerview.widget.RecyclerView
import kotlin.math.roundToInt

/**
 * author: tiny
 * created on: 20-3-27 下午3:12
 */
class ItemDivider(private val orientation: Int) : RecyclerView.ItemDecoration() {

    var divider: Drawable? = null

    private val bounds = Rect()

    override fun onDraw(c: Canvas, parent: RecyclerView) {
        if (parent.layoutManager == null || divider == null) {
            return
        }
        c.withSave {
            if (orientation == LinearLayout.VERTICAL) {
                drawVertical(c, parent)
            } else {
                drawHorizontal(c, parent)
            }
        }
    }

    private fun drawVertical(c: Canvas, parent: RecyclerView) {
        var left = 0
        var right = 0
        var top = 0
        var botton = 0

        if (parent.clipToPadding) {
            left = parent.paddingLeft
            right = parent.width - parent.paddingRight
            c.clipRect(left, parent.paddingTop, right, parent.height - parent.paddingBottom)
        } else {
            left = 0
            right = parent.width
        }

        for (i in 0 until parent.childCount) {
            val child = parent.getChildAt(i)
            parent.getDecoratedBoundsWithMargins(child, bounds)
            botton = bounds.bottom + child.translationY.roundToInt()
            top = botton - divider!!.intrinsicHeight
            divider?.let {
                it.setBounds(left, top, right, botton)
                it.draw(c)
            }
        }
    }

    private fun drawHorizontal(c: Canvas, parent: RecyclerView) {
        var left = 0
        var right = 0
        var top = 0
        var bottom = 0

        if (parent.clipToPadding) {
            top = parent.paddingTop
            bottom = parent.height - parent.paddingBottom
            c.clipRect(parent.paddingLeft, top, parent.width - parent.paddingRight, bottom)
        }

        for (i in 0 until parent.childCount - 1) {
            val child = parent.getChildAt(i)
            parent.getDecoratedBoundsWithMargins(child, bounds)
            right = bounds.right + child.translationX.roundToInt()
            left = right - divider!!.intrinsicWidth
            divider?.let {
                it.setBounds(left, top, right, bottom)
                it.draw(c)
            }
        }
    }

    override fun getItemOffsets(outRect: Rect, itemPosition: Int, parent: RecyclerView) {
        divider?.let {
            if (orientation == LinearLayout.VERTICAL) {
                outRect.set(0, 0, 0, it.intrinsicHeight)
            } else {
                val layoutManager = parent.layoutManager
                val lastPosition = (layoutManager?.itemCount ?: 0) - 1
                if (itemPosition != lastPosition) {
                    outRect.set(0, 0, it.intrinsicWidth, 0)
                } else {
                    outRect.set(0, 0, 0, 0)
                }
            }
        } ?: outRect.set(0, 0, 0, 0)
    }
}