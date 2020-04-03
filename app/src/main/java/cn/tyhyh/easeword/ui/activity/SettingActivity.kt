package cn.tyhyh.easeword.ui.activity

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.core.content.ContextCompat
import cn.tyhyh.easeword.R
import cn.tyhyh.easeword.databinding.ActivitySettingBinding
import cn.tyhyh.easeword.ui.activity.base.BaseActivity

class SettingActivity : BaseActivity() {
    companion object {
        fun actionStart(activity: Activity) {
            activity.startActivity(Intent(activity, SettingActivity::class.java))
        }
    }

    private lateinit var viewBinding: ActivitySettingBinding

    private val circleDrawables by lazy {
        arrayOf(
            getDrawable(R.drawable.circle_r12dp)!!.also {
                it.setTint(ContextCompat.getColor(this, R.color.colorYellowCircle))
            },
            getDrawable(R.drawable.circle_r19dp)!!.also {
                it.setTint(ContextCompat.getColor(this, R.color.colorRedCircle))
            },
            getDrawable(R.drawable.circle_r28dp)!!.also {
                it.setTint(ContextCompat.getColor(this, R.color.colorBlueCircle))
            }
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewBinding = ActivitySettingBinding.inflate(layoutInflater)
        setContentView(viewBinding.root)
        viewBinding.aboutUsBtn.setOnClickListener { AboutActivity.actionStart(this) }
        viewBinding.feedbackBtn.setOnClickListener { FeedbackActivity.actionStart(this) }
        viewBinding.dynamicBgContainer.setDrawables(*circleDrawables)
        viewBinding.dynamicBgContainer.observe(this)
        setupSupportActionBar(getString(R.string.setting))
    }

    private fun setupSupportActionBar(title: String? = null) {
        viewBinding.toolbarBinding.apply {
            leftBtn.setImageDrawable(getDrawable(R.drawable.ic_back))
            rightBtn.setImageDrawable(getDrawable(R.drawable.ic_home))
            leftBtn.setOnClickListener { onBackPressed() }
            rightBtn.setOnClickListener {
                MainActivity.actionStart(this@SettingActivity)
                this@SettingActivity.finish()
            }
            this.title = title
        }
        setSupportActionBar(viewBinding.toolbarBinding.toolbar)
        val actonBar = supportActionBar ?: return
        actonBar.title = null
    }
}