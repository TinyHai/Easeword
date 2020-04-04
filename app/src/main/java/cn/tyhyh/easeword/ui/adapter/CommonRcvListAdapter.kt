package cn.tyhyh.easeword.ui.adapter

import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import kale.adapter.CommonRcvAdapter

/**
 * author: tiny
 * created on: 20-4-4 下午12:44
 */
abstract class CommonRcvListAdapter<T>(
    differCallback: DiffUtil.ItemCallback<T>
) : CommonRcvAdapter<T>(emptyList()) {

    @Suppress("LeakingThis")
    private val differ: AsyncListDiffer<T> = AsyncListDiffer(this, differCallback)

    override fun setData(data: List<T>) {
        differ.submitList(data)
        super.setData(data)
    }

    fun submitList(data: List<T>) {
        setData(data)
    }
}