package cn.tyhyh.easeword.ui.activity

import android.app.Activity
import android.widget.LinearLayout
import androidx.recyclerview.widget.RecyclerView
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.Espresso.pressBackUnconditionally
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import androidx.test.rule.ActivityTestRule
import cn.tyhyh.easeword.R
import cn.tyhyh.easeword.data.entity.Word
import cn.tyhyh.easeword.ui.adapter.CommonRcvListAdapter
import cn.tyhyh.easeword.util.childAtPosition
import cn.tyhyh.easeword.util.getCurrentActivity
import cn.tyhyh.easeword.util.withRecyclerView
import org.hamcrest.Matchers.allOf
import org.hamcrest.Matchers.not
import org.hamcrest.core.IsInstanceOf
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

/**
 * author: tiny
 * created on: 20-4-7 上午11:59
 */
@LargeTest
@RunWith(AndroidJUnit4::class)
class AllActivityForwardTest {

    @Rule
    @JvmField
    val mSplashActivityTestRule = ActivityTestRule(SplashActivity::class.java)

    @Test
    fun appTestStart() {
        Thread.sleep(2100) // Wait SplashActivity to finish
        assertTrue("SplashActivity is not finished", mSplashActivityTestRule.activity.isFinishing)
        mainActivityTestStart()
    }

    fun mainActivityTestStart() {
        runActivityTest<MainActivity> {
            val settingEntry = onView(
                allOf(
                    withId(R.id.leftBtn),
                    childAtPosition(
                        allOf(
                            withId(R.id.toolbarBinding),
                            childAtPosition(
                                IsInstanceOf.instanceOf(LinearLayout::class.java),
                                0
                            )
                        ),
                        0
                    ),
                    isDisplayed()
                )
            )
            settingEntry.perform(click())
            settingActivityTestStart()

            val chapterRecyclerView = onView(
                allOf(
                    withId(R.id.wordRecyclerView),
                    childAtPosition(
                        IsInstanceOf.instanceOf(LinearLayout::class.java),
                        1
                    ),
                    isDisplayed()
                )
            )

            val recyclerView = it.findViewById<RecyclerView>(R.id.wordRecyclerView)
            val adapter = recyclerView.adapter!!
            val wordText = onView(
                allOf(
                    withId(R.id.wordText),
                    childAtPosition(
                        withRecyclerView(R.id.wordRecyclerView)
                            .atPosition(adapter.itemCount - 1),
                        1
                    )
                )
            )
            chapterRecyclerView.perform(
                RecyclerViewActions.scrollToPosition<CommonRcvListAdapter.CommonViewHolder<Word>>(
                    adapter.itemCount - 1
                )
            )

            wordText.perform(click())
            detailActivityTestStart()

            val searchEntry = onView(
                allOf(
                    withId(R.id.leftBtn),
                    childAtPosition(
                        allOf(
                            withId(R.id.toolbarBinding),
                            childAtPosition(
                                IsInstanceOf.instanceOf(LinearLayout::class.java),
                                0
                            )
                        ),
                        2
                    ),
                    isDisplayed()
                )
            )
            searchEntry.perform(click())
            searchActivityTestStart()

            pressBackUnconditionally()
            pressBackUnconditionally()
        }
    }

    fun searchActivityTestStart() {
        runActivityTest<SearchActivity> {
            val backBtn = onView(
                allOf(
                    withId(R.id.leftBtn),
                    childAtPosition(
                        allOf(
                            withId(R.id.toolbarBinding),
                            childAtPosition(
                                IsInstanceOf.instanceOf(LinearLayout::class.java),
                                0
                            )
                        ),
                        0
                    ),
                    isDisplayed()
                )
            )
            backBtn.perform(click())
        }
    }

    fun detailActivityTestStart() {
        runActivityTest<DetailActivity> {
            val noteEntry = onView(
                withId(R.id.addNote)
            )
            val drawingEntry = onView(
                withId(R.id.addDrawing)
            )
            val addFab = onView(
                allOf(
                    withId(R.id.addEssayBtn),
                    isDisplayed()
                )
            )
            addFab.perform(click())
            noteEntry.perform(click())
            noteActivityTestStart()

            noteEntry.check(matches(not(isDisplayed())))
            drawingEntry.check(matches(not(isDisplayed())))

            addFab.perform(click())
            drawingEntry.perform(click())
            drawingActivityTestStart()

            noteEntry.check(matches(not(isDisplayed())))
            drawingEntry.check(matches(not(isDisplayed())))

            val backBtn = onView(
                allOf(
                    withId(R.id.leftBtn),
                    childAtPosition(
                        allOf(
                            withId(R.id.toolbarBinding),
                            childAtPosition(
                                IsInstanceOf.instanceOf(LinearLayout::class.java),
                                0
                            )
                        ),
                        0
                    ),
                    isDisplayed()
                )
            )
            backBtn.perform(click())
        }
    }

