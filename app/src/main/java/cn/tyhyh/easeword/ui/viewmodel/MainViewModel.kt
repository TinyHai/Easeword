package cn.tyhyh.easeword.ui.viewmodel

import androidx.lifecycle.*
import cn.tyhyh.easeword.data.entity.Chapter
import cn.tyhyh.easeword.data.entity.Data
import cn.tyhyh.easeword.data.entity.Essay
import cn.tyhyh.easeword.data.repository.ChapterRepository
import cn.tyhyh.easeword.data.repository.SimpleRepositoryDataChangeListener
import cn.tyhyh.easeword.data.repository.WordRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MainViewModel(
    private val chapterRepository: ChapterRepository,
    private val wordRepository: WordRepository
) : ViewModel(), Observer<Data> {

    companion object {
        private const val TAG = "MainViewModel"
    }

    private val essayListener = object : SimpleRepositoryDataChangeListener<Essay>() {
        override fun onInsert(data: Essay?) {
            if (data == null) {
                return
            }
            val word = data.word ?: return
            unlockNextChapterIfNeed()
            unlockNextWordIfNeed(word.id)
        }
    }.also { wordRepository.addEssayChangeListener(it) }

    private val data = liveData(Dispatchers.IO) {
        val result = chapterRepository.getCurrentData()
        emit(result)
    }.also { it.observeForever(this) } as MutableLiveData

    private var unlockedChapterCount: Int = 0

    val isCatalogRollUp = MutableLiveData<Boolean>()

    val isRefreshing = MutableLiveData<Boolean>()

    val chapterList = MutableLiveData<List<Chapter>>()

    val selectChapter = MutableLiveData<Chapter>()

    private fun loadDataInOrder(data: Data) = viewModelScope.launch(Dispatchers.IO) {
        val refreshing = isRefreshing.value ?: false
        if (!refreshing) {
            return@launch
        }
        unlockedChapterCount = data.unlockedCount
        val chapters = chapterRepository.getChapterListByDataId(data.id)
        chapterList.postValue(chapters)
        val selectedChapterId = chapterRepository.getLastSelectedChapterId()
        val selectedChapter = chapters.find { it.id == selectedChapterId }
        if (selectedChapter != null) {
            selectChapter.postValue(selectedChapter)
        }
        isRefreshing.postValue(false)
    }

    fun reverseCatalogStatus() {
        isCatalogRollUp.value = isCatalogRollUp.value?.not() ?: true
    }

    fun setSelectedChapter(chapter: Chapter) {
        selectChapter.value = chapter
    }

    fun saveLastSelectedChapterId(chapter: Chapter?) {
        val chapterId = chapter?.id ?: return
        chapterRepository.saveLastSelectedChapterId(chapterId)
    }

    fun getUnlockChapterCount() = unlockedChapterCount

    @Suppress("SameParameterValue")
    private fun unlockNextWordIfNeed(updatedWordId: Long) {
        val selectedChapter = selectChapter.value
        val unlockedCount = selectedChapter?.unlockedCount ?: return
        val maxUnlockedWordId = wordRepository.getMaxWordIdInLimit(selectedChapter.id, unlockedCount)
        if (maxUnlockedWordId == updatedWordId) {
            selectedChapter.unlockedCount++
            chapterRepository.updateChapter(selectedChapter)
        }
    }

    private fun unlockNextChapterIfNeed() {
        val data = data.value ?: return
        val currentSelectedChapter = selectChapter.value ?: return
        val chapterId = currentSelectedChapter.id
        val unlockedWordCount = currentSelectedChapter.unlockedCount
        val totalWord = wordRepository.countWordByChapterId(chapterId)

        if (unlockedWordCount == totalWord) {
            val unlockedChapterCount = data.unlockedCount
            val totalChapter = chapterRepository.countChapterByWordId(data.id)
            if (totalChapter == unlockedChapterCount) {
                return
            }
            val maxUnlockedChapterId = chapterRepository.getMaxChapterIdInLimit(data.id, unlockedChapterCount)
            if (maxUnlockedChapterId == chapterId) {
                data.unlockedCount++
                chapterRepository.updateData(data)
            }
        }
    }

    override fun onCleared() {
        saveLastSelectedChapterId(selectChapter.value)
        data.removeObserver(this)
        wordRepository.removeEssayChangeListener(essayListener)
    }

    fun load() {
        val data = data.value ?: return
        loadDataInOrder(data)
    }

    fun reLoad() {
        isRefreshing.postValue(true)
    }

    override fun onChanged(t: Data?) {
        if (t != null) {
            isRefreshing.value = true
        }
    }
}
