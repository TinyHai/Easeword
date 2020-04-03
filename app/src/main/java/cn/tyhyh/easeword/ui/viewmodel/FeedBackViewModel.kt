package cn.tyhyh.easeword.ui.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import cn.tyhyh.easeword.R
import cn.tyhyh.easeword.util.ToastUtil

/**
 * author: tiny
 * created on: 20-3-22 上午11:12
 */
class FeedBackViewModel : ViewModel() {

    val contact by lazy {
        MutableLiveData<String>()
    }

    val feedback by lazy {
        MutableLiveData<String>()
    }

    fun isContactEmpty() = contact.value.isNullOrEmpty()

    fun isFeedbackEmpty() = feedback.value.isNullOrEmpty()

    // TODO send feedback
    fun sendFeedback() {
        ToastUtil.showShortToast(R.string.in_development)
    }
}