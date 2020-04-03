package cn.tyhyh.easeword.ui.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import cn.tyhyh.easeword.data.db.EaseWordDB
import cn.tyhyh.easeword.data.entity.Essay
import cn.tyhyh.easeword.data.entity.Word
import cn.tyhyh.easeword.data.repository.WordRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

/**
 * author: tiny
 * created on: 20-3-31 下午3:32
 */
class NoteViewModel(
    private val wordRepository: WordRepository
) : ViewModel() {

    companion object {
        private const val TAG = "NoteViewModel"
    }

    val saving = MutableLiveData<Boolean?>()

    val noteContent = MutableLiveData<String>()

    val loading = MutableLiveData<Boolean>()

    val essay = MutableLiveData<Essay>()

    val word = MutableLiveData<Word>()

    fun saveNote(): Boolean {
        val note = noteContent.value
        if (note.isNullOrEmpty() || note.isBlank()) {
            return false
        }

        val word = word.value!!

        val newEssay = essay.value.also {
            if (it != null) {
                it.word = word
                when (it.content) {
                    note -> return false
                    else -> {
                        it.content = note
                    }
                }
            }
        } ?: Essay(word, Essay.TYPE_NOTE, note)

        saving.value = true

        viewModelScope.launch(Dispatchers.IO) {
            val result = wordRepository.saveOrUpdateEssay(newEssay)
            essay.postValue(newEssay)
            saving.postValue(if (result) false else null)
        }

        return true
    }

    fun setWordIdAndEssayId(wordId: Long, essayId: Long) {
        loading.value = true
        val getEssayJob =  if (essayId != EaseWordDB.INVALID_ID) {
            viewModelScope.launch(Dispatchers.IO) {
                val essay = wordRepository.getEssayById(essayId)
                if (essay != null) {
                    this@NoteViewModel.essay.postValue(essay)
                }
            }
        } else null
        viewModelScope.launch(Dispatchers.IO) {
            val word = wordRepository.getWordById(wordId)
            if (word != null) {
                this@NoteViewModel.word.postValue(word)
                getEssayJob?.join()
                essay.value?.word = word
            }
            loading.postValue(false)
        }
    }
}