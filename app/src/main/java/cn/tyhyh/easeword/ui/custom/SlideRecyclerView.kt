package cn.tyhyh.easeword.ui.custom

import android.content.Context
import android.util.AttributeSet
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.view.ViewConfiguration
import androidx.recyclerview.widget.RecyclerView

/**
 * author: tiny
 * created on: 20-3-28 下午5:18
 */
class SlideRecyclerView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : RecyclerView(context, attrs, defStyleAttr), SlideRecyclerViewHelper.Callback {

    companion object {
        private const val TAG = "SlideRecyclerView"
    }

//    private var downX = -1f
//    private var downY = -1f

    private val scaledTouchSlop: Int

    init {
        val config = ViewConfiguration.get(context)
        scaledTouchSlop = config.scaledTouchSlop
    }

    private val viewHelper: SlideRecyclerViewHelper = SlideRecyclerViewHelper(this)

    override fun dispatchTouchEvent(ev: MotionEvent): Boolean {
        return viewHelper.handleDispatchTouchEvent(ev) || super.dispatchTouchEvent(ev)
    }

    override fun onInterceptTouchEvent(event: MotionEvent): Boolean {
        return when (event.actionMasked) {
            MotionEvent.ACTION_DOWN -> {
                viewHelper.handleListDownTouchEvent(event, super.onInterceptTouchEvent(event))
            }
            else -> {
                super.onInterceptTouchEvent(event).also { Log.d(TAG, "super.intercepted = $it, action = ${event.actionMasked}") }
            }
        }
    }

    override fun getTouchedItemView(xPos: Float, yPos: Float): View? {
        return findChildViewUnder(xPos, yPos)
    }

    override fun getAdapterPosition(view: View): Int {
        return getChildAdapterPosition(view)
    }

    override fun getItemViewByAdapterPosition(position: Int, default: View?): View? {
        val viewHolder = findViewHolderForAdapterPosition(position)
        return viewHolder?.itemView ?: default
    }
}