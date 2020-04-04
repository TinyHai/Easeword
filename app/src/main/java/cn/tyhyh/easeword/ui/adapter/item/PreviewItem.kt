package cn.tyhyh.easeword.ui.adapter.item

import android.content.Context
import android.text.TextUtils
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.widget.AppCompatImageView
import androidx.appcompat.widget.AppCompatTextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import cn.tyhyh.easeword.R
import cn.tyhyh.easeword.data.entity.Essay
import cn.tyhyh.easeword.ui.adapter.CommonRcvListAdapter
import cn.tyhyh.easeword.util.DisplayUtil
import cn.tyhyh.easeword.util.ImageUtil

/**
 * author: tiny
 * created on: 20-4-4 下午6:02
 */

class PreDrawingItem : CommonRcvListAdapter.CommonItem<Essay> {

    private lateinit var rootView: ImageView

    override fun getRootView(context: Context, parent: ViewGroup): View {
        rootView = AppCompatImageView(context).apply {
            layoutParams = ViewGroup.LayoutParams(
                DisplayUtil.dp2px(60),
                ViewGroup.LayoutParams.MATCH_PARENT
            )
            scaleType = ImageView.ScaleType.CENTER_CROP
        }
        return rootView
    }

    override fun bindData(data: Essay) {
        ImageUtil.setImage(rootView, data.content)
    }

    override fun initViews(holder: RecyclerView.ViewHolder) {}
}

class PreNoteItem : CommonRcvListAdapter.CommonItem<Essay> {

    private lateinit var rootView: TextView

    override fun getRootView(context: Context, parent: ViewGroup): View {
        rootView = AppCompatTextView(context).apply {
            layoutParams = ViewGroup.LayoutParams(
                DisplayUtil.dp2px(60),
                ViewGroup.LayoutParams.MATCH_PARENT
            )
            textSize = 10f
            setTextColor(ContextCompat.getColor(context, R.color.colorNormalText))
            setLineSpacing(DisplayUtil.sp2px(6).toFloat(), 1f)
            maxLines = 3
            gravity = Gravity.CENTER_HORIZONTAL or Gravity.TOP
            ellipsize = TextUtils.TruncateAt.END
        }
        return rootView
    }

    override fun bindData(data: Essay) {
        rootView.text = data.content
    }

    override fun initViews(holder: RecyclerView.ViewHolder) {}
}