package cn.tyhyh.easeword.ui.activity

import android.os.Bundle
import android.view.View
import androidx.databinding.DataBindingUtil
import cn.tyhyh.easeword.R
import cn.tyhyh.easeword.databinding.ActivitySplashBinding
import cn.tyhyh.easeword.ui.activity.base.BaseActivity
import cn.tyhyh.easeword.util.FontUtil

class SplashActivity : BaseActivity() {

    companion object {
        private const val DURING = 4000
    }

    @Volatile
    private var startTime = 0L

    private lateinit var binding: ActivitySplashBinding

    private val doCHeck = Runnable {
        val currentTimeMills = System.currentTimeMillis()
        val duration = currentTimeMills - startTime
        findViewById<View>(android.R.id.content)
            .postDelayed({ startHomeActivity() }, DURING - duration)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_splash)
        binding.apply {
            FontUtil.setTypefaceForTextView(teText, FontUtil.YRDZS_PATH)
            FontUtil.setTypefaceForTextView(bsText, FontUtil.YRDZS_PATH)
        }
    }

    private fun startHomeActivity() {
        MainActivity.actionStart(this)
    }

    override fun onResume() {
        super.onResume()
        startTime = System.currentTimeMillis()
        Thread(doCHeck).start()
    }
}