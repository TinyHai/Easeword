package cn.tyhyh.easeword.util

import android.graphics.Typeface
import android.widget.TextView
import cn.tyhyh.easeword.EaseWordApplication

object FontUtil {

    const val YRDZS = "fonts/yrdzs.ttf"

    const val SWJZ = "fonts/swjz.ttf"


    private val fontPaths = arrayOf(
        SWJZ,
        YRDZS
    )

    private val fontCache = HashMap<String, Typeface>()

    fun setTypefaceForTextView(tv: TextView, fontPath: String) {
        tv.typeface = getTypefaceFromCache(fontPath) ?: {
            initTypeface()
            fontCache[fontPath]
        }()
    }

    private fun getTypefaceFromCache(fontPath: String): Typeface? {
        return fontCache[fontPath]
    }

    private fun initTypeface() {
        val application = EaseWordApplication.getApplication()
        fontPaths.forEach {
            val typeface = Typeface.createFromAsset(application.assets, it)
            fontCache[it] = typeface
        }
    }
}