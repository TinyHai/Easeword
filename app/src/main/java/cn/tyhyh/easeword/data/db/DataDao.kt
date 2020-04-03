package cn.tyhyh.easeword.data.db

import android.content.SharedPreferences
import androidx.preference.PreferenceManager
import cn.tyhyh.easeword.EaseWordApplication
import cn.tyhyh.easeword.data.entity.Chapter
import cn.tyhyh.easeword.data.entity.Data
import cn.tyhyh.easeword.data.entity.Word
import org.litepal.LitePal
import org.litepal.extension.findFirst
import java.io.*

class DataDao {

    companion object {
        private const val CURRENT_SOURCE_FILE = "current_source_file"
    }

    private var currentSourceFile = PreferenceManager.getDefaultSharedPreferences(
        EaseWordApplication.getApplication()
    ).getString(CURRENT_SOURCE_FILE, null) ?: EaseWordDB.DEFAULT_DATA_SOURCE_FILE

    private val defaultParser: (sourceFile: String) -> Data = { sourceFile ->

        val context = EaseWordApplication.getApplication()
        val dataBufferedReader = try {
            context.assets.open(sourceFile).bufferedReader()
        } catch (e: IOException) {
            val file = File(sourceFile)
            if (file.exists()) {
                BufferedReader(InputStreamReader(FileInputStream(file)))
            } else {
                throw IllegalArgumentException("sourceFile($sourceFile) is not an exist file")
            }
        }

        val chapters = ArrayList<Chapter>()
        val data = Data(sourceFile, 1, chapters)

        dataBufferedReader.use { reader ->
            var line: String? = null
            var isNewChapter = true
            var preview = ""

            var words = ArrayList<Word>()

            var chapter = Chapter().also { it.data = data }

            while (true) {
                line = reader.readLine() ?: break
                line = line.trim()
                when {
                    line.isNotEmpty() -> {
                        if (isNewChapter) {
                            isNewChapter = false
                            preview = line
                        }
                        for (char in line) {
                            if (char.isWhitespace()) {
                                continue
                            }
                            words.add(Word(chapter, char.toString(), ArrayList(0)))
                        }
                    }
                    else -> {
                        chapter.preview = preview
                        chapter.words = words
                        chapters.add(chapter)

                        words = ArrayList()
                        chapter = Chapter().also { it.data = data }
                        isNewChapter = true
                        preview = ""
                    }
                }
            }

            if (preview.isNotEmpty() && words.isNotEmpty()) {
                chapter.preview = preview
                chapter.words = words
                chapters.add(chapter)
            }
        }

        data
    }

    fun getCurrentData(): Data {
        return getDataBySourceFile(currentSourceFile)
    }

    fun updateData(data: Data?) = data?.update(data.id) ?: false

    fun getDataBySourceFile(sourceFile: String) =
        LitePal.where("sourceFile=?", sourceFile).findFirst<Data>()!!

    fun loadDataFromSourceFile(
        sourceFile: String,
        parser: (sourceFile: String) -> Data = defaultParser
    ) {
        val data = parser(sourceFile)
        val chapters = data.chapters

        val essayDao = EaseWordDB.getEssayDao()
        val wordDao = EaseWordDB.getWordDao()
        val chapterDao = EaseWordDB.getChapterDao()

        for (chapter in chapters) {
            val words = chapter.words
            for (word in words) {
                val essays = word.essays
                essayDao.saveEssayList(essays)
            }
            wordDao.saveWordList(words)
        }
        chapterDao.saveChapterList(chapters)

        data.save()

        PreferenceManager.getDefaultSharedPreferences(EaseWordApplication.getApplication()).edit {
            putString(CURRENT_SOURCE_FILE, sourceFile)
        }

        currentSourceFile = sourceFile
    }

    private fun SharedPreferences.edit(block: SharedPreferences.Editor.() -> Unit) {
        val edit = edit()
        block(edit)
        edit.apply()
    }
}