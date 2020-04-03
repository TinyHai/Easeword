package cn.tyhyh.easeword.data.db

import cn.tyhyh.easeword.data.entity.Essay
import org.litepal.LitePal
import org.litepal.extension.find

class EssayDao {

    fun saveEssay(essay: Essay?) {
        essay?.save()
    }

    fun saveOrUpdateEssay(essay: Essay?): Boolean {
        return essay?.save() ?: false
    }

    fun deleteEssay(essay: Essay?) {
        essay?.delete()
    }

    fun deleteEssays(essays: Collection<Essay>) {
        essays.forEach { it.delete() }
    }

    fun saveEssayList(essayList: List<Essay>?) {
        if (essayList != null && essayList.isNotEmpty()) {
            LitePal.saveAll(essayList)
        }
    }

    fun getEssayListByWordId(wordId: Long, isEager: Boolean) =
        LitePal.where("word_id=?", wordId.toString()).find<Essay>(isEager)

    fun getEssayById(id: Long, isEager: Boolean) = LitePal.find<Essay>(id, isEager)

    fun findWordIdArrayByContent(content: String) =
        LitePal.findBySQL(
            """
            SELECT word_id
            FROM ${Essay::class.java.simpleName}
            WHERE type=? and content like ?
        """.trimIndent(),
            Essay.TYPE_NOTE.toString(),
            "%$content%"
        ).let {
            it.moveToFirst()
            val result = LongArray(it.count)
            var count = 0
            if (it.count > 0) {
                do {
                    result[count++] = it.getLong(it.getColumnIndex("word_id"))
                } while (it.moveToNext())
            }
            result
        }

}