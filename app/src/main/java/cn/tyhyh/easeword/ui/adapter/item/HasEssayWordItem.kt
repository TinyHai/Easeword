package cn.tyhyh.easeword.ui.adapter.item

import android.app.Activity
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import cn.tyhyh.easeword.data.entity.Word
import cn.tyhyh.easeword.databinding.ItemHasEssayWordBinding
import cn.tyhyh.easeword.ui.activity.DetailActivity
import cn.tyhyh.easeword.ui.adapter.CommonRcvListAdapter
import cn.tyhyh.easeword.ui.adapter.EssayPreviewAdapter
import cn.tyhyh.easeword.util.FontUtil

/**
 * author: tiny
 * created on: 20-4-4 下午5:00
 */
class HasEssayWordItem(private val activity: Activity) : CommonRcvListAdapter.CommonItem<Word> {

    private lateinit var binding: ItemHasEssayWordBinding

    private lateinit var adapter: EssayPreviewAdapter

    override fun getRootView(context: Context, parent: ViewGroup): View {
        binding = ItemHasEssayWordBinding.inflate(LayoutInflater.from(context), parent, false)
        return binding.root
    }

    override fun bindData(data: Word) {
        binding.wordText.text = data.text
        adapter.submitList(data.essays)
        binding.wordText.setOnClickListener {
            DetailActivity.actionStart(activity, data.id)
        }
    }

    override fun initViews(holder: RecyclerView.ViewHolder) {
        FontUtil.setTypefaceForTextView(binding.wordText, FontUtil.YRDZS)
        adapter = EssayPreviewAdapter()
        binding.previewRv.adapter = adapter
    }
}