package cn.tyhyh.easeword.data.repository

import android.util.Log
import cn.tyhyh.easeword.data.db.EssayDao
import cn.tyhyh.easeword.data.db.WordDao
import cn.tyhyh.easeword.data.entity.Essay
import cn.tyhyh.easeword.data.entity.Word

class WordRepository private constructor(
    private val wordDao: WordDao,
    private val essayDao: EssayDao
) {

    private val essayListeners = ArrayList<OnRepositoryDataChangeListener<Essay>>()

    fun addEssayChangeListener(listener: OnRepositoryDataChangeListener<Essay>) {
        essayListeners.add(listener)
    }

    fun removeEssayChangeListener(listener: OnRepositoryDataChangeListener<Essay>) {
        essayListeners.remove(listener)
    }

    fun getWordList(chapterId: Long, count: Int = -1, isEager: Boolean = false) =
        wordDao.getWordList(chapterId, count, isEager)

    fun getWordById(wordId: Long, isEager: Boolean = false) = wordDao.getWord(wordId, isEager)

    fun getEssayById(essayId: Long, isEager: Boolean = false) = essayDao.getEssayById(essayId, isEager)

    fun getEssayListByWordId(wordId: Long, isEager: Boolean = false) = essayDao.getEssayListByWordId(wordId, isEager)

    fun saveOrUpdateEssay(essay: Essay?): Boolean {
        if (essay != null) {
            val isUpdate = essay.isSaved
            return essayDao.saveOrUpdateEssay(essay).also {
                essayListeners.forEach { if (isUpdate) it.onUpdate(essay) else it.onInsert(essay) }
            }
        }
        return false
    }

    fun getMaxWordIdInLimit(chapterId: Long, limit: Int): Long {
        return wordDao.getMaxWordIdInLimit(chapterId, limit)
    }

    fun countWordByChapterId(chapterId: Long) = wordDao.countWordByChapterId(chapterId)

    fun deleteEssay(essay: Essay?) {
        if (essay != null) {
            essayDao.deleteEssay(essay)
            essayListeners.forEach { it.onDelete(essay) }
        }
    }

    fun deleteEssays(essays: Collection<Essay>) {
        essayDao.deleteEssays(essays)
        essayListeners.forEach { it.onDeleteCollection(essays) }
    }

    fun findWordByText(word: String, isEager: Boolean = false) = wordDao.findWordListByText(word, isEager)

    fun findWordByWordOrContent(content: String, isEager: Boolean = false): List<Word> {
        val wordIdArray = findWordIdArrayByContent(content).also { Log.d(TAG, it.contentToString()) }
        val distinctWordIds = wordIdArray.distinct()
        val wordList = ArrayList<Word>()
        distinctWordIds.forEach {
            val word = wordDao.getWord(it, isEager)
            if (word != null) {
                wordList.add(word)
            }
        }
        return wordList
    }

    fun findWordIdArrayByContent(content: String) = essayDao.findWordIdArrayByContent(content)

    companion object {

        private const val TAG = "WordRepository"

        private lateinit var instance: WordRepository

        fun getInstance(wordDao: WordDao, essayDao: EssayDao): WordRepository {
            if (!this::instance.isInitialized) {
                synchronized(WordRepository::class.java) {
                    if (!this::instance.isInitialized) {
                        instance = WordRepository(wordDao, essayDao)
                    }
                }
            }
            return instance
        }
    }
}