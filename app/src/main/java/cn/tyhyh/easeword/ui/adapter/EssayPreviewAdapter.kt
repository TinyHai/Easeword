package cn.tyhyh.easeword.ui.adapter

import android.net.Uri
import android.view.Gravity
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.widget.AppCompatImageView
import androidx.appcompat.widget.AppCompatTextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import cn.tyhyh.easeword.R
import cn.tyhyh.easeword.data.entity.Essay
import cn.tyhyh.easeword.util.DisplayUtil
import java.io.File

/**
 * author: tiny
 * created on: 20-3-26 下午9:08
 */
class EssayPreviewAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    var essayList: List<Essay>? = null
        set(value) {
            val refresh = field != value
            field = value
            if (refresh) {
                notifyDataSetChanged()
            }
        }

    private class ImageViewHolder(val iv: ImageView) : RecyclerView.ViewHolder(iv)

    private class TextViewHolder(val tv: TextView) : RecyclerView.ViewHolder(tv)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val context = parent.context
        return when (viewType) {
            Essay.TYPE_DRAWING -> {
                val imageView = AppCompatImageView(context).apply {
                    layoutParams = ViewGroup.LayoutParams(
                        DisplayUtil.dp2px(60),
                        ViewGroup.LayoutParams.MATCH_PARENT
                    )
                    scaleType = ImageView.ScaleType.CENTER_CROP
                }
                ImageViewHolder(imageView)
            }
            Essay.TYPE_NOTE -> {
                val textView = AppCompatTextView(context).apply {
                    layoutParams = ViewGroup.LayoutParams(
                        DisplayUtil.dp2px(60),
                        ViewGroup.LayoutParams.MATCH_PARENT
                    )
                    textSize = 10f
                    setTextColor(ContextCompat.getColor(context, R.color.colorNormalText))
                    setLineSpacing(DisplayUtil.sp2px(6).toFloat(), 1f)
                    maxLines = 3
                    gravity = Gravity.CENTER_HORIZONTAL or Gravity.TOP
                }
                TextViewHolder(textView)
            }
            else -> throw IllegalArgumentException("viewType = $viewType is invalid")
        }
    }

    override fun getItemCount(): Int {
        return essayList?.size ?: 0
    }

    override fun getItemViewType(position: Int): Int {
        val essayPair = essayList!![position]
        return essayPair.type
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val essayPair = essayList!![position]
        when (holder) {
            is ImageViewHolder -> {
                holder.iv.setImageURI(Uri.fromFile(File(essayPair.content)))
            }
            is TextViewHolder -> {
                holder.tv.text = essayPair.content
            }
        }
    }
}