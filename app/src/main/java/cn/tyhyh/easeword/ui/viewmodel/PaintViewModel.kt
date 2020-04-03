package cn.tyhyh.easeword.ui.viewmodel

import android.app.Application
import android.graphics.Bitmap
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import cn.tyhyh.easeword.EaseWordApplication
import cn.tyhyh.easeword.data.db.EaseWordDB
import cn.tyhyh.easeword.data.entity.Essay
import cn.tyhyh.easeword.data.entity.Word
import cn.tyhyh.easeword.data.repository.WordRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileOutputStream
import java.text.SimpleDateFormat
import java.util.*

/**
 * author: tiny
 * created on: 20-3-23 下午9:16
 */
class PaintViewModel(
    private val wordRepository: WordRepository,
    application: Application
) : AndroidViewModel(application) {

    companion object {
        private const val PNG = ".png"
        private const val TAG = "PaintViewModel"
    }

    val essay = MutableLiveData<Essay>()

    val saving = MutableLiveData<Boolean>()

    val loading = MutableLiveData<Boolean>()

    val word = MutableLiveData<Word>()

    private suspend fun saveDrawingPath(drawingPath: String?): Boolean {
        if (drawingPath.isNullOrEmpty()) {
            return false
        }
        val word = word.value!!

        val newEssay = essay.value.also {
            if (it != null) {
                it.word = word
                val oldContent = it.content
                it.content = drawingPath
                try {
                    val oldFile = File(oldContent)
                    if (oldFile.exists()) {
                        oldFile.delete()
                    }
                } catch (e: Exception) {
                    Log.d(TAG, "delete file = $oldContent failed")
                    e.printStackTrace()
                }
            }
        } ?: Essay(word, Essay.TYPE_DRAWING, drawingPath)

        val result = saveOrUpdateEssay(newEssay)

        if (result) {
            essay.postValue(newEssay)
        }

        return result
    }

    private suspend fun saveOrUpdateEssay(essay: Essay?) =
        withContext(Dispatchers.IO) {
            wordRepository.saveOrUpdateEssay(essay)
        }

    fun saveDrawing(bitmap: Bitmap) {
        saving.value = true
        viewModelScope.launch(Dispatchers.IO) {
            val simpleDateFormat = SimpleDateFormat("-yyyy-MM-dd-HH-mm-ss-SSS", Locale.SIMPLIFIED_CHINESE)
            val fileName = (word.value?.text ?: "") + simpleDateFormat.format(Date()) + PNG
            val directoryFile = getApplication<EaseWordApplication>().externalCacheDir!!
            val imageFile = File(directoryFile.absolutePath, fileName)

            var saved = true

            try {
                val outputStream = FileOutputStream(imageFile)
                outputStream.use {
                    bitmap.compress(Bitmap.CompressFormat.PNG, 100, it)
                }
            } catch (e: Exception) {
                e.printStackTrace()
                saved = false
            }

            withContext(Dispatchers.Main) {
                saving.value = if (saved && saveDrawingPath(imageFile.absolutePath)) false else null
            }
        }
    }

    fun setWordIdAndEssayId(wordId: Long, essayId: Long) {
        loading.value = true
        val getEssayJob =  if (essayId != EaseWordDB.INVALID_ID) {
            viewModelScope.launch(Dispatchers.IO) {
                val essay = wordRepository.getEssayById(essayId)
                if (essay != null) {
                    this@PaintViewModel.essay.postValue(essay)
                }
            }
        } else null
        viewModelScope.launch(Dispatchers.IO) {
            val word = wordRepository.getWordById(wordId)
            if (word != null) {
                this@PaintViewModel.word.postValue(word)
                getEssayJob?.join()
                essay.value?.word = word
            }
            loading.postValue(false)
        }
    }

    val checkedPaintSizeRb by lazy {
        MutableLiveData<Int>()
    }

    fun setCheckedPaintSizeRb(id: Int) {
        checkedPaintSizeRb.value = id
    }
}