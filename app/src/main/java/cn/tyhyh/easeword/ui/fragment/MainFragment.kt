package cn.tyhyh.easeword.ui.fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import cn.tyhyh.easeword.R
import cn.tyhyh.easeword.databinding.FragmentMainBinding
import cn.tyhyh.easeword.ui.adapter.WordAdapter
import cn.tyhyh.easeword.ui.itemdecoration.SpaceItemDecoration
import cn.tyhyh.easeword.ui.viewmodel.MainFragViewModel
import cn.tyhyh.easeword.ui.viewmodel.MainViewModel
import cn.tyhyh.easeword.util.InjectorUtil

class MainFragment : Fragment() {

    companion object {
        private const val TAG = "MainFragment"

        fun newInstance(): MainFragment {
            return MainFragment()
        }
    }

    private val wordAdapter by lazy {
        WordAdapter(requireActivity())
    }

    private val mainActivityViewModel by lazy {
        ViewModelProvider(requireActivity(), InjectorUtil.getMainModelFactory()).get(MainViewModel::class.java)
    }

    private val viewModel by lazy {
        ViewModelProvider(this, InjectorUtil.getMainFragModelFactory()).get(MainFragViewModel::class.java)
    }

    private lateinit var binding: FragmentMainBinding

    override fun onCreateAnimation(transit: Int, enter: Boolean, nextAnim: Int): Animation? {
        return if (enter) {
            AnimationUtils.loadAnimation(activity, android.R.anim.fade_in)
        } else {
            AnimationUtils.loadAnimation(activity, android.R.anim.fade_out)
        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        configRecyclerView(binding.wordRecyclerView)
        observe()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_main, container, false)
        binding.lifecycleOwner = this
        binding.viewModel = viewModel
        return binding.root
    }

    private fun observe() {
        mainActivityViewModel.selectChapter.observe(this, Observer {
            viewModel.setSelectedChapter(it)
        })
        viewModel.wordList.observe(this, Observer {
            Log.d(TAG, it.toString())
            wordAdapter.submitList(it)
        })
    }

    private fun configRecyclerView(recyclerView: RecyclerView) {
        recyclerView.addItemDecoration(SpaceItemDecoration(24))
        recyclerView.adapter = wordAdapter
    }
}