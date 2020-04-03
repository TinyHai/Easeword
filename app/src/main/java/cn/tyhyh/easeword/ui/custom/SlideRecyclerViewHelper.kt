package cn.tyhyh.easeword.ui.custom

import android.view.MotionEvent
import android.view.View

/**
 * author: tiny
 * created on: 20-3-28 下午5:28
 */
class SlideRecyclerViewHelper(private val callback: Callback) {

    companion object {
        private const val INVALID_POSITION = -1
        private const val TAG = "SlideRecyclerViewHelper"
    }

    private var lastTouchedView: SlideFrameLayout? = null

    private var lastTouchingPosition = INVALID_POSITION

    fun handleListDownTouchEvent(ev: MotionEvent, defaultIntercepted: Boolean): Boolean {
        var isIntercepted = defaultIntercepted

        var touchedItemView = callback.getTouchedItemView(ev.x, ev.y)
        val touchingPosition = if (touchedItemView == null) {
            INVALID_POSITION
        } else callback.getAdapterPosition(touchedItemView)

        if (touchingPosition != lastTouchingPosition && lastTouchedView != null) {
            isIntercepted = lastTouchedView?.closeMenuForce() ?: false
        }

        touchedItemView = callback.getItemViewByAdapterPosition(touchingPosition, touchedItemView)
        if (touchedItemView is SlideFrameLayout) {
            lastTouchedView = touchedItemView
            lastTouchingPosition = touchingPosition
        }

        if (isIntercepted) {
            lastTouchedView = null
            lastTouchingPosition = INVALID_POSITION
        }

        return isIntercepted
    }

    fun handleDispatchTouchEvent(ev: MotionEvent): Boolean {
        return when (ev.actionMasked) {
            MotionEvent.ACTION_POINTER_DOWN -> {
                true
            }
            else -> false
        }
    }

    interface Callback {
        fun getTouchedItemView(xPos: Float, yPos: Float): View?
        fun getAdapterPosition(view: View): Int
        fun getItemViewByAdapterPosition(position: Int, default: View?): View?
    }
}