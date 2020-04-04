package cn.tyhyh.easeword.ui.adapter

import android.app.Activity
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import cn.tyhyh.easeword.R
import cn.tyhyh.easeword.data.entity.Essay
import cn.tyhyh.easeword.ui.adapter.item.Callback
import cn.tyhyh.easeword.ui.adapter.item.DrawingItem
import cn.tyhyh.easeword.ui.adapter.item.NoteItem
import cn.tyhyh.easeword.ui.viewmodel.DetailViewModel
import com.google.android.material.snackbar.Snackbar

/**
 * author: tiny
 * created on: 20-3-25 下午4:30
 */
class DetailAdapter(
    private val activity: Activity,
    private val viewModel: DetailViewModel
) : CommonRcvListAdapter<Essay>(DIFF_CALLBACK), Callback {

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

    override fun getItemType(data: Essay): Int {
        return data.type
    }

    override fun getCurrentWordId(): Long {
        return viewModel.getWordId()
    }

    override fun removeItem(adapterPosition: Int) {
        val oldContent = ArrayList(currentList)
        val undo = {
            submitList(oldContent)
        }
        val newList = ArrayList(oldContent)
        val removed = newList.removeAt(adapterPosition)
        submitList(newList)
        handleDelete(removed, adapterPosition, undo)
    }

    override fun createCommonItem(viewType: Any): CommonItem<Essay> {
        return when (viewType) {
            Essay.TYPE_DRAWING -> {
                DrawingItem(activity, this)
            }
            Essay.TYPE_NOTE -> {
                NoteItem(activity, this)
            }
            else -> {
                throw IllegalArgumentException("viewType = $viewType is invalid")
            }
        }
    }
}