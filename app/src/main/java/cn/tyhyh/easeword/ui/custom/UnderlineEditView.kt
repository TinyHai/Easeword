package cn.tyhyh.easeword.ui.custom

import android.content.Context
import android.graphics.Canvas
import android.graphics.DashPathEffect
import android.graphics.Paint
import android.graphics.Rect
import android.util.AttributeSet
import android.util.Log
import androidx.appcompat.widget.AppCompatEditText
import androidx.core.content.ContextCompat
import androidx.core.graphics.withSave
import cn.tyhyh.easeword.R
import cn.tyhyh.easeword.util.DisplayUtil

/**
 * author: tiny
 * created on: 20-3-26 上午9:58
 */
class UnderlineEditView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : AppCompatEditText(context, attrs, defStyleAttr) {

    private val clipBound = Rect()

    private val dashPaint: Paint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        val fourDp = DisplayUtil.dp2px(8).toFloat()
        style = Paint.Style.STROKE
        pathEffect = DashPathEffect(floatArrayOf(fourDp, fourDp), 0f)
        strokeWidth = DisplayUtil.dp2px(1).toFloat()
        color = ContextCompat.getColor(context, R.color.colorDashLine)
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        val saveCount = canvas.save()

        clipBound.let {
            it.left = paddingLeft
            it.right = width - paddingRight
            it.top = paddingTop
            it.bottom = height - paddingBottom
        }
        Log.d(TAG, clipBound.toString())

        canvas.clipRect(clipBound)
        canvas.translate(paddingLeft.toFloat(), paddingTop.toFloat())

        val lineCount = layout.lineCount

        val lineLeft = 0f
        val lineRight = (width - paddingLeft - paddingRight).toFloat()
        var lineBottom = 0f
        val lineExtraHeight = lineSpacingExtra
        val lastLineIndex = lineCount - 1
        for (line in 0 until lastLineIndex) {
            canvas.withSave {
                lineBottom = layout.getLineBottom(line).minus(lineExtraHeight)
                canvas.translate(0f, lineBottom)
                canvas.drawLine(lineLeft, 0f, lineRight, 0f, dashPaint)
            }
        }
        canvas.withSave {
            lineBottom = layout.getLineBottom(lastLineIndex).toFloat()
            canvas.translate(0f, lineBottom)
            canvas.drawLine(lineLeft, 0f, lineRight, 0f, dashPaint)
        }

        canvas.restoreToCount(saveCount)
    }

    companion object {
        private const val TAG = "UnderlineEditView"
    }
}