package cn.tyhyh.easeword.ui.activity

import android.os.Bundle
import androidx.core.view.postDelayed
import cn.tyhyh.easeword.data.db.EaseWordDB
import cn.tyhyh.easeword.databinding.ActivitySplashBinding
import cn.tyhyh.easeword.ui.activity.base.BaseActivity
import cn.tyhyh.easeword.util.FontUtil

class SplashActivity : BaseActivity() {

    companion object {
        private const val DURING = 2000
    }

    @Volatile
    private var startTime = 0L

    @Volatile
    private lateinit var binding: ActivitySplashBinding

    private val initialize = Runnable {
        val currentTimeMills = System.currentTimeMillis()
        val duration = currentTimeMills - startTime
        EaseWordDB.initFromAsset()
        binding.root.postDelayed(DURING - duration) {startHomeActivity()}
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySplashBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.apply {
            FontUtil.setTypefaceForTextView(teText, FontUtil.YRDZS)
            FontUtil.setTypefaceForTextView(bsText, FontUtil.YRDZS)
        }
    }

    private fun startHomeActivity() {
        MainActivity.actionStart(this)
//        TestActivity.actionStart(this)
        finish()
    }

    override fun onResume() {
        super.onResume()
        startTime = System.currentTimeMillis()
        Thread(initialize).start()
    }
}