package cn.tyhyh.easeword.ui.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.MutableLiveData
import androidx.test.platform.app.InstrumentationRegistry
import cn.tyhyh.easeword.data.db.EaseWordDB
import cn.tyhyh.easeword.data.entity.Chapter
import cn.tyhyh.easeword.data.entity.Essay
import cn.tyhyh.easeword.data.repository.ChapterRepository
import cn.tyhyh.easeword.data.repository.WordRepository
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.litepal.LitePal

/**
 * author: tiny
 * created on: 20-4-4 下午8:10
 */
class MainViewModelTest {

    companion object {
        private val TAG = MainViewModelTest::class.simpleName!!
    }

    private lateinit var chapterRepository: ChapterRepository

    private lateinit var wordRepository: WordRepository

    private lateinit var mainViewModel: MainViewModel

    private lateinit var selectChapter: MutableLiveData<Chapter>

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    private lateinit var essay: Essay

    @Before
    fun setUp() {
        val context = InstrumentationRegistry.getInstrumentation().targetContext
        LitePal.initialize(context)
        wordRepository = WordRepository.getInstance(EaseWordDB.getWordDao(), EaseWordDB.getEssayDao())
        chapterRepository = ChapterRepository.getInstance(EaseWordDB.getDataDao(), EaseWordDB.getChapterDao())
        mainViewModel = MainViewModel(chapterRepository, wordRepository)
        selectChapter = mainViewModel.selectChapter
        essay = Essay(null, Essay.TYPE_NOTE, "hello")
    }

    @Test
    fun mainActivityTest() {
        val selectedChapter = mainViewModel.selectChapter
        for (i in 1..32L) {
            val chapter = chapterRepository.getChapterById(i)
            selectedChapter.value = chapter

            var unlockedChapterCount = chapter.unlockedCount
            val count = wordRepository.countWordByChapterId(chapter.id)
            var maxWordId = wordRepository.getMaxWordIdInLimit(chapter.id, unlockedChapterCount)
            var word = wordRepository.getWordById(maxWordId)

            for (j in unlockedChapterCount..count) {

                val before = unlockedChapterCount

                wordRepository.saveOrUpdateEssay(essay.also {
                    it.word = word
                    it.clearSavedState() // 清除保存状态，不清楚将不会存入数据库
                })

                unlockedChapterCount = chapter.unlockedCount

                assertTrue(
                    "$TAG: essay保存后MainViewModel未自动解锁下一个Word",
                    unlockedChapterCount > before
                )

                maxWordId = wordRepository.getMaxWordIdInLimit(chapter.id, unlockedChapterCount)
                word = wordRepository.getWordById(maxWordId)
            }
        }
    }
}