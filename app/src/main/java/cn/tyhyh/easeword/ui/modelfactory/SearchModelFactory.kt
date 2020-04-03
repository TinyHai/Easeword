package cn.tyhyh.easeword.ui.modelfactory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import cn.tyhyh.easeword.data.repository.ChapterRepository
import cn.tyhyh.easeword.data.repository.WordRepository
import cn.tyhyh.easeword.ui.viewmodel.SearchViewModel

/**
 * author: tiny
 * created on: 20-4-3 下午2:59
 */
class SearchModelFactory(
    private val chapterRepository: ChapterRepository,
    private val wordRepository: WordRepository
) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return modelClass.cast(SearchViewModel(chapterRepository, wordRepository))!!
    }
}