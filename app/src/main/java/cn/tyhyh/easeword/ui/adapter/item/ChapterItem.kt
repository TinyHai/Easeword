package cn.tyhyh.easeword.ui.adapter.item

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.view.isInvisible
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import cn.tyhyh.easeword.R
import cn.tyhyh.easeword.data.entity.Chapter
import cn.tyhyh.easeword.databinding.ItemChapterRecyclerViewBinding
import cn.tyhyh.easeword.ui.adapter.CommonRcvListAdapter
import cn.tyhyh.easeword.util.FontUtil

/**
 * author: tiny
 * created on: 20-4-4 下午4:10
 */
class ChapterItem(private val callback: CallBack) : CommonRcvListAdapter.CommonItem<Chapter> {

    private lateinit var context: Context

    private lateinit var binding: ItemChapterRecyclerViewBinding

    override fun getRootView(context: Context, parent: ViewGroup): View {
        this.context = context
        binding = ItemChapterRecyclerViewBinding.inflate(LayoutInflater.from(context), parent, false)
        return binding.root
    }

    override fun bindData(data: Chapter) {
        val chapterId = data.id
        val maxUnlockedChapterId = callback.getMaxUnlockedChapterId()
        val isUnlock = chapterId <= maxUnlockedChapterId

        binding.apply {
            when (chapterId) {
                callback.getSelectedChapterId() -> {
                    changeToSelected(this, chapterId)
                }
                else -> {
                    changeToNormal(this)
                }
            }
            if (isUnlock) {
                setToUnlockedStatus(this, data)
            } else {
                setToLockedStatus(this)
            }
            previewText.text = data.preview
        }
    }

    override fun initViews(holder: RecyclerView.ViewHolder) {
        FontUtil.setTypefaceForTextView(binding.previewText, FontUtil.YRDZS)
    }

    @Suppress("SameParameterValue")
    private fun changeToSelected(binding: ItemChapterRecyclerViewBinding, chapterId: Long) {
        binding.apply {
            chapterItemContainer.isSelected = true
            if (!selectChapterView.isInflated) {
                selectChapterView.viewStub?.inflate()
            }
            val chapterCountView = selectChapterView.root
            if (chapterCountView is android.widget.TextView) {
                chapterCountView.text = chapterId.toString()
            }
            if (chapterCountView.isInvisible) {
                chapterCountView.visibility = View.VISIBLE
            }
            previewText.setTextColor(ContextCompat.getColor(context, R.color.colorSelectedText))
        }
    }

    private fun changeToNormal(binding: ItemChapterRecyclerViewBinding) {
        binding.apply {
            chapterItemContainer.isSelected = false
            if (selectChapterView.isInflated && selectChapterView.root.isVisible) {
                selectChapterView.root.visibility = View.INVISIBLE
            }
            previewText.setTextColor(ContextCompat.getColor(context, R.color.colorNormalText))
        }
    }

    private fun setToUnlockedStatus(binding: ItemChapterRecyclerViewBinding, chapter: Chapter) {
        binding.apply {
            chapterItemContainer.isEnabled = true
            chapterItemContainer.setOnClickListener {
                callback.setSelectedChapter(chapter)
                callback.rollUpCatalog()
            }
            if (lockedView.isVisible) {
                lockedView.visibility = View.INVISIBLE
            }
        }
    }

    private fun setToLockedStatus(binding: ItemChapterRecyclerViewBinding) {
        binding.apply {
            chapterItemContainer.isEnabled = false
            if (lockedView.isInvisible) {
                lockedView.visibility = View.VISIBLE
            }
            previewText.setTextColor(ContextCompat.getColor(context, R.color.colorDark))
        }
    }

    interface CallBack {
        fun getSelectedChapterId(): Long

        fun getMaxUnlockedChapterId(): Long

        fun setSelectedChapter(chapter: Chapter)

        fun rollUpCatalog()
    }
}