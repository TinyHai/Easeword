package cn.tyhyh.easeword.ui.custom

import android.animation.ObjectAnimator
import android.animation.ValueAnimator
import android.util.Log
import android.view.View
import androidx.core.view.marginBottom
import androidx.core.view.marginRight
import kotlin.math.absoluteValue

/**
 * author: tiny
 * created on: 20-3-28 上午11:07
 */
sealed class SlideMenuHelper(val contentView: View) {

    private val values = TranslationValue(0f, 0f)

    protected abstract val halfDistance: Float

    abstract val maxDuration: Long

    protected abstract val translateAnimator: ValueAnimator

    fun isMenuClosed() = contentView.translationX == 0f

    abstract fun translateBy(delta: Float)

    protected abstract fun getTranslationValuesForOpen(values: TranslationValue)
    protected abstract fun getTranslationValuesForClose(values: TranslationValue)

    fun translateToOpened() {
        ensureAnimatorStop()
        resetTranslationValues()
        getTranslationValuesForOpen(values)
        translateAnimator.let {
            it.setFloatValues(values.from, values.to)
            it.duration = getDuration(values.absoluteDistance())
            it.start()
        }
    }
    fun translateToClosed() {
        ensureAnimatorStop()
        resetTranslationValues()
        getTranslationValuesForClose(values)
        translateAnimator.let {
            it.setFloatValues(values.from, values.to)
            it.duration = getDuration(values.absoluteDistance())
            it.start()
        }
    }

    abstract fun isInMenuBound(x: Float, y: Float): Boolean

    fun isMenuSliding() = !isMenuClosed() && !isMenuOpened()

    abstract fun autoTranslateFinish()

    abstract fun getMenuWidth(): Int

    abstract fun isMenuOpened(): Boolean

    private fun getDuration(distance: Float): Long {
        return distance.div(getMenuWidth()).times(maxDuration).toLong().coerceAtMost(maxDuration)
    }

    private fun resetTranslationValues() {
        values.reset()
    }

    protected fun ensureAnimatorStop() {
        if (translateAnimator.isRunning) {
            translateAnimator.cancel()
        }
    }

    protected class TranslationValue(var from: Float, var to: Float) {
        fun reset() {
            from = 0f
            to = 0f
        }

        fun set(from: Float, to: Float) {
            this.from = from
            this.to = to
        }

        fun absoluteDistance() = to.minus(from).absoluteValue

        override fun toString(): String {
            return "TranslationValue(from=$from, to=$to)"
        }
    }
}

class EndSlideMenuHelper(val menuView: View, contentView: View) : SlideMenuHelper(contentView) {

    companion object {
        private const val TAG = "EndSlideMenuHelper"
    }

    override val halfDistance: Float
        get() = getMenuWidth().div(2f)

    override val maxDuration = 300L

    override val translateAnimator: ValueAnimator
        get() = ObjectAnimator.ofFloat(contentView, "translationX", 0f, 0f)

    override fun translateBy(delta: Float) {
        ensureAnimatorStop()
        val dest = contentView.translationX + delta
        val translation = dest.coerceAtLeast(-getMenuWidth().toFloat()).coerceAtMost(0f)
        Log.d(TAG, "delta = $delta,dest = $dest, translation = $translation")
        contentView.translationX = translation
    }

    override fun getMenuWidth() = menuView.width

    override fun isMenuOpened() = contentView.translationX == -getMenuWidth().toFloat()

    override fun getTranslationValuesForOpen(values: TranslationValue) {
        val from = contentView.translationX
        val to = -getMenuWidth().toFloat()
        values.set(from, to)
        Log.d(TAG, "open = $values")
    }

    override fun getTranslationValuesForClose(values: TranslationValue) {
        val from = contentView.translationX
        val to = 0f
        values.set(from, to)
        Log.d(TAG, "close = $values")
    }

    override fun isInMenuBound(x: Float, y: Float): Boolean {
        val menuRight = contentView.right - contentView.paddingRight - menuView.marginRight
        val menuLeft = menuRight - getMenuWidth()
        val menuBottom = contentView.bottom - contentView.paddingBottom - menuView.marginBottom
        val menuTop = menuBottom - menuView.height
        if (x > menuLeft && x < menuRight && y > menuTop && y < menuBottom) {
            return true
        }
        return false
    }

    override fun autoTranslateFinish() {
        if (contentView.translationX.absoluteValue > halfDistance) {
            translateToOpened()
        } else {
            translateToClosed()
        }
    }
}