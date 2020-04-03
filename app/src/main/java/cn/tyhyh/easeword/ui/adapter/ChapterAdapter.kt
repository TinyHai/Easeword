package cn.tyhyh.easeword.ui.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.core.view.isInvisible
import androidx.core.view.isVisible
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import cn.tyhyh.easeword.R
import cn.tyhyh.easeword.data.entity.Chapter
import cn.tyhyh.easeword.databinding.ItemChapterRecyclerViewBinding
import cn.tyhyh.easeword.ui.viewmodel.MainViewModel
import cn.tyhyh.easeword.util.FontUtil

class ChapterAdapter(
    private val context: Context,
    private val viewModel: MainViewModel
) : ListAdapter<Chapter, ChapterAdapter.ChapterItemHolder>(DIFF_CALLBACK) {

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

    class ChapterItemHolder(val binding: ItemChapterRecyclerViewBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChapterItemHolder {
        val binding = DataBindingUtil.inflate<ItemChapterRecyclerViewBinding>(
            LayoutInflater.from(context),
            R.layout.item_chapter_recycler_view,
            parent,
            false
        )
        return ChapterItemHolder(binding)
    }

    override fun onBindViewHolder(holder: ChapterItemHolder, position: Int) {
        val chapter = getItem(position)
        val chapterId = chapter.id
        val unLockChapterCount = viewModel.getUnlockChapterCount()
        val isUnlock = chapterId <= unLockChapterCount

        holder.binding.apply {
            when (chapterId) {
                selectedChapterId -> {
                    changeToSelected(this, chapterId)
                }
                else -> {
                    changeToNormal(this)
                }
            }
            if (isUnlock) {
                setToUnlockedStatus(this, chapter)
            } else {
                setToLockedStatus(this)
            }
            previewText.text = chapter.preview
            FontUtil.setTypefaceForTextView(previewText, FontUtil.YRDZS_PATH)
        }
    }

    @Suppress("SameParameterValue")
    private fun changeToSelected(binding: ItemChapterRecyclerViewBinding, chapterId: Long) {
        binding.apply {
            chapterItemContainer.isSelected = true
            if (!selectChapterView.isInflated) {
                selectChapterView.viewStub?.inflate()
            }
            val chapterCountView = selectChapterView.root
            if (chapterCountView is TextView) {
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
                viewModel.setSelectedChapter(chapter)
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
            previewText.setTextColor(ContextCompat.getColor(context, R.color.colorLockedText))
        }
    }

    fun setSelectedChapterId(chapterId: Long) {
        selectedChapterId = chapterId
        notifyItemRangeChanged(0, viewModel.getUnlockChapterCount())
    }
}