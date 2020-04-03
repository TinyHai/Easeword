package cn.tyhyh.easeword.ui.modelfactory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import cn.tyhyh.easeword.data.repository.WordRepository
import cn.tyhyh.easeword.ui.viewmodel.NoteViewModel

/**
 * author: tiny
 * created on: 20-3-31 下午3:58
 */
class NoteModelFactory(private val wordRepository: WordRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return modelClass.cast(NoteViewModel(wordRepository))!!
    }
}