package cn.tyhyh.easeword.data.db

import android.content.SharedPreferences
import androidx.preference.PreferenceManager
import cn.tyhyh.easeword.EaseWordApplication

object EaseWordDB {

    internal const val INVALID_ID = -1L

    internal const val DEFAULT_DATA_SOURCE_FILE = "data.txt"

    private lateinit var dataDao: DataDao

    private lateinit var chapterDao: ChapterDao

    private lateinit var wordDao: WordDao

    private lateinit var essayDao: EssayDao

    private const val DATA_INITIALIZED = "data_initialized"

    private val isInit = PreferenceManager.getDefaultSharedPreferences(
        EaseWordApplication.getApplication()
    ).getBoolean(DATA_INITIALIZED, false)

    fun initFromAsset() {
        if (isInit) {
            return
        }
        getDataDao().loadDataFromSourceFile(DEFAULT_DATA_SOURCE_FILE)
        PreferenceManager.getDefaultSharedPreferences(EaseWordApplication.getApplication()).edit {
            putBoolean(DATA_INITIALIZED, true)
        }
    }

    fun getDataDao(): DataDao {
        if (!this::dataDao.isInitialized) {
            dataDao = DataDao()
        }
        return dataDao
    }

    fun getChapterDao(): ChapterDao {
        if (!this::chapterDao.isInitialized) {
            chapterDao = ChapterDao()
        }
        return chapterDao
    }

    fun getWordDao(): WordDao {
        if (!this::wordDao.isInitialized) {
            wordDao = WordDao()
        }
        return wordDao
    }

    fun getEssayDao(): EssayDao {
        if (!this::essayDao.isInitialized) {
            essayDao = EssayDao()
        }
        return essayDao
    }

    private fun SharedPreferences.edit(block: SharedPreferences.Editor.() -> Unit) {
        val edit = edit()
        block(edit)
        edit.apply()
    }
}