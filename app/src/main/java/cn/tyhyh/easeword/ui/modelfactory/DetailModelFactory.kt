package cn.tyhyh.easeword.ui.modelfactory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import cn.tyhyh.easeword.data.repository.WordRepository
import cn.tyhyh.easeword.ui.viewmodel.DetailViewModel

/**
 * author: tiny
 * created on: 20-3-25 上午8:51
 */
class DetailModelFactory(
    private val wordRepository: WordRepository
) : ViewModelProvider.Factory {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return modelClass.cast(DetailViewModel(wordRepository))!!
    }
}