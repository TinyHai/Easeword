package cn.tyhyh.easeword.ui.adapter

import android.app.Activity
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import cn.tyhyh.easeword.R
import cn.tyhyh.easeword.data.entity.Essay
import cn.tyhyh.easeword.databinding.ItemDetailDrawingBinding
import cn.tyhyh.easeword.databinding.ItemDetailNoteBinding
import cn.tyhyh.easeword.ui.activity.NoteActivity
import cn.tyhyh.easeword.ui.activity.PaintActivity
import cn.tyhyh.easeword.ui.viewmodel.DetailViewModel
import cn.tyhyh.easeword.util.FontUtil
import cn.tyhyh.easeword.util.ImageUtil
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.item_detail_drawing.view.*

/**
 * author: tiny
 * created on: 20-3-25 下午4:30
 */
class DetailAdapter(
    private val activity: Activity,
    private val viewModel: DetailViewModel
) : ListAdapter<Essay, RecyclerView.ViewHolder>(DIFF_CALLBACK) {

    companion object {

        private const val TAG = "DetailAdapter"

        private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<Essay>() {
            override fun areItemsTheSame(oldItem: Essay, newItem: Essay): Boolean {
                return oldItem === newItem
            }

            override fun areContentsTheSame(oldItem: Essay, newItem: Essay): Boolean {
                return oldItem == newItem
            }
        }
    }

    private lateinit var rv: RecyclerView

    override fun onAttachedToRecyclerView(recyclerView: RecyclerView) {
        super.onAttachedToRecyclerView(recyclerView)
        rv = recyclerView
    }

    private val snackBar by lazy { 
        Snackbar.make(rv, "", Snackbar.LENGTH_SHORT).apply {
            addCallback(object : Snackbar.Callback() {
                override fun onDismissed(transientBottomBar: Snackbar?, event: Int) {
                    super.onDismissed(transientBottomBar, event)
                    if (waitToDeleted.isNotEmpty()) {
                        viewModel.deleteEssays(waitToDeleted)
                    }
                }
            })
            setActionTextColor(ContextCompat.getColor(context, R.color.colorOrange))
            view.background = ColorDrawable(Color.WHITE)
            setTextColor(ContextCompat.getColor(context, R.color.colorCommonBlack))
        }
    }

    private var waitToDeleted = HashSet<Essay>()
    
    private var handleDelete: (Essay, Int, () -> Unit) -> Unit = { essay, _, undo ->
        waitToDeleted.add(essay)
        snackBar.setText(rv.context.getString(R.string.delete_success))
            .setAction(rv.context.getString(R.string.undo)) {
                undo()
                waitToDeleted.remove(essay)
            }.show()
    }

    class DetailDrawingViewHolder(val binding: ItemDetailDrawingBinding) : RecyclerView.ViewHolder(binding.root)

    class DetailNoteViewHolder(val binding: ItemDetailNoteBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            Essay.TYPE_DRAWING -> DetailDrawingViewHolder(
                ItemDetailDrawingBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
            )
            Essay.TYPE_NOTE -> DetailNoteViewHolder(
                ItemDetailNoteBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                ).also { FontUtil.setTypefaceForTextView(it.noteTv, FontUtil.YRDZS_PATH) }
            )
            else -> throw IllegalArgumentException("viewType = $viewType is invalid")
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val essay = getItem(position)
        when (holder) {
            is DetailDrawingViewHolder -> {
                holder.apply {
                    binding.deleteBtn.setOnClickListener {
                        Log.d(TAG, this.toString())
                        removeItem(adapterPosition)
                    }
                    binding.root.drawingIv.setOnClickListener {
                        PaintActivity.actionStart(activity, viewModel.getWordId(), essay.id)
                    }
                    ImageUtil.setImage(binding.drawingIv, essay.content)
                }
            }
            is DetailNoteViewHolder -> {
                holder.apply {
                    binding.deleteBtn.setOnClickListener {
                        Log.d(TAG, this.toString())
                        removeItem(adapterPosition)
                    }
                    binding.noteTv.setOnClickListener {
                        NoteActivity.actionStart(activity, viewModel.getWordId(), essay.id)
                    }
                    binding.noteTv.text = essay.content
                }
            }
        }
    }

    override fun getItemViewType(position: Int): Int {
        val essayPair = getItem(position)
        return essayPair.type
    }

    private fun removeItem(position: Int) {
        val oldContent = ArrayList(currentList)
        val undo = {
            submitList(oldContent)
        }
        val newList = ArrayList(oldContent)
        val removed = newList.removeAt(position)
        submitList(newList)
        handleDelete(removed, position, undo)
    }
}