    fun noteActivityTestStart() {
        runActivityTest<NoteActivity> {
            val backBtn = onView(
                allOf(
                    withId(R.id.leftBtn),
                    childAtPosition(
                        allOf(
                            withId(R.id.toolbarBinding),
                            childAtPosition(
                                IsInstanceOf.instanceOf(LinearLayout::class.java),
                                0
                            )
                        ),
                        0
                    ),
                    isDisplayed()
                )
            )
            backBtn.perform(click())
        }
    }

    fun drawingActivityTestStart() {
        runActivityTest<DrawingActivity> {
            val backBtn = onView(
                allOf(
                    withId(R.id.leftBtn),
                    childAtPosition(
                        allOf(
                            withId(R.id.toolbarBinding),
                            childAtPosition(
                                IsInstanceOf.instanceOf(LinearLayout::class.java),
                                0
                            )
                        ),
                        0
                    ),
                    isDisplayed()
                )
            )
            backBtn.perform(click())
        }
    }

    fun settingActivityTestStart() {
        runActivityTest<SettingActivity> {
            val aboutEntry = onView(
                allOf(
                    withId(R.id.aboutUsBtn),
                    childAtPosition(
                        allOf(
                            IsInstanceOf.instanceOf(LinearLayout::class.java),
                            childAtPosition(
                                IsInstanceOf.instanceOf(LinearLayout::class.java),
                                4
                            )
                        ),
                        4
                    ),
                    isDisplayed()
                )
            )
            aboutEntry.perform(click())

            aboutActivityTestStart()

            val feedbackEntry = onView(
                allOf(
                    withId(R.id.feedbackBtn),
                    childAtPosition(
                        allOf(
                            IsInstanceOf.instanceOf(LinearLayout::class.java),
                            childAtPosition(
                                IsInstanceOf.instanceOf(LinearLayout::class.java),
                                4
                            )
                        ),
                        2
                    ),
                    isDisplayed()
                )
            )
            feedbackEntry.perform(click())

            feedbackActivityTestStart()

            val backBtn = onView(
                allOf(
                    withId(R.id.leftBtn),
                    childAtPosition(
                        allOf(
                            withId(R.id.toolbarBinding),
                            childAtPosition(
                                IsInstanceOf.instanceOf(LinearLayout::class.java),
                                0
                            )
                        ),
                        0
                    ),
                    isDisplayed()
                )
            )
            backBtn.perform(click())
        }
    }

    fun aboutActivityTestStart() {
        runActivityTest<AboutActivity> {
            val backBtn = onView(
                allOf(
                    withId(R.id.leftBtn),
                    childAtPosition(
                        allOf(
                            withId(R.id.toolbarBinding),
                            childAtPosition(
                                IsInstanceOf.instanceOf(LinearLayout::class.java),
                                0
                            )
                        ),
                        0
                    ),
                    isDisplayed()
                )
            )
            backBtn.perform(click())
        }
    }

    fun feedbackActivityTestStart() {
        runActivityTest<FeedbackActivity> {
            val backBtn = onView(
                allOf(
                    withId(R.id.leftBtn),
                    childAtPosition(
                        allOf(
                            withId(R.id.toolbarBinding),
                            childAtPosition(
                                IsInstanceOf.instanceOf(LinearLayout::class.java),
                                0
                            )
                        ),
                        0
                    ),
                    isDisplayed()
                )
            )
            backBtn.perform(click())
        }
    }

    private inline fun <reified T : Activity> runActivityTest(block: (currentActivity: T) -> Unit) {
        val classSimpleName = T::class.java.simpleName
        val currentActivity = getCurrentActivity()
        assertTrue("cuurentActivity is not $classSimpleName", currentActivity is T)
        block(currentActivity as T)
        assertTrue("$classSimpleName is not finished", currentActivity.isFinishing)
    }
}