package cn.tyhyh.easeword.ui.activity

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.MotionEvent
import android.view.View
import android.widget.EditText
import androidx.appcompat.widget.SearchView
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import cn.tyhyh.easeword.R
import cn.tyhyh.easeword.databinding.ActivitySearchBinding
import cn.tyhyh.easeword.ui.activity.base.BaseActivity
import cn.tyhyh.easeword.ui.adapter.WordAdapter
import cn.tyhyh.easeword.ui.itemdecoration.SpaceItemDecoration
import cn.tyhyh.easeword.ui.viewmodel.SearchViewModel
import cn.tyhyh.easeword.util.InjectorUtil

/**
 * author: tiny
 * created on: 20-4-3 上午10:55
 */
class SearchActivity : BaseActivity() {

    private lateinit var binding: ActivitySearchBinding

    private val viewModel by lazy {
        ViewModelProvider(this, InjectorUtil.getSearchModelFactory()).get(SearchViewModel::class.java)
    }

    private val searchAdapter by lazy {
        WordAdapter(this)
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_search)
        binding.lifecycleOwner = this
        configSearchResultRv(binding.searchResultRv)
        binding.searchResultRv.setOnTouchListener { _, event ->
            clearSearchViewFocusIfNeed(event.actionMasked)
            false
        }
        binding.root.setOnTouchListener { _, event ->
            clearSearchViewFocusIfNeed(event.actionMasked)
            false
        }
        observe()
        setupSupportToolbar()
    }

    private fun clearSearchViewFocusIfNeed(event: Int) {
        if (event == MotionEvent.ACTION_DOWN) {
            binding.toolbarBinding.searchView.clearFocus()
        }
    }

    private fun configSearchResultRv(searchResultRv: RecyclerView) {
        searchResultRv.addItemDecoration(SpaceItemDecoration(24))
        searchResultRv.adapter = searchAdapter
    }

    private fun setupSupportToolbar() {
        binding.toolbarBinding.let {
            it.searchView.findViewById<EditText>(R.id.search_src_text).apply {
                setHintTextColor(ContextCompat.getColor(this@SearchActivity, R.color.colorSearchHint))
                textSize = 16f
                setTextColor(ContextCompat.getColor(this@SearchActivity, R.color.colorCommonBlack))
            }
            it.searchView.findViewById<View>(R.id.search_plate).background = null
            it.leftBtn.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.ic_back))
            it.leftBtn.setOnClickListener { onBackPressed() }
            it.searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
                override fun onQueryTextSubmit(query: String?) = false

                override fun onQueryTextChange(newText: String?): Boolean {
                    viewModel.submitQuery(newText)
                    return true
                }
            })
        }
    }

    private fun observe() {
        viewModel.queryResult.observe(this, Observer {
            val length = it?.size ?: 0
            binding.searchCountTv.text = getString(R.string.search_count_text, length)
            searchAdapter.submitList(it)
        })
    }

    companion object {
        private const val TAG = "SearchActivity"

        fun actonStart(activity: Activity) {
            activity.startActivity(Intent(activity, SearchActivity::class.java))
        }
    }
}