package cn.tyhyh.easeword.ui.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import cn.tyhyh.easeword.data.entity.Chapter
import cn.tyhyh.easeword.data.entity.Essay
import cn.tyhyh.easeword.data.entity.Word
import cn.tyhyh.easeword.data.repository.ChapterRepository
import cn.tyhyh.easeword.data.repository.SimpleRepositoryDataChangeListener
import cn.tyhyh.easeword.data.repository.WordRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

/**
 * author: tiny
 * created on: 20-4-3 下午1:06
 */
class SearchViewModel(
    private val chapterRepository: ChapterRepository,
    private val wordRepository: WordRepository
) : ViewModel(), Observer<String> {

    private val essayListener = object : SimpleRepositoryDataChangeListener<Essay>() {
        override fun onUpdate(date: Essay?) {
            super.onUpdate(date)
            submitQuery(queryString.value)
        }

        override fun onDelete(data: Essay?) {
            super.onDelete(data)
            submitQuery(queryString.value)
        }

        override fun onInsert(data: Essay?) {
            super.onInsert(data)
            submitQuery(queryString.value)
        }

        override fun onDeleteCollection(dataCollection: Collection<Essay>) {
            super.onDeleteCollection(dataCollection)
            submitQuery(queryString.value)
        }
    }.also { wordRepository.addEssayChangeListener(it) }

    val queryResult by lazy {
        MutableLiveData<List<Word>>()
    }

    private val queryString = MutableLiveData<String>().also { it.observeForever(this) }

    private var lastQueryJob: Job? = null

    fun submitQuery(query: String?) {
        queryString.postValue(query)
    }

    override fun onCleared() {
        super.onCleared()
        queryString.removeObserver(this)
        wordRepository.removeEssayChangeListener(essayListener)
    }

    override fun onChanged(t: String?) {
        if (t.isNullOrEmpty() || t.isBlank()) {
            queryResult.postValue(emptyList())
            return
        }
        lastQueryJob?.cancel()
        lastQueryJob = viewModelScope.launch(Dispatchers.IO) {
            when (t.length) {
                1 -> {
                    val unlockedCount = chapterRepository.getUnlockChapterCount()
                    val allUnlockedWordList = ArrayList<Word>()
                    val unlockedChapters = Array<Chapter>(unlockedCount) {
                        val id = it.inc().toLong()
                        chapterRepository.getChapterById(id)
                    }
                    unlockedChapters.forEach {
                        val words = wordRepository.getWordList(it.id, it.unlockedCount, true)
                        allUnlockedWordList.addAll(words)
                    }
                    val result = allUnlockedWordList.filter { it.text == t }
                    queryResult.postValue(result)
                }
                else -> {
                    val result = wordRepository.findWordByWordOrContent(t, true)
                    queryResult.postValue(result)
                }
            }
            lastQueryJob = null
        }
    }
}