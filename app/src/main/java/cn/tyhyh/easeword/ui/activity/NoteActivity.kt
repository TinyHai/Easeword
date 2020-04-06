package cn.tyhyh.easeword.ui.activity

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.GestureDetector
import android.view.MotionEvent
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import cn.tyhyh.easeword.R
import cn.tyhyh.easeword.data.db.EaseWordDB
import cn.tyhyh.easeword.databinding.ActivityNoteBinding
import cn.tyhyh.easeword.ui.activity.base.BaseActivity
import cn.tyhyh.easeword.ui.viewmodel.NoteViewModel
import cn.tyhyh.easeword.util.FontUtil
import cn.tyhyh.easeword.util.InjectorUtil
import cn.tyhyh.easeword.util.ToastUtil


/**
 * author: tiny
 * created on: 20-3-31 上午10:30
 */
class NoteActivity : BaseActivity() {

    private lateinit var binding: ActivityNoteBinding

    private val viewModel by lazy {
        ViewModelProvider(this, InjectorUtil.getNoteModelFactory()).get(NoteViewModel::class.java)
    }

    private val wordId
        get() = intent.getLongExtra(WORD_ID, EaseWordDB.INVALID_ID)

    private val essayId
        get() = intent.getLongExtra(ESSAY_ID, EaseWordDB.INVALID_ID)

    private val gestureDetector by lazy {
        GestureDetector(this, object : GestureDetector.SimpleOnGestureListener() {
            override fun onSingleTapConfirmed(e: MotionEvent?): Boolean {
                binding.contentEv.requestFocus()
                return true
            }
        })
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_note)
        binding.lifecycleOwner = this
        binding.contentEv.addTextChangedListener(object : TextWatcher {

            override fun afterTextChanged(s: Editable) {
                val count = s.count()
                binding.noteTextCountTv.text = getString(R.string.note_text_count_with_limit, count)
                val countWithOutBlank = s.count {
                    !it.isWhitespace()
                }
                binding.toolbarBinding.rightBtn.isEnabled = countWithOutBlank > 0
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }
        })
        binding.contentEv.setOnFocusChangeListener { v, hasFocus ->
            if (hasFocus) {
                showSoftKeyBoard(v)
            } else {
                hideSoftKeyBoard()
            }
        }
        binding.noteWrapper.setOnTouchListener { _, event -> gestureDetector.onTouchEvent(event)}
        binding.viewModel = viewModel
        viewModel.setWordIdAndEssayId(wordId, essayId)
        FontUtil.setTypefaceForTextView(binding.contentEv, FontUtil.YRDZS)
        FontUtil.setTypefaceForTextView(binding.wordTv, FontUtil.SWJZ)
        setupSupportActionBar()
        observe()
    }

    private fun showSoftKeyBoard(view: View) {
        val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.showSoftInput(view, InputMethodManager.SHOW_FORCED)
    }

    private fun hideSoftKeyBoard() {
        val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(window.decorView.windowToken, 0)
    }

    private fun observe() {
        viewModel.word.observe(this, Observer {
            binding.wordTv.text = it.text
        })
        viewModel.saving.observe(this, Observer { saving ->
            when (saving) {
                false -> {
                    ToastUtil.showShortToast(R.string.save_success)
                    binding.root.postDelayed({ onBackPressed() }, 1000)
                }
                null -> {
                    ToastUtil.showShortToast(R.string.save_fail)
                }
            }
        })
        viewModel.loading.observe(this, Observer {
            binding.contentEv.isEnabled = it != true
        })
        viewModel.essay.observe(this, Observer {
            binding.contentEv.setText(it.content)
        })
    }

    private fun setupSupportActionBar(title: String? = null) {
        binding.toolbarBinding.apply {
            leftBtn.setImageDrawable(getDrawable(R.drawable.ic_back))
            rightBtn.setImageDrawable(
                getDrawable(R.drawable.ic_yes)!!.also {
                    it.setTint(ContextCompat.getColor(this@NoteActivity, R.color.colorOrange))
                }
            )
            leftBtn.setOnClickListener { onBackPressed() }
            rightBtn.setOnClickListener {
                val needSave = viewModel.saveNote()
                if (!needSave) {
                    onBackPressed()
                }
            }
            rightBtn.isEnabled = false
            this.title = title
        }
        setSupportActionBar(binding.toolbarBinding.toolbar)
        val actonBar = supportActionBar ?: return
        actonBar.title = null
    }

    companion object {
        private const val TAG = "NoteActivity"

        private const val WORD_ID = "word_id"

        private const val ESSAY_ID = "essay_id"

        fun actionStart(
            activity: Activity,
            wordId: Long,
            essayId: Long = EaseWordDB.INVALID_ID
        ) {
            if (wordId == EaseWordDB.INVALID_ID) {
                throw IllegalArgumentException("wordId = $essayId is invalid")
            }
            activity.startActivity(
                Intent(activity, NoteActivity::class.java).also {
                    it.putExtra(WORD_ID, wordId)
                    it.putExtra(ESSAY_ID, essayId)
                }
            )
        }
    }
}