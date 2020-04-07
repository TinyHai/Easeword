package cn.tyhyh.easeword.data.db

import android.util.Log
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import org.junit.Test
import org.junit.runner.RunWith
import org.litepal.LitePal

@RunWith(AndroidJUnit4::class)
class DataDaoTest {

    companion object {
        private const val TAG = "DataDaoTest"
    }

    private val sourceFile: String = "data.txt"

    private val dataDao = EaseWordDB.getDataDao()

    private val chapterDao = EaseWordDB.getChapterDao()

    private val wordDao = EaseWordDB.getWordDao()

    private val essayDao = EaseWordDB.getEssayDao()

    init {
        val context = InstrumentationRegistry.getInstrumentation().targetContext
        LitePal.initialize(context)
    }

    @Test
    fun test() {
        init()
        saveEssay()
        getEssay()
    }

    private fun getDataBySourceFile() {
        try {
            val data = dataDao.getCurrentData().toString()
            val chapters = chapterDao.getChapterById(1)
            val words = wordDao.getWordList(1, -1, false)
            Log.d(TAG, "hello")
            Log.d(TAG, data)
            Log.d(TAG, chapters.toString())
            Log.d(TAG, words.toString())
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun saveEssay() {
    }

    private fun getEssay() {
        val essay = essayDao.getEssayById(1, true)
        Log.d(TAG, essay.toString())
    }

    private fun getWord() {
        val word = wordDao.getWord(1, true)
        Log.d(TAG, word.toString())
    }

    private fun init() {
        EaseWordDB.initFromAsset()
    }
}