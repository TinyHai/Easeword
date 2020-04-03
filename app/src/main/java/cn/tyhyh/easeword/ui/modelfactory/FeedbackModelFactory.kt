package cn.tyhyh.easeword.ui.modelfactory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import cn.tyhyh.easeword.ui.viewmodel.FeedBackViewModel

/**
 * author: tiny
 * created on: 20-3-22 上午11:38
 */
class FeedbackModelFactory : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return modelClass.cast(FeedBackViewModel())!!
    }
}