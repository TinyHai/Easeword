package cn.tyhyh.easeword.ui.adapter

import android.view.View
import androidx.databinding.BindingAdapter
import androidx.databinding.InverseBindingAdapter
import androidx.databinding.InverseBindingListener
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout

/**
 * author: tiny
 * created on: 20-3-24 下午8:16
 */

@BindingAdapter("isGone")
fun bindIsGone(view: View, isGone: Boolean) {
    view.visibility = if (isGone) View.GONE else View.VISIBLE
}

@InverseBindingAdapter(attribute = "refreshing", event = "refreshing")
fun getRefreshing(refreshLayout: SwipeRefreshLayout) = refreshLayout.isRefreshing

@BindingAdapter("refreshing", requireAll = false)
fun setListenerForRefreshLayout(
    refreshLayout: SwipeRefreshLayout,
    listener: InverseBindingListener?
) {
    refreshLayout.setOnRefreshListener {
        listener?.onChange()
    }
}