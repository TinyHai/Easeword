package cn.tyhyh.easeword.ui.activity

import android.util.Log
import android.widget.LinearLayout
import androidx.recyclerview.widget.RecyclerView
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.*
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.runners.AndroidJUnit4
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

/**
 * author: tiny
 * created on: 20-4-6 下午4:14
 */
@RunWith(AndroidJUnit4::class)
class SearchActivityTest {

    companion object {
        private const val TAG = "SearchActivityTest"
    }

    @Rule
    @JvmField
    val mSearchActivityTest = ActivityTestRule(SearchActivity::class.java)

    @Test
    fun searchActivityTest() {

        val searchAutoCompleteEv = onView(
            allOf(
                withId(R.id.search_src_text),
                childAtPosition(
                    allOf(
                        withId(R.id.search_plate),
                        childAtPosition(
                            withId(R.id.search_edit_frame),
                            1
                        )
                    ),
                    0
                ),
                isDisplayed()
            )
        )

        val clearQueryBtn = onView(
            allOf(
                withId(R.id.search_close_btn), withContentDescription("Clear query"),
                childAtPosition(
                    allOf(
                        withId(R.id.search_plate),
                        childAtPosition(
                            withId(R.id.search_edit_frame),
                            1
                        )
                    ),
                    1
                )
            )
        )

        val searchResultCountTv = onView(
            allOf(
                withId(R.id.searchCountTv),
                isDisplayed()
            )
        )

        searchResultCountTv.check(matches(withText("搜索到0个结果")))

        val searchResultRv = onView(
            allOf(
                withId(R.id.searchResultRv),
                childAtPosition(
                    IsInstanceOf.instanceOf(LinearLayout::class.java),
                    4
                ),
                isDisplayed()
            )
        )

        searchResultRv.check(matches(isDisplayed()))

        searchAutoCompleteEv.perform(click())
        clearQueryBtn.check(matches(not(isDisplayed())))
        searchAutoCompleteEv.perform(replaceText("天"), closeSoftKeyboard())
        clearQueryBtn.check(matches(isDisplayed()))

        val searchResult = onView(
            withRecyclerView(R.id.searchResultRv).atPosition(0)
        )

        Thread.sleep(500) // 由于是异步搜索，当查询条件改变就会自动重新查询并显示查询结果

        searchResult.check(matches(isDisplayed()))
        val adapter = mSearchActivityTest.activity.findViewById<RecyclerView>(R.id.searchResultRv).adapter!!
        val itemCount = adapter.itemCount
        searchResultCountTv.check(matches(withText("搜索到${itemCount}个结果")))

        clearQueryBtn.perform(click())

        Thread.sleep(500) // 由于是异步搜索，当查询条件改变就会自动重新查询并显示查询结果

        searchResult.check { view, exception ->
            if (view != null || exception == null) {
                Log.e(TAG, "searchResult's view is not null")
                Log.e(TAG, view.toString())
                throw IllegalStateException("search result is invalid")
            }
        }
    }
}