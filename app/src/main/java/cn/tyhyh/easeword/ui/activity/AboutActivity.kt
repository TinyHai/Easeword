package cn.tyhyh.easeword.ui.activity

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import cn.tyhyh.easeword.R
import cn.tyhyh.easeword.databinding.ActivityAboutBinding
import cn.tyhyh.easeword.ui.activity.base.BaseActivity

class AboutActivity : BaseActivity() {

    companion object {
        fun actionStart(activity: Activity) {
            activity.startActivity(Intent(activity, AboutActivity::class.java))
        }
    }

    private lateinit var binding: ActivityAboutBinding

    private val circleDrawables by lazy {
        arrayOf(
            getDrawable(R.drawable.circle_r20dp)!!.also {
                it.setTint(ContextCompat.getColor(this, R.color.colorYellowCircle))
            },
            getDrawable(R.drawable.circle_r20dp)!!.also {
                it.setTint(ContextCompat.getColor(this, R.color.colorRedCircle))
            },
            getDrawable(R.drawable.circle_r20dp)!!.also {
                it.setTint(ContextCompat.getColor(this, R.color.colorBlueCircle))
            }
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_about)
        binding.dynamicBgContainer.setDrawables(*circleDrawables)
        binding.dynamicBgContainer.observe(this)
        setupSupportActionBar(getString(R.string.about_us))
    }

    private fun setupSupportActionBar(title: String) {
        binding.toolbarBinding.apply {
            leftBtn.setImageDrawable(getDrawable(R.drawable.ic_back))
            rightBtn.setImageDrawable(getDrawable(R.drawable.ic_home))
            leftBtn.setOnClickListener { onBackPressed() }
            rightBtn.setOnClickListener {
                MainActivity.actionStart(this@AboutActivity)
                this@AboutActivity.finish()
            }
            this.title = title
        }
        setSupportActionBar(binding.toolbarBinding.toolbar)
        val actonBar = supportActionBar ?: return
        actonBar.title = null
    }
}