package cn.tyhyh.easeword.ui.custom

import android.animation.ValueAnimator
import android.content.Context
import android.graphics.Canvas
import android.graphics.Rect
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.util.Log
import android.widget.LinearLayout
import androidx.core.content.res.use
import androidx.core.graphics.withSave
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.OnLifecycleEvent
import cn.tyhyh.easeword.R
import cn.tyhyh.easeword.util.DisplayUtil
import kotlin.math.cos
import kotlin.math.sin
import kotlin.random.Random

class DynamicBgLinearLayout @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : LinearLayout(context, attrs, defStyleAttr), LifecycleObserver {

    private var drawOffsetTop = 0

    private lateinit var dynamicDrawables: List<Drawable>

    private lateinit var moveInfoMap: HashMap<Drawable, MoveInfo>

    private var speed = DisplayUtil.dp2px(2)

     init {
         if (attrs != null) {
            context.obtainStyledAttributes(
                attrs,
                R.styleable.DynamicBgLinearLayout,
                defStyleAttr,
                0
            ).use {
                for (i in 0..it.indexCount) {
                    when (it.getIndex(i)) {
                        R.styleable.DynamicBgLinearLayout_drawOffsetTop -> {
                            drawOffsetTop = it.getDimensionPixelSize(i, 0)
                        }
                        R.styleable.DynamicBgLinearLayout_moveSpeed -> {
                            speed = it.getDimensionPixelSize(i, DisplayUtil.dp2px(2))
                        }
                    }
                }
            }
        }
    }

    private val animator = ValueAnimator.ofInt(0, 30).apply {
        duration = 1000
        repeatMode = ValueAnimator.RESTART
        repeatCount = ValueAnimator.INFINITE

        addUpdateListener { _ ->
            dynamicDrawables.forEach {
                updateNextPoint(it, moveInfoMap[it]!!.point!!)
                Log.d(TAG, moveInfoMap[it].toString())
            }
            invalidate()
        }
    }

    private fun startAnimator() {
        if (this::dynamicDrawables.isInitialized) {
            if (animator.isStarted) {
                animator.cancel()
            }
            animator.start()
        }
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_PAUSE)
    fun pauseAnimator() {
        if (animator.isRunning && !animator.isPaused) {
            animator.pause()
        }
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
    fun resumeAnimator() {
        if (animator.isPaused) {
            animator.resume()
        }
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    fun stopAnimator() {
        if (animator.isStarted) {
            animator.cancel()
        }
    }

    fun setDrawables(vararg drawables: Drawable) {
        if (drawables.isEmpty()) {
            return
        }
        setWillNotDraw(false)
        dynamicDrawables = drawables.toList().also { drawableList ->
            drawableList.forEach { drawable ->
                drawable.setBounds(0, 0, drawable.intrinsicWidth, drawable.intrinsicHeight)
            }
        }
        moveInfoMap = HashMap(drawables.size * 2)
    }

    private fun initPositions() {
        dynamicDrawables.forEach {
            val moveInfo = moveInfoMap[it]!!
            val limit = moveInfo.limit!!
            moveInfo.point = Point(
                Random.nextFloat() * limit.right,
                Random.nextFloat() * limit.bottom
            )
        }
    }

    private fun computePositionLimit() {
        dynamicDrawables.forEach {
            computePositionLimitFor(it)
        }
    }

    override fun onLayout(changed: Boolean, l: Int, t: Int, r: Int, b: Int) {
        super.onLayout(changed, l, t, r, b)
        Log.d(TAG, "changed = $changed")
        if (changed) {
            computePositionLimit()
            initPositions()
            initDxDy()
            startAnimator()
        }
    }

    private fun initDxDy() {
        dynamicDrawables.forEach {
            val moveInfo = moveInfoMap[it]!!
            changeDxDyFor(moveInfo)
        }
    }

    private fun changeDxDyFor(moveInfo: MoveInfo) {

        val angle = (Random.nextFloat() * 70) + 10

        val radians = angle * Math.PI / 180

        val direction = moveInfo.direction
        val signX = direction[0]
        val signY = direction[1]
        Log.d(TAG, "cos = ${cos(radians)}")
        Log.d(TAG, "sin = ${sin(radians)}")
        Log.d(TAG, "speed = $speed")

        moveInfo.dX = (speed * cos(radians) * signX).toFloat()
        moveInfo.dY = (speed * sin(radians) * signY).toFloat()
    }

    private fun computePositionLimitFor(drawable: Drawable) {
        var moveInfo = moveInfoMap[drawable]
        if (moveInfo != null) {
            return
        }
        moveInfo = MoveInfo()

        val l = left
        val r = right - drawable.intrinsicWidth
        val t = top + drawOffsetTop
        val b = bottom - drawable.intrinsicHeight

        moveInfo.apply {
            limit = Rect(l, t, r, b)
        }

        moveInfoMap[drawable] = moveInfo
    }

    private fun updateNextPoint(drawable: Drawable, point: Point) {
        val moveInfo = moveInfoMap[drawable]!!
        val limit = moveInfo.limit!!

        val direction = moveInfo.direction

        val nextX = point.x + moveInfo.dX
        val nextY = point.y + moveInfo.dY

        if (hasEdgeOutBound(nextX, nextY, limit)) {
            if (nextX > limit.right) {
                point.x = limit.right.toFloat()
                direction[0] = -1
            }
            if (nextX < limit.left) {
                point.x = limit.left.toFloat()
                direction[0] = 1
            }
            if (nextY > limit.bottom) {
                point.y = limit.bottom.toFloat()
                direction[1] = -1
            }
            if (nextY < limit.top) {
                point.y = limit.top.toFloat()
                direction[1] = 1
            }
            changeDxDyFor(moveInfo)
        } else {
            point.x = nextX
            point.y = nextY
        }
    }

    private fun hasEdgeOutBound(x: Float, y: Float, limit: Rect): Boolean {
        return x > limit.right || x < limit.left || y > limit.bottom || y < limit.top
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        Log.d(TAG, "onDraw")
        dynamicDrawables.forEach { drawable ->
            val point = moveInfoMap[drawable]!!.point!!
            canvas.withSave {
                canvas.translate(point.x, point.y)
                drawable.draw(canvas)
            }
        }
    }

    companion object {
        private const val TAG = "DynamicBgLinearLayout"
    }

    private class Point(var x: Float, var y: Float) {
        override fun toString(): String {
            return "Point(x=$x, y=$y)"
        }
    }

    private class MoveInfo(
        var point: Point? = null,
        var direction: IntArray = IntArray(2) { if (Random.nextBoolean()) 1 else -1 },
        var limit: Rect? = null,
        var dX: Float = 0f,
        var dY: Float = 0f
    ) {
        override fun toString(): String {
            return "MoveInfo(point=$point,, direction=${direction.contentToString()}, dX=$dX, dY=$dY)"
        }
    }

    fun observe(owner: LifecycleOwner) {
        owner.lifecycle.addObserver(this)
    }
}