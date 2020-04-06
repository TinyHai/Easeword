package cn.tyhyh.easeword.ui.activity

import android.content.Intent
import android.widget.LinearLayout
import android.widget.ScrollView
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.action.ViewActions.replaceText
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.rule.ActivityTestRule
import cn.tyhyh.easeword.R
import cn.tyhyh.easeword.util.childAtPosition
import org.junit.Assert.assertTrue
import org.hamcrest.Matchers.allOf
import org.hamcrest.Matchers.not
import org.hamcrest.core.IsInstanceOf
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

/**
 * author: tiny
 * created on: 20-4-6 下午5:48
 */
@RunWith(AndroidJUnit4::class)
class NoteActivityTest {

    @Rule
    @JvmField
    val mNoteActivityTestRule = ActivityTestRule(NoteActivity::class.java, false, false)

    @Before
    fun setUp() {
        val intent = Intent()
        intent.putExtra("word_id", 1L)
        mNoteActivityTestRule.launchActivity(intent)
    }

    @Test
    fun noteActivityTest() {
        val wordTv = onView(
            allOf(
                withId(R.id.wordTv),
                childAtPosition(
                    IsInstanceOf.instanceOf(LinearLayout::class.java),
                    1
                ),
                withText("天")
            )
        )
        wordTv.check(matches(isDisplayed()))

        val saveBtn = onView(
            allOf(
                withId(R.id.rightBtn),
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

        saveBtn.check(matches(not(isEnabled())))

        val contextEv = onView(
            allOf(
                withId(R.id.contentEv),
                childAtPosition(
                    allOf(
                        withId(R.id.noteWrapper),
                        IsInstanceOf.instanceOf(ScrollView::class.java)
                    ),
                    0
                )
            )
        )
        contextEv.perform(replaceText("hello"))

        saveBtn.check(matches(isEnabled()))

        saveBtn.perform(click())

        Thread.sleep(2000)

        assertTrue("after click saveBtn, activity has not finished", mNoteActivityTestRule.activity.isFinishing)
    }
}