package cn.tyhyh.easeword.ui.adapter

import android.content.Context
import android.util.Log
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import cn.tyhyh.easeword.BuildConfig

/**
 * author: tiny
 * created on: 20-4-4 下午12:44
 */
abstract class CommonRcvListAdapter<T>(
    differCallback: DiffUtil.ItemCallback<T>
) : ListAdapter<T, CommonRcvListAdapter.CommonViewHolder<T>>(differCallback) {

    private val itemTypePool = object {
        private val typePool = HashMap<Any, Int>()

        fun getIntType(any: Any): Int {
            var intType = typePool[any]
            if (intType == null) {
                intType = typePool.size
                typePool[any] = intType
            }
            return intType
        }
    }

    private var type: Any = -1

    override fun onBindViewHolder(holder: CommonViewHolder<T>, position: Int) {
        holder.bindData(getItem(position))
    }

    override fun getItemViewType(position: Int): Int {
        val item = getItem(position)
        type = getItemType(item)
        return itemTypePool.getIntType(type)
    }

    protected open fun getItemType(data: T) = -1

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CommonViewHolder<T> {
        return CommonViewHolder(parent.context, parent, createCommonItem(type))
    }

    abstract fun createCommonItem(viewType: Any): CommonItem<T>

    class CommonViewHolder<T>(
        context: Context, parent: ViewGroup, val commonItem: CommonItem<T>
    ) : RecyclerView.ViewHolder(commonItem.getRootView(context, parent)) {

        companion object {

            private const val TAG = "CommonViewHolder"

            private var count = 0
            fun logCount() {
                if (BuildConfig.DEBUG) {
                    Log.d(TAG, "count = ${++count}")
                }
            }
        }

        init {
            logCount()
            initViews(this)
        }

        fun bindData(data: T) {
            commonItem.bindData(data)
        }

        private fun initViews(holder: RecyclerView.ViewHolder) {
            commonItem.initViews(holder)
        }
    }

    interface CommonItem<T> {

        fun getRootView(context: Context, parent: ViewGroup): View

        fun bindData(data: T)

        fun initViews(holder: RecyclerView.ViewHolder)
    }
}