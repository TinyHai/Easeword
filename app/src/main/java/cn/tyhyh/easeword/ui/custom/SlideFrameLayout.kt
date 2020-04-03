package cn.tyhyh.easeword.ui.custom

import android.content.Context
import android.util.AttributeSet
import android.util.Log
import android.view.*
import android.widget.FrameLayout
import androidx.core.view.children
import kotlin.math.absoluteValue

/**
 * author: tiny
 * created on: 20-3-25 上午11:21
 * Inspired by SwipeMenu @see https://github.com/TUBB/SwipeMenu
 */
class SlideFrameLayout @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr), View.OnTouchListener {

    companion object {
        private const val TAG = "SlideFragment"
    }

    private lateinit var slideHelper: SlideMenuHelper

    private var downX: Float = -1f

    private var downY: Float = -1f

    private var lastX: Float = -1f

    private var lastY: Float = -1f

    private val velocityTracker = VelocityTracker.obtain()

    private val scaledTouchSlop: Int

    private val scaledMinimumVelocity: Int

    private val scaledMaximumVelocity: Int

    private var menuSliding = false

    init {
        val config = ViewConfiguration.get(getContext())
        scaledTouchSlop = config.scaledTouchSlop
        scaledMinimumVelocity = config.scaledMinimumFlingVelocity
        scaledMaximumVelocity = config.scaledMaximumFlingVelocity
        setOnTouchListener(this)
    }

    override fun onFinishInflate() {
        super.onFinishInflate()
        var contentView: View? = null
        var menuView: View? = null
        children.forEachIndexed { index, view ->
            if (index == childCount - 1) contentView = view
            else {
                val params = view.layoutParams as LayoutParams
                if (params.gravity == Gravity.END && menuView == null) {
                    menuView = view
                }
            }
        }
        if (contentView != null && menuView != null) {
            slideHelper = EndSlideMenuHelper(menuView!!, contentView!!)
        }
    }

    override fun onInterceptTouchEvent(ev: MotionEvent): Boolean {
        var isIntercepted = super.onInterceptTouchEvent(ev)
        when (ev.actionMasked) {
            MotionEvent.ACTION_DOWN -> {
                downX = ev.x
                downY = ev.y
                lastX = ev.x
                lastY = ev.y
                isIntercepted = handleActionDownIntercept(ev)
            }
            MotionEvent.ACTION_MOVE -> {
                val dx = ev.x - downX
                val dy = ev.y - downY
                isIntercepted = dx.absoluteValue > scaledTouchSlop && dx.absoluteValue > dy.absoluteValue
            }
            MotionEvent.ACTION_UP -> {
                isIntercepted = handleActionUpIntercept(ev).also {
                    if (!it) {
                        if (slideHelper.isMenuOpened()) {
                            slideHelper.translateToClosed()
                        }
                    }
                }
            }
            MotionEvent.ACTION_CANCEL -> {
                isIntercepted = true
                if (!slideHelper.isMenuClosed()) {
                    slideHelper.translateToClosed()
                }
            }
        }
        Log.d(TAG, "action = ${ev.actionMasked}, isIntercepted = $isIntercepted")

        return isIntercepted
    }

    private fun handleActionUpIntercept(ev: MotionEvent): Boolean {
        return when {
            slideHelper.isMenuClosed() -> false
            slideHelper.isMenuSliding() -> true
            slideHelper.isMenuOpened() -> {
                val x = ev.x
                val y = ev.y
                !slideHelper.isInMenuBound(x, y)
            }
            else -> false
        }
    }

    private fun handleActionDownIntercept(ev: MotionEvent): Boolean {
        return when {
            slideHelper.isMenuClosed() -> false
            slideHelper.isMenuSliding() -> true
            slideHelper.isMenuOpened() -> {
                val x = ev.x
                val y = ev.y
                !slideHelper.isInMenuBound(x, y)
            }
            else -> false
        }
    }

    fun closeMenuForce(): Boolean {
        if (slideHelper.isMenuClosed()) {
            return false
        }
        slideHelper.translateToClosed()
        return true
    }

    override fun onTouch(v: View?, event: MotionEvent): Boolean {

        Log.d(TAG, "onTouch")
        var consume = false

        velocityTracker.addMovement(event)

        when (event.actionMasked) {
            MotionEvent.ACTION_DOWN -> {
                consume = true
            }
            MotionEvent.ACTION_MOVE -> {
                val dX = event.x - lastX
                val dY = event.y - lastY
                if (!menuSliding
                    && dX.absoluteValue > scaledTouchSlop
                    && dX.absoluteValue > dY.absoluteValue) {
                    parent?.requestDisallowInterceptTouchEvent(true)
                    menuSliding = true
                }
                slideHelper.translateBy(dX)
                consume = true
            }
            MotionEvent.ACTION_UP -> {
                if (!menuSliding) {
                    if (slideHelper.isMenuOpened()) {
                        slideHelper.translateToClosed()
                    }
                }
                menuSliding = false
                parent?.requestDisallowInterceptTouchEvent(false)
                velocityTracker.computeCurrentVelocity(1000)
                val currentVelocity = velocityTracker.xVelocity
                if (currentVelocity.absoluteValue > scaledMinimumVelocity) {
                    when {
                        currentVelocity > 0 && !slideHelper.isMenuClosed() -> slideHelper.translateToClosed()
                        currentVelocity < 0 && !slideHelper.isMenuOpened() -> slideHelper.translateToOpened()
                    }
                } else {
                    slideHelper.autoTranslateFinish()
                }

                consume = true
            }
            MotionEvent.ACTION_CANCEL -> {
                menuSliding = false
                Log.d(TAG, "isMenuClosed = ${slideHelper.isMenuClosed()}")
                if (!slideHelper.isMenuClosed()) {
                    slideHelper.translateToClosed()
                }
            }
        }

        lastX = event.x
        lastY = event.y

        return consume
    }
}