package cn.tyhyh.easeword.util

import android.graphics.Typeface
import android.widget.TextView

object FontUtil {

    const val YRDZS_PATH = "fonts/yrdzs.ttf"

    private val fontCache = HashMap<String, Typeface>()

    fun setTypefaceForTextView(tv: TextView, fontPath: String) {
        tv.typeface = getTypefaceFromCache(fontPath) ?: {
            val context = tv.context
            val typeface = Typeface.createFromAsset(context.assets, fontPath)
            if (typeface != null) {
                fontCache[fontPath] = typeface
            }
            typeface
        }()
    }

    private fun getTypefaceFromCache(fontPath: String): Typeface? {
        return fontCache[fontPath]
    }
}