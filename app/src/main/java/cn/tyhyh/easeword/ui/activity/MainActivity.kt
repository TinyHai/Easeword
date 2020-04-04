package cn.tyhyh.easeword.ui.activity

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import androidx.core.view.isInvisible
import androidx.core.view.isVisible
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import cn.tyhyh.easeword.R
import cn.tyhyh.easeword.databinding.ActivityMainBinding
import cn.tyhyh.easeword.ui.activity.base.BaseActivity
import cn.tyhyh.easeword.ui.adapter.ChapterAdapter
import cn.tyhyh.easeword.ui.itemdecoration.SpaceItemDecoration
import cn.tyhyh.easeword.ui.viewmodel.MainViewModel
import cn.tyhyh.easeword.util.InjectorUtil
import cn.tyhyh.easeword.util.ToastUtil

class MainActivity : BaseActivity() {

    private lateinit var binding: ActivityMainBinding

    private val viewModel by lazy {
        ViewModelProvider(this, InjectorUtil.getMainModelFactory()).get(MainViewModel::class.java)
    }

    private val rollUpAnimation: Animation by lazy {
        AnimationUtils.loadAnimation(this, R.anim.roll_up_catalog)
    }

    private val rollDownAnimation: Animation by lazy {
        AnimationUtils.loadAnimation(this, R.anim.roll_down_catalog)
    }

    private val mainFragment by lazy {
        supportFragmentManager.findFragmentById(R.id.wordFragment)!!
    }

    private val chapterAdapter by lazy {
        ChapterAdapter(viewModel)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        binding.apply {
            lifecycleOwner = this@MainActivity
            viewModel = this@MainActivity.viewModel
            rollUpAnimation.setAnimationListener(object : Animation.AnimationListener {
                    override fun onAnimationRepeat(animation: Animation?) {}

                    override fun onAnimationEnd(animation: Animation?) {
                        if (chapterContainer.isVisible) {
                            chapterContainer.visibility = View.INVISIBLE
                        }
                        showMainFragment()
                    }

                    override fun onAnimationStart(animation: Animation?) {
                        if (chapterContainer.isInvisible) {
                            chapterContainer.visibility = View.VISIBLE
                        }
                    }
                })
            rollDownAnimation.setAnimationListener(object : Animation.AnimationListener {
                override fun onAnimationRepeat(animation: Animation?) {}

                override fun onAnimationEnd(animation: Animation?) {}

                override fun onAnimationStart(animation: Animation?) {
                    hideMainFragment()
                    if (chapterContainer.isInvisible) {
                        chapterContainer.visibility = View.VISIBLE
                    }
                }
            })
            chapterRecyclerView.addItemDecoration(SpaceItemDecoration(12))
            chapterRecyclerView.adapter = chapterAdapter
            onCatalogClick = View.OnClickListener {
                this@MainActivity.viewModel.reverseCatalogStatus()
            }
        }
        setupSupportActionBar()
        observe()
    }

    private fun observe() {
        viewModel.chapterList.observe(this, Observer {
            chapterAdapter.submitList(it)
        })
        viewModel.selectChapter.observe(this, Observer {
            Log.d(TAG, "selectChapter = $it")
            if (it != null) {
                binding.catalogText = getString(R.string.catalog_text, it.id)
            } else {
                binding.catalogText = getString(R.string.default_catalog_text)
            }
            chapterAdapter.setSelectedChapterId(it.id)
            viewModel.saveLastSelectedChapterId(it)
        })
        viewModel.isRefreshing.observe(this, Observer {
            if (it) {
                chapterAdapter.submitList(emptyList())
                viewModel.load()
            }
        })
        viewModel.isCatalogRollUp.observe(this, Observer {
            if (it == true) {
                rollUpCatalog()
            } else {
                rollDownCatalog()
            }
        })
    }

    private fun showMainFragment() {
        if (mainFragment.isVisible) {
            return
        }
        supportFragmentManager.beginTransaction().show(mainFragment).commit()
    }

    private fun hideMainFragment() {
        if (mainFragment.isHidden) {
            return
        }
        supportFragmentManager.beginTransaction().hide(mainFragment).commit()
    }

    private fun rollUpCatalog() {
        if (rollDownAnimation.hasStarted() and !rollDownAnimation.hasEnded()) {
            rollDownAnimation.cancel()
        }
        binding.chapterContainer.startAnimation(rollUpAnimation)
        binding.catalogDrawingEnd = getDrawable(R.mipmap.icon_roll_down)
    }

    private fun rollDownCatalog() {
        if (rollUpAnimation.hasStarted() and !rollUpAnimation.hasEnded()) {
            rollUpAnimation.cancel()
        }
        binding.chapterContainer.startAnimation(rollDownAnimation)
        binding.catalogDrawingEnd = getDrawable(R.mipmap.icon_roll_up)
    }

    private fun setupSupportActionBar() {
        binding.toolbarBinding.apply {
            leftBtn.setImageDrawable(getDrawable(R.drawable.ic_set))
            rightBtn.setImageDrawable(getDrawable(R.drawable.ic_search))
            leftBtn.setOnClickListener { SettingActivity.actionStart(this@MainActivity) }
            rightBtn.setOnClickListener { SearchActivity.actonStart(this@MainActivity) }
        }
        binding.catalogDrawingEnd = getDrawable(R.mipmap.icon_roll_down)
        setSupportActionBar(binding.toolbarBinding.toolbar)
        val actonBar = supportActionBar ?: return
        actonBar.title = null
    }

    private var lastPressTime: Long = 0L

    override fun onBackPressed() {
        val currentTime = System.currentTimeMillis()
        val duration = currentTime - lastPressTime
        if (duration == currentTime || duration > 2000) {
            lastPressTime = currentTime
            ToastUtil.showShortToast("再按一次退出")
            return
        }
        super.onBackPressed()
    }

    companion object {

        private const val TAG = "MainActivity"

        fun actionStart(activity: Activity) {
            activity.startActivity(Intent(activity, MainActivity::class.java))
        }
    }
}