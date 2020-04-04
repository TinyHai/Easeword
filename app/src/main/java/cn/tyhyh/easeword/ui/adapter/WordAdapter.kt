package cn.tyhyh.easeword.ui.adapter

import android.app.Activity
import androidx.recyclerview.widget.DiffUtil
import cn.tyhyh.easeword.data.entity.Word
import cn.tyhyh.easeword.ui.adapter.item.HasEssayWordItem
import cn.tyhyh.easeword.ui.adapter.item.NoEssayWordItem

class WordAdapter(
    private val activity: Activity
) : CommonRcvListAdapter<Word>(DIFF_CALLBACK) {

    companion object {
        private const val TYPE_HAS_ESSAY = 100
        private const val TYPE_NO_ESSAY = 101

        private const val TAG = "WordAdapter"

        private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<Word>() {
            override fun areItemsTheSame(oldItem: Word, newItem: Word): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: Word, newItem: Word): Boolean {
                return oldItem == newItem
            }
        }
    }

    override fun getItemType(data: Word): Int {
        return if (data.hasEssay) TYPE_HAS_ESSAY else TYPE_NO_ESSAY
    }

    private val Word.hasEssay: Boolean
        get() = with(this.essays) {
            this.isNotEmpty()
        }

    override fun createCommonItem(viewType: Any): CommonItem<Word> {
        return when (viewType) {
            TYPE_HAS_ESSAY -> {
                HasEssayWordItem(activity)
            }
            TYPE_NO_ESSAY -> {
                NoEssayWordItem(activity)
            }
            else -> {
                throw IllegalArgumentException("viewType = $viewType is invalid")
            }
        }
    }
}