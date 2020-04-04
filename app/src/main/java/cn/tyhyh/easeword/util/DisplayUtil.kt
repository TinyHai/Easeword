package cn.tyhyh.easeword.util

import cn.tyhyh.easeword.EaseWordApplication

object DisplayUtil {

    fun dp2px(dp: Int): Int {
        val context = EaseWordApplication.getApplication()
        val displayMetrics = context.resources.displayMetrics
        val scale = displayMetrics.density
        return (dp * scale + 0.5f).toInt()
    }

    fun sp2px(sp: Int): Int {
        val context = EaseWordApplication.getApplication()
        val displayMetrics = context.resources.displayMetrics
        val scale = displayMetrics.scaledDensity
        return (sp * scale + 0.5f).toInt()
    }
}