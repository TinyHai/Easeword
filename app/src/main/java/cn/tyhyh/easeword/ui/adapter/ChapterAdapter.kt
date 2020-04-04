package cn.tyhyh.easeword.ui.adapter

import androidx.recyclerview.widget.DiffUtil
import cn.tyhyh.easeword.data.entity.Chapter
import cn.tyhyh.easeword.ui.adapter.item.ChapterItem
import cn.tyhyh.easeword.ui.viewmodel.MainViewModel

class ChapterAdapter(
    private val viewModel: MainViewModel
) : CommonRcvListAdapter<Chapter>(DIFF_CALLBACK), ChapterItem.CallBack {

    companion object {
        private const val TAG = "ChapterAdapter"

        private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<Chapter>() {
            override fun areItemsTheSame(oldItem: Chapter, newItem: Chapter): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: Chapter, newItem: Chapter): Boolean {
                return oldItem == newItem
            }
        }
    }

    private var selectedChapterId = 0L

    fun setSelectedChapterId(chapterId: Long) {
        selectedChapterId = chapterId
        notifyItemRangeChanged(0, viewModel.getUnlockedChapterCount())
    }

    override fun createCommonItem(viewType: Any): CommonItem<Chapter> {
        return ChapterItem(this)
    }

    override fun getSelectedChapterId(): Long {
        return selectedChapterId
    }

    override fun getMaxUnlockedChapterId(): Long {
        return viewModel.getMaxUnlockedChapterId()
    }

    override fun setSelectedChapter(chapter: Chapter) {
        viewModel.setSelectedChapter(chapter)
    }

    override fun rollUpCatalog() {
        viewModel.reverseCatalogStatus()
    }
}
