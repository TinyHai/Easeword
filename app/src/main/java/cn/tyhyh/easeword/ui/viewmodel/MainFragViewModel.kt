package cn.tyhyh.easeword.ui.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import cn.tyhyh.easeword.data.entity.Chapter
import cn.tyhyh.easeword.data.entity.Essay
import cn.tyhyh.easeword.data.repository.SimpleRepositoryDataChangeListener
import cn.tyhyh.easeword.data.repository.WordRepository
import kotlinx.coroutines.Dispatchers

class MainFragViewModel(
    private val wordRepository: WordRepository
) : ViewModel() {

    companion object {
        private const val TAG = "MainFragViewModel"
    }

    private val essayListener = object : SimpleRepositoryDataChangeListener<Essay>() {
        override fun onDelete(data: Essay?) {
            super.onDelete(data)
            reLoad()
        }

        override fun onDeleteCollection(dataCollection: Collection<Essay>) {
            super.onDeleteCollection(dataCollection)
            reLoad()
        }

        override fun onUpdate(date: Essay?) {
            super.onUpdate(date)
            reLoad()
        }

        override fun onInsert(data: Essay?) {
            super.onInsert(data)
            reLoad()
        }
    }.also { wordRepository.addEssayChangeListener(it) }

    private val selectedChapter = MutableLiveData<Chapter>()

    var wordList = Transformations.switchMap(selectedChapter) { chapter ->
        liveData(Dispatchers.IO) {
            if (chapter != null) {
                val chapterId = chapter.id
                val unlockCount = chapter.unlockedCount
                val wordList = wordRepository.getWordList(chapterId, unlockCount, true)
                emit(wordList)
            }
        }
    }

    val unlockedPercent = Transformations.switchMap(selectedChapter) { chapter ->
        liveData {
            val unlockCount = chapter.unlockedCount
            val almost = wordRepository.countWordByChapterId(chapter.id)
            val percent = unlockCount.div(almost.toFloat()).times(100).toInt()
            emit(percent)
        }
    }

    fun setSelectedChapter(chapter: Chapter?) {
        selectedChapter.value = chapter
    }

    override fun onCleared() {
        super.onCleared()
        wordRepository.removeEssayChangeListener(essayListener)
    }

    fun reLoad() {
        selectedChapter.postValue(selectedChapter.value)
    }
}