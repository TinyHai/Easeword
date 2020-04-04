package cn.tyhyh.easeword.ui.adapter

import androidx.recyclerview.widget.DiffUtil
import cn.tyhyh.easeword.data.entity.Essay
import cn.tyhyh.easeword.ui.adapter.item.PreDrawingItem
import cn.tyhyh.easeword.ui.adapter.item.PreNoteItem

/**
 * author: tiny
 * created on: 20-3-26 下午9:08
 */
class EssayPreviewAdapter : CommonRcvListAdapter<Essay>(DIFF_CALLBACK) {

    companion object {
        private const val TAG = "EssayPreviewAdapter"

        private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<Essay>() {
            override fun areItemsTheSame(oldItem: Essay, newItem: Essay): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: Essay, newItem: Essay): Boolean {
                return oldItem == newItem
            }
        }
    }

    override fun getItemType(data: Essay): Int {
        return data.type
    }


    override fun createCommonItem(viewType: Any): CommonItem<Essay> {
        return when (viewType) {
            Essay.TYPE_DRAWING -> {
                PreDrawingItem()
            }
            Essay.TYPE_NOTE -> {
                PreNoteItem()
            }
            else -> {
                throw IllegalArgumentException("viewType = $viewType is invalid")
            }
        }
    }
}