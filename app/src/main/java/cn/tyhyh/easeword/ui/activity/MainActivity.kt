package cn.tyhyh.easeword.ui.activity

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import cn.tyhyh.easeword.R
import cn.tyhyh.easeword.databinding.ActivityMainBinding
import cn.tyhyh.easeword.ui.activity.base.BaseActivity
import cn.tyhyh.easeword.ui.viewmodel.MainViewModel
import cn.tyhyh.easeword.util.InjectorUtil

class MainActivity : BaseActivity() {

    private lateinit var binding: ActivityMainBinding

    private val viewModel by lazy {
        ViewModelProvider(this, InjectorUtil.getMainModelFactory()).get(MainViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        binding.viewModel = viewModel
        setupSupportActionBar()
    }

    private fun setupSupportActionBar(setNoTitle: Boolean = true) {
        setSupportActionBar(binding.toolBar)
        val actonBar = supportActionBar ?: return
        if (setNoTitle) actonBar.title = null
    }

    companion object {
        fun actionStart(activity: Activity) {
            activity.startActivity(Intent(activity, MainActivity::class.java))
            activity.finish()
        }
    }
}