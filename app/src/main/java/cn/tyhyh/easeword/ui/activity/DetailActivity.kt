package cn.tyhyh.easeword.ui.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import cn.tyhyh.easeword.R
import cn.tyhyh.easeword.data.db.EaseWordDB
import cn.tyhyh.easeword.databinding.ActivityDetailBinding
import cn.tyhyh.easeword.ui.activity.base.BaseActivity
import cn.tyhyh.easeword.ui.adapter.DetailAdapter
import cn.tyhyh.easeword.ui.itemdecoration.SpaceItemDecoration
import cn.tyhyh.easeword.ui.viewmodel.DetailViewModel
import cn.tyhyh.easeword.util.FontUtil
import cn.tyhyh.easeword.util.InjectorUtil

/**
 * author: tiny
 * created on: 20-3-24 下午8:17
 */
class DetailActivity : BaseActivity() {

    private lateinit var binding: ActivityDetailBinding

    private val viewModel by lazy {
        ViewModelProvider(this, InjectorUtil.getDetailModelFactory()).get(DetailViewModel::class.java)
    }

    private val wordId: Long
        get() = intent.getLongExtra(WORD_ID, EaseWordDB.INVALID_ID)

    private val detailAdapter: DetailAdapter by lazy {
        DetailAdapter(this, viewModel)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_detail)
        binding.lifecycleOwner = this
        viewModel.setWordId(wordId)
        binding.addEssayBtn.setOnClickListener { _ ->
            viewModel.reverseFabStatus()
        }
        binding.addDrawing.let { fab ->
            fab.setOnClickListener {
                DrawingActivity.actionStart(this, wordId)
                viewModel.hideTwoFab()
            }
        }
        binding.addNote.let { fab ->
            fab.setOnClickListener {
                NoteActivity.actionStart(this, wordId)
                viewModel.hideTwoFab()
            }
        }
        configRecyclerView(binding.essayContentRv)
        setupSupportActionBar()
        observe()
    }

    private fun hideTwoFab() {
        binding.addNote.hide()
        binding.addDrawing.hide()
    }

    private fun showTwoFab() {
        binding.addDrawing.show()
        binding.addNote.show()
    }

    private fun configRecyclerView(recyclerView: RecyclerView) {
        recyclerView.adapter = detailAdapter
        recyclerView.addItemDecoration(SpaceItemDecoration(16))
    }

    private fun observe() {
        viewModel.essayList.observe(this, Observer {
            binding.hasEssay = !it.isNullOrEmpty()
            detailAdapter.submitList(it)
        })
        viewModel.word.observe(this, Observer {
            binding.toolbarBinding.rightBtn.text = it?.text
            binding.toolbarBinding.titleTv.text = it?.text
        })
        viewModel.showAddFab.observe(this, Observer { show ->
            if (show) {
                showTwoFab()
            } else {
                hideTwoFab()
            }
        })
    }

    private fun setupSupportActionBar() {
        binding.toolbarBinding.apply {
            leftBtn.setImageDrawable(getDrawable(R.drawable.ic_back))
            leftBtn.setOnClickListener { onBackPressed() }
            FontUtil.setTypefaceForTextView(rightBtn, FontUtil.SWJZ)
        }
        setSupportActionBar(binding.toolbarBinding.toolbar)
        val actonBar = supportActionBar ?: return
        actonBar.title = null
    }

    companion object {

        private const val TAG = "DetailActivity"

        const val WORD_ID = "word_id"

        fun actionStart(context: Context, wordId: Long) {
            context.startActivity(
                Intent(context, DetailActivity::class.java).also {
                    it.putExtra(WORD_ID, wordId)
                }
            )
        }
    }
}