package cn.tyhyh.easeword.ui.adapter

import android.app.Activity
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import cn.tyhyh.easeword.R
import cn.tyhyh.easeword.data.entity.Word
import cn.tyhyh.easeword.databinding.ItemHasEssayWordBinding
import cn.tyhyh.easeword.databinding.ItemNoEssayWordBinding
import cn.tyhyh.easeword.ui.activity.DetailActivity
import cn.tyhyh.easeword.ui.itemdecoration.ItemDivider
import cn.tyhyh.easeword.util.FontUtil

class WordAdapter(
    private val activity: Activity
) : ListAdapter<Word, RecyclerView.ViewHolder>(DIFF_CALLBACK) {

    companion object {
        private const val TYPE_HAS_ESSAY = 0
        private const val TYPE_NO_ESSAY = 1

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

    class HasEssayWordHolder(val binding: ItemHasEssayWordBinding) : RecyclerView.ViewHolder(binding.root)

    class NoEssayWordHolder(val binding: ItemNoEssayWordBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            TYPE_NO_ESSAY -> NoEssayWordHolder(
                ItemNoEssayWordBinding.inflate(
                    LayoutInflater.from(activity)
                )
            )
            TYPE_HAS_ESSAY -> HasEssayWordHolder(
                ItemHasEssayWordBinding.inflate(
                    LayoutInflater.from(activity)
                ).also {
                    it.previewRv.adapter = EssayPreviewAdapter()
                    it.previewRv.addItemDecoration(
                        ItemDivider(LinearLayout.HORIZONTAL).apply {
                            divider = ContextCompat.getDrawable(activity, R.drawable.essay_preview_divider)
                        }
                    )
                }
            )
            else -> throw IllegalArgumentException("viewType = $viewType is invalid")
        }
    }

    override fun getItemViewType(position: Int): Int {
        val word = currentList[position]
        return if (word.hasEssay) TYPE_HAS_ESSAY else TYPE_NO_ESSAY
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val word = currentList[position]

        when (holder) {
            is NoEssayWordHolder -> {
                holder.binding.wordText.text = word.text
                FontUtil.setTypefaceForTextView(holder.binding.wordText, FontUtil.YRDZS_PATH)
                holder.binding.wordText.setOnClickListener {
                    DetailActivity.actionStart(activity, word.id)
                }
            }
            is HasEssayWordHolder -> {
                holder.binding.wordText.text = word.text
                FontUtil.setTypefaceForTextView(holder.binding.wordText, FontUtil.YRDZS_PATH)
                (holder.binding.previewRv.adapter as EssayPreviewAdapter).essayList = word.essays
                holder.binding.wordText.setOnClickListener {
                    DetailActivity.actionStart(activity, word.id)
                }
            }
        }
    }

    private val Word.hasEssay: Boolean
        get() = with(this.essays) {
            this.isNotEmpty()
        }
}