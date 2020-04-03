package cn.tyhyh.easeword.ui.activity

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import cn.tyhyh.easeword.R
import cn.tyhyh.easeword.databinding.ActivityFeedbackBinding
import cn.tyhyh.easeword.ui.activity.base.BaseActivity
import cn.tyhyh.easeword.ui.viewmodel.FeedBackViewModel
import cn.tyhyh.easeword.util.InjectorUtil

/**
 * author: tiny
 * created on: 20-3-22 上午10:16
 */
class FeedbackActivity : BaseActivity() {

    private val viewModel by lazy {
        ViewModelProvider(this, InjectorUtil.getFeedbackModelFactory()).get(FeedBackViewModel::class.java)
    }

    private lateinit var binding: ActivityFeedbackBinding

    private val circleDrawables by lazy {
        arrayOf(
            getDrawable(R.drawable.circle_r20dp)!!.also {
                it.setTint(ContextCompat.getColor(this, R.color.colorYellowCircle))
            },
            getDrawable(R.drawable.circle_r20dp)!!.also {
                it.setTint(ContextCompat.getColor(this, R.color.colorBlueCircle))
            },
            getDrawable(R.drawable.circle_r15_5dp)!!.also {
                it.setTint(ContextCompat.getColor(this, R.color.colorRedCircle))
            }
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_feedback)
        binding.lifecycleOwner = this
        binding.dynamicBgContainer.setDrawables(*circleDrawables)
        binding.dynamicBgContainer.observe(this)
        binding.onSend = View.OnClickListener {
            when {
                viewModel.isContactEmpty() -> binding.contactEv.requestFocus()
                viewModel.isFeedbackEmpty() -> binding.feedbackEv.requestFocus()
                else -> viewModel.sendFeedback()
            }
        }
        binding.viewModel = viewModel
        setupSupportActionBar()
    }

    private fun setupSupportActionBar(title: String? = null) {
        binding.toolbarBinding.apply {
            leftBtn.setImageDrawable(getDrawable(R.drawable.ic_back))
            rightBtn.setImageDrawable(getDrawable(R.drawable.ic_home))
            leftBtn.setOnClickListener { onBackPressed() }
            rightBtn.setOnClickListener {
                MainActivity.actionStart(this@FeedbackActivity)
                this@FeedbackActivity.finish()
            }
        }
        setSupportActionBar(binding.toolbarBinding.toolbar)
        val actonBar = supportActionBar ?: return
        actonBar.title = title
    }

    companion object {
        private const val TAG = "FeedbackActivity"

        fun actionStart(activity: Activity) {
            activity.startActivity(Intent(activity, FeedbackActivity::class.java))
        }
    }
}