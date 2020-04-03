package cn.tyhyh.easeword.util

import cn.tyhyh.easeword.EaseWordApplication
import cn.tyhyh.easeword.data.db.EaseWordDB
import cn.tyhyh.easeword.data.repository.ChapterRepository
import cn.tyhyh.easeword.data.repository.WordRepository
import cn.tyhyh.easeword.ui.modelfactory.*

object InjectorUtil {

    private fun getChapterRepository() = ChapterRepository.getInstance(EaseWordDB.getDataDao(), EaseWordDB.getChapterDao())

    private fun getWordRepository() = WordRepository.getInstance(EaseWordDB.getWordDao(), EaseWordDB.getEssayDao())

    private fun getApplication() = EaseWordApplication.getApplication()

    fun getMainModelFactory() = MainModelFactory(getChapterRepository(), getWordRepository())

    fun getMainFragModelFactory() = MainFragModelFactory(getWordRepository())

    fun getFeedbackModelFactory() = FeedbackModelFactory()

    fun getPaintModelFactory() = PaintModelFactory(getWordRepository(), getApplication())

    fun getDetailModelFactory() = DetailModelFactory(getWordRepository())

    fun getNoteModelFactory() = NoteModelFactory(getWordRepository())

    fun getSearchModelFactory() = SearchModelFactory(getChapterRepository(), getWordRepository())
}