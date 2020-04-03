package cn.tyhyh.easeword.data.db

import android.content.SharedPreferences
import androidx.preference.PreferenceManager
import cn.tyhyh.easeword.EaseWordApplication
import cn.tyhyh.easeword.data.entity.Chapter
import org.litepal.LitePal
import org.litepal.extension.count
import org.litepal.extension.find
import org.litepal.extension.findLast

class ChapterDao {

    companion object {

        private const val LAST_CHAPTER_ID = "last_chapter_id"
    }

    fun getChapterList(dataId: Long, isEager: Boolean) =
        LitePal.where("data_id=?", dataId.toString()).find<Chapter>(isEager)

    fun saveChapter(chapter: Chapter?) {
        chapter?.save()
    }

    fun saveOrUpdateChapter(chapter: Chapter?, vararg conditions: String) {
        chapter?.saveOrUpdate(*conditions)
    }

    fun saveChapterList(chapterList: List<Chapter>?) {
        if (chapterList != null && chapterList.isNotEmpty()) {
            LitePal.saveAll(chapterList)
        }
    }

    fun updateChapter(chapter: Chapter?) {
        chapter?.update(chapter.id)
    }

    fun saveLastSelectedChapterId(chapterId: Long) {
        PreferenceManager.getDefaultSharedPreferences(EaseWordApplication.getApplication()).edit {
            putLong(LAST_CHAPTER_ID, chapterId)
        }
    }

    fun getChapterById(chapterId: Long) = LitePal.find(Chapter::class.java, chapterId)

    fun countChapterByDataId(dataId: Long) =
        LitePal.where("data_id=?", dataId.toString()).count<Chapter>()

    fun getLastSelectedChapterId() = PreferenceManager.getDefaultSharedPreferences(
            EaseWordApplication.getApplication()).getLong(LAST_CHAPTER_ID, 1L)

    fun getMaxChapterIdInLimit(dataId: Long, limit: Int) =
        LitePal.where("data_id=?", dataId.toString()).limit(limit)
            .findLast<Chapter>()?.id ?: EaseWordDB.INVALID_ID

    private fun SharedPreferences.edit(block: SharedPreferences.Editor.() -> Unit) {
        val edit = edit()
        block(edit)
        edit.apply()
    }
}