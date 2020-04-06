package cn.tyhyh.easeword.ui.activity

import android.content.Intent
import android.widget.LinearLayout
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.ViewInteraction
import androidx.test.espresso.action.ViewActions.click
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
 * created on: 20-4-6 下午5:16
 */
@RunWith(AndroidJUnit4::class)
class DetailActivityTest {

    @Rule
    @JvmField
    val mDetailActivityTestRule = ActivityTestRule(DetailActivity::class.java, false, false)

    @Before
    fun setUp() {
        val intent = Intent()
        intent.putExtra("word_id", 1L)
        mDetailActivityTestRule.launchActivity(intent)
    }

    @Test
    fun detailActivityTest() {
        val noContentView = onView(
            allOf(
                withText("暂无内容，点击下方按钮添加"),
                childAtPosition(
                    IsInstanceOf.instanceOf(CoordinatorLayout::class.java),
                    2
                )
            )
        )
        noContentView.check(matches(isDisplayed()))

        val titleTv = onView(
            allOf(
                withId(R.id.titleTv),
                childAtPosition(
                    allOf(
                        withId(R.id.toolbarBinding),
                        childAtPosition(
                            IsInstanceOf.instanceOf(LinearLayout::class.java),
                            0
                        )
                    ),
                    1
                ),
                withText("天")
            )
        )
        titleTv.check(matches(isDisplayed()))

        val rightTv = onView(
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
                withText("天")
            )
        )
        rightTv.check(matches(isDisplayed()))

        val essayContentRv = onView(
            withId(R.id.essayContentRv)
        )
        essayContentRv.check(matches(not(isDisplayed())))

        val addFloatActionButton = onView(
            allOf(
                withId(R.id.addEssayBtn),
                isDisplayed()
            )
        )
        val addNoteBtn = onView(
            withId(R.id.addNote)
        )
        val addDrawingBtn = onView(
            withId(R.id.addDrawing)
        )
        checkAllIsNotDisplayed(addNoteBtn, addDrawingBtn)

        addFloatActionButton.perform(click())

        checkAllIsDisplayed(addNoteBtn, addDrawingBtn)

        addFloatActionButton.perform(click())

        checkAllIsNotDisplayed(addNoteBtn, addDrawingBtn)
    }

    private fun checkAllIsDisplayed(vararg viewInteraction: ViewInteraction) {
        viewInteraction.forEach {
            it.check(matches(isDisplayed()))
        }
    }

    private fun checkAllIsNotDisplayed(vararg viewInteraction: ViewInteraction) {
        viewInteraction.forEach {
            it.check(matches(not(isDisplayed())))
        }
    }
}