package cn.tyhyh.easeword.data.db

import cn.tyhyh.easeword.data.entity.Word
import org.litepal.LitePal
import org.litepal.extension.count
import org.litepal.extension.find
import org.litepal.extension.findLast

class WordDao {

    fun getWordList(chapterId: Long, count: Int, isEager: Boolean): List<Word> {
        return if (count == -1) {
            LitePal.where("chapter_id=?", chapterId.toString()).find(isEager)
        } else {
            LitePal.limit(count).where("chapter_id=?", chapterId.toString()).find(isEager)
        }
    }

    fun getMaxWordIdInLimit(chapterId: Long, limit: Int) =
        LitePal.where("chapter_id=?", chapterId.toString()).limit(limit)
            .findLast<Word>()?.id ?: EaseWordDB.INVALID_ID

    fun countWordByChapterId(chapterId: Long) =
        LitePal.where("chapter_id=?", chapterId.toString()).count<Word>()

    fun saveWord(word: Word?) {
        word?.save()
    }

    fun getWord(wordId: Long, isEager: Boolean) = LitePal.find<Word>(wordId, isEager)

    fun findWordListByText(word: String, isEager: Boolean) =
        LitePal.where("text=?", word).find<Word>(isEager)

    fun saveWordList(essayList: List<Word>?) {
        if (essayList != null && essayList.isNotEmpty()) {
            LitePal.saveAll(essayList)
        }
    }
}