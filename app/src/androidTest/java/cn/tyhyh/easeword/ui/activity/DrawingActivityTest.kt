package cn.tyhyh.easeword.ui.activity

import android.content.Intent
import android.widget.LinearLayout
import android.widget.RadioGroup
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.action.ViewActions.swipeDown
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.rule.ActivityTestRule
import cn.tyhyh.easeword.R
import cn.tyhyh.easeword.util.childAtPosition
import org.hamcrest.Matchers.allOf
import org.hamcrest.Matchers.not
import org.hamcrest.core.IsInstanceOf
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

/**
 * author: tiny
 * created on: 20-4-7 上午11:07
 */
@RunWith(AndroidJUnit4::class)
class DrawingActivityTest {

    @Rule
    @JvmField
    val mDrawingActivityTestRule = ActivityTestRule(DrawingActivity::class.java, false, false)

    @Before
    fun setUp() {
        val intent = Intent()
        intent.putExtra("word_id", 1L)
        mDrawingActivityTestRule.launchActivity(intent)
    }

    @Test
    fun drawingActivityTest() {
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

        val undoBtn = onView(
            allOf(
                withId(R.id.undoBtn),
                childAtPosition(
                    allOf(
                        IsInstanceOf.instanceOf(LinearLayout::class.java),
                        childAtPosition(
                            IsInstanceOf.instanceOf(LinearLayout::class.java),
                            4
                        )
                    ),
                    0
                ),
                isDisplayed()
            )
        )
        undoBtn.check(matches(not(isEnabled())))

        val redoBtn = onView(
            allOf(
                withId(R.id.redoBtn),
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
        redoBtn.check(matches(not(isEnabled())))

        val paintSizeRadioButton = onView(
            allOf(
                withId(R.id.paintSmallRb),
                childAtPosition(
                    allOf(
                        withId(R.id.paintSizeRg),
                        IsInstanceOf.instanceOf(RadioGroup::class.java),
                        childAtPosition(
                            allOf(
                                IsInstanceOf.instanceOf(LinearLayout::class.java),
                                childAtPosition(
                                    IsInstanceOf.instanceOf(LinearLayout::class.java),
                                    4
                                )
                            ),
                            4
                        )
                    ),
                    0
                ),
                isDisplayed()
            )
        )
        paintSizeRadioButton.check(matches(isChecked()))

        val freeDrawView = onView(
            allOf(
                withId(R.id.freeDrawView),
                childAtPosition(
                    IsInstanceOf.instanceOf(LinearLayout::class.java),
                    3
                ),
                isDisplayed()
            )
        )
        freeDrawView.check(matches(isDisplayed()))

        freeDrawView.perform(swipeDown())

        saveBtn.check(matches(isEnabled()))

        undoBtn.check(matches(isEnabled()))
        redoBtn.check(matches(not(isEnabled())))

        undoBtn.perform(click())

        redoBtn.check(matches(isEnabled()))
        undoBtn.check(matches(not(isEnabled())))
        saveBtn.check(matches(not(isEnabled())))
    }
}