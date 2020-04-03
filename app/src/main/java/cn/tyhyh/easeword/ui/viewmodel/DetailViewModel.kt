package cn.tyhyh.easeword.ui.viewmodel

import androidx.lifecycle.*
import cn.tyhyh.easeword.data.db.EaseWordDB
import cn.tyhyh.easeword.data.entity.Essay
import cn.tyhyh.easeword.data.repository.SimpleRepositoryDataChangeListener
import cn.tyhyh.easeword.data.repository.WordRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

/**
 * author: tiny
 * created on: 20-3-25 上午8:42
 */
class DetailViewModel(
    private val wordRepository: WordRepository
) : ViewModel() {

    private val wordId = MutableLiveData<Long>()

    private val essayListener = object : SimpleRepositoryDataChangeListener<Essay>() {
        override fun onInsert(data: Essay?) {
            super.onInsert(data)
            reLoad()
        }

        override fun onUpdate(date: Essay?) {
            super.onUpdate(date)
            reLoad()
        }

        override fun onDeleteCollection(dataCollection: Collection<Essay>) {
            super.onDeleteCollection(dataCollection)
            reLoad()
        }
    }.also { wordRepository.addEssayChangeListener(it) }

    val showAddFab = MutableLiveData<Boolean>()

    val word = Transformations.switchMap(wordId) { id ->
        liveData(Dispatchers.IO) {
            val result = wordRepository.getWordById(id, true)
            if (result != null) {
                emit(result)
            }
        }
    } as MutableLiveData

    fun deleteEssays(essays: Collection<Essay>) {
        if (essays.isEmpty()) {
            return
        }
        viewModelScope.launch(Dispatchers.IO) {
            wordRepository.deleteEssays(essays)
        }
    }

    fun hideTwoFab() {
        showAddFab.value = false
    }

    fun showTwoFab() {
        showAddFab.value = true
    }

    fun contraryFabStatus() {
        showAddFab.value = showAddFab.value?.not() ?: true
    }

    val essayList = Transformations.map(word) { w ->
        w.essays
    }

    fun setWordId(id: Long) {
        wordId.value = id
    }

    fun getWordId() = wordId.value ?: EaseWordDB.INVALID_ID

    fun reLoad() {
        wordId.postValue(wordId.value)
    }

    override fun onCleared() {
        super.onCleared()
        wordRepository.removeEssayChangeListener(essayListener)
    }
}