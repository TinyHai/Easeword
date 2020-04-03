package cn.tyhyh.easeword.ui.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import cn.tyhyh.easeword.databinding.ActivityTestBinding
import cn.tyhyh.easeword.ui.activity.base.BaseActivity

/**
 * author: tiny
 * created on: 20-3-26 上午11:55
 */
class TestActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val viewBinding = ActivityTestBinding.inflate(layoutInflater)
        setContentView(viewBinding.root)
    }

    companion object {
        fun actionStart(context: Context) {
            context.startActivity(Intent(context, TestActivity::class.java))
        }
    }
}