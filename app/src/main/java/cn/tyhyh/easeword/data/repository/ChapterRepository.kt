package cn.tyhyh.easeword.data.repository

import cn.tyhyh.easeword.data.db.ChapterDao
import cn.tyhyh.easeword.data.db.DataDao
import cn.tyhyh.easeword.data.entity.Chapter
import cn.tyhyh.easeword.data.entity.Data

class ChapterRepository private constructor(
    private val dataDao: DataDao,
    private val chapterDao: ChapterDao
) {
    private var currentData: Data? = null

    private val chapterListeners = ArrayList<OnRepositoryDataChangeListener<Chapter>>()

    fun addChapterChangeListener(listener: OnRepositoryDataChangeListener<Chapter>) {
        chapterListeners.add(listener)
    }

    fun removeChapterChangeListener(listener: OnRepositoryDataChangeListener<Chapter>) {
        chapterListeners.remove(listener)
    }

    fun getUnlockChapterCount(): Int {
        return getData().unlockedCount
    }

    fun getMaxChapterIdInLimit(dataId: Long, limit: Int) = chapterDao.getMaxChapterIdInLimit(dataId, limit)

    private fun getData(): Data {
        if (currentData == null) {
            currentData = dataDao.getCurrentData()
        }
        return currentData!!
    }

    fun getChapterById(chapterId: Long) = chapterDao.getChapterById(chapterId)

    fun getChapterListByDataId(dataId: Long, isEager: Boolean = false) =
        chapterDao.getChapterList(dataId, isEager)

    fun getCurrentData() = dataDao.getCurrentData()

    fun saveLastSelectedChapterId(chapterId: Long) {
        val unlockedChapterCount = getUnlockChapterCount()
        if (chapterId > unlockedChapterCount) {
            return
        }
        chapterDao.saveLastSelectedChapterId(chapterId)
    }

    fun saveChapter(chapter: Chapter?) {
        if (chapter != null) {
            val isUpdate = chapter.isSaved
            chapterDao.saveChapter(chapter)
            chapterListeners.forEach {
                if (isUpdate) {
                    it.onUpdate(chapter)
                } else {
                    it.onInsert(chapter)
                }
            }
        }
    }

    fun saveOrUpdateChapter(chapter: Chapter?, vararg conditions: String) =
        chapterDao.saveOrUpdateChapter(chapter, *conditions)

    fun updateChapter(chapter: Chapter?) {
        chapterDao.updateChapter(chapter)
        chapterListeners.forEach { it.onUpdate(chapter) }
    }

    fun getLastSelectedChapterId(): Long {
        return chapterDao.getLastSelectedChapterId()
    }

    fun updateData(data: Data?) = dataDao.updateData(data)

    fun countChapterByWordId(wordId: Long) = chapterDao.countChapterByDataId(wordId)

    companion object {

        private lateinit var instance: ChapterRepository

        fun getInstance(dataDao: DataDao, chapterDao: ChapterDao): ChapterRepository {
            if (!this::instance.isInitialized) {
                synchronized(ChapterRepository::class.java) {
                    if (!this::instance.isInitialized) {
                        instance = ChapterRepository(dataDao, chapterDao)
                    }
                }
            }
            return instance
        }
    }
}