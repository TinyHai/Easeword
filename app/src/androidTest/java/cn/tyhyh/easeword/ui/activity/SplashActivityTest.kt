package cn.tyhyh.easeword.ui.activity

import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.rule.ActivityTestRule
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

/**
 * author: tiny
 * created on: 20-4-6 下午4:05
 */
@RunWith(AndroidJUnit4::class)
class SplashActivityTest {

    @Rule
    @JvmField
    val mSplashActivityTest = ActivityTestRule(SplashActivity::class.java)

    @Test
    fun splashActivityTest() {
        Thread.sleep(2100)
        assertTrue("SplashActivity is not finished", mSplashActivityTest.activity.isFinishing)
    }
}