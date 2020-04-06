package cn.tyhyh.easeword.ui.activity


import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.recyclerview.widget.RecyclerView
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import androidx.test.rule.ActivityTestRule
import cn.tyhyh.easeword.R
import cn.tyhyh.easeword.util.childAtPosition
import cn.tyhyh.easeword.util.withRecyclerView
import org.hamcrest.Matchers.allOf
import org.hamcrest.Matchers.not
import org.hamcrest.core.IsInstanceOf
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@LargeTest
@RunWith(AndroidJUnit4::class)
class MainActivityTest {

    @Rule
    @JvmField
    var mActivityTestRule = ActivityTestRule(MainActivity::class.java)

    @Test
    fun mainActivityTest() {
        // Added a sleep statement to match the app's execution delay.
        // The recommended way to handle such scenarios is to use Espresso idling resources:
        // https://google.github.io/android-testing-support-library/docs/espresso/idling-resource/index.html

        val mainFragmentView = onView(
            allOf(
                withId(R.id.wordFragment),
                childAtPosition(
                    childAtPosition(
                        IsInstanceOf.instanceOf(ViewGroup::class.java),
                        0
                    ),
                    0
                )
            )
        )

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
                isDisplayed()
            )
        )
        for (i in 0..10) { // 进行单数次点击，可加大次数，确保多次点击后界面显示正常
            titleTv.perform(click())
        }

        val chapterContainer = onView(
            allOf(
                withId(R.id.chapterContainer),
                childAtPosition(
                    childAtPosition(
                        IsInstanceOf.instanceOf(ViewGroup::class.java),
                        0
                    ),
                    1
                ),
                isDisplayed()
            )
        )

        Thread.sleep(800) // 等待动画结束 动画时长400ms,等待800ms防止误差
        chapterContainer.check(matches(isDisplayed()))
        mainFragmentView.check(matches(not(isDisplayed())))

        titleTv.perform(click())

        mainFragmentView.check(matches(isDisplayed()))

        val recyclerView = mActivityTestRule.activity.findViewById<RecyclerView>(R.id.wordRecyclerView)
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
        wordText.perform(click())
    }
}
