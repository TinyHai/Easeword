package cn.tyhyh.easeword.ui.adapter.item

import android.app.Activity
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import cn.tyhyh.easeword.data.entity.Essay
import cn.tyhyh.easeword.databinding.ItemDetailDrawingBinding
import cn.tyhyh.easeword.databinding.ItemDetailNoteBinding
import cn.tyhyh.easeword.ui.activity.NoteActivity
import cn.tyhyh.easeword.ui.activity.DrawingActivity
import cn.tyhyh.easeword.ui.adapter.CommonRcvListAdapter
import cn.tyhyh.easeword.util.FontUtil
import cn.tyhyh.easeword.util.ImageUtil
import kotlinx.android.synthetic.main.item_detail_drawing.view.*

/**
 * author: tiny
 * created on: 20-4-4 下午5:25
 */
interface Callback {
    fun getCurrentWordId(): Long

    fun removeItem(adapterPosition: Int)
}

class DrawingItem(private val activity: Activity, private val callback: Callback) :
    CommonRcvListAdapter.CommonItem<Essay> {

    private lateinit var binding: ItemDetailDrawingBinding

    override fun getRootView(context: Context, parent: ViewGroup): View {
        binding = ItemDetailDrawingBinding.inflate(LayoutInflater.from(context), parent, false)
        return binding.root
    }

    override fun bindData(data: Essay) {
        binding.root.drawingIv.setOnClickListener {
            DrawingActivity.actionStart(activity, callback.getCurrentWordId(), data.id)
        }
        ImageUtil.setImage(binding.drawingIv, data.content)
    }

    override fun initViews(holder: RecyclerView.ViewHolder) {
        binding.deleteBtn.setOnClickListener {
            callback.removeItem(holder.adapterPosition)
        }
    }
}

class NoteItem(private val activity: Activity, private val callback: Callback) :
    CommonRcvListAdapter.CommonItem<Essay> {

    private lateinit var binding: ItemDetailNoteBinding

    override fun getRootView(context: Context, parent: ViewGroup): View {
        binding = ItemDetailNoteBinding.inflate(LayoutInflater.from(context), parent, false)
        return binding.root
    }

    override fun bindData(data: Essay) {
        binding.noteTv.setOnClickListener {
            NoteActivity.actionStart(activity, callback.getCurrentWordId(), data.id)
        }
        binding.noteTv.text = data.content
    }

    override fun initViews(holder: RecyclerView.ViewHolder) {
        binding.deleteBtn.setOnClickListener {
            callback.removeItem(holder.adapterPosition)
        }
        FontUtil.setTypefaceForTextView(binding.noteTv, FontUtil.YRDZS)
    }
}