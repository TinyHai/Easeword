package cn.tyhyh.easeword.ui.modelfactory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import cn.tyhyh.easeword.data.repository.ChapterRepository
import cn.tyhyh.easeword.data.repository.WordRepository
import cn.tyhyh.easeword.ui.viewmodel.MainViewModel

class MainModelFactory(
    private val chapterRepository: ChapterRepository,
    private val wordRepository: WordRepository
) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return modelClass.cast(MainViewModel(chapterRepository, wordRepository))!!
    }
}