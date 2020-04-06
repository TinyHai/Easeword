package cn.tyhyh.easeword.ui.activity

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.os.Bundle
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import cn.tyhyh.easeword.GlideApp
import cn.tyhyh.easeword.R
import cn.tyhyh.easeword.data.db.EaseWordDB
import cn.tyhyh.easeword.databinding.ActivityDrawingBinding
import cn.tyhyh.easeword.ui.activity.base.BaseActivity
import cn.tyhyh.easeword.ui.viewmodel.DrawingViewModel
import cn.tyhyh.easeword.util.FontUtil
import cn.tyhyh.easeword.util.InjectorUtil
import cn.tyhyh.easeword.util.ToastUtil
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import com.rm.freedrawview.FreeDrawView
import com.rm.freedrawview.PathRedoUndoCountChangeListener
import kotlinx.android.synthetic.main.common_toolbar.view.*

/**
 * author: tiny
 * created on: 20-3-22 下午9:37
 */
class DrawingActivity : BaseActivity(), FreeDrawView.DrawCreatorListener {

    private lateinit var binding: ActivityDrawingBinding

    private val viewModel by lazy {
        ViewModelProvider(this, InjectorUtil.getPaintModelFactory()).get(DrawingViewModel::class.java)
    }

    private val wordId
        get() = intent?.getLongExtra(WORD_ID, EaseWordDB.INVALID_ID) ?: EaseWordDB.INVALID_ID

    private val essayId
        get() = intent?.getLongExtra(ESSAY_ID, EaseWordDB.INVALID_ID) ?: EaseWordDB.INVALID_ID

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_drawing)
        binding.lifecycleOwner = this
        binding.viewModel = viewModel
        binding.freeDrawView.apply {
            this.setPathRedoUndoCountChangeListener(object : PathRedoUndoCountChangeListener {
                override fun onRedoCountChanged(redoCount: Int) {
                    binding.canRedo = redoCount != 0
                }

                override fun onUndoCountChanged(undoCount: Int) {
                    if (undoCount == 0) {
                        binding.canUndo = false
                        binding.root.rightBtn.isEnabled = false
                    } else {
                        binding.canUndo = true
                        binding.root.rightBtn.isEnabled = true
                    }
                }
            })
            binding.undoBtn.setOnClickListener { undoLast() }
            binding.redoBtn.setOnClickListener { redoLast() }
        }
        FontUtil.setTypefaceForTextView(binding.wordTv, FontUtil.SWJZ)
        viewModel.setWordIdAndEssayId(wordId, essayId)
        viewModel.setCheckedPaintSizeRb(R.id.paintSmallRb)
        setupSupportActionBar()
        observe()
    }

    private fun observe() {
        viewModel.checkedPaintSizeRb.observe(this, Observer {
            binding.freeDrawView.setPaintWidthDp(
                when (it) {
                    R.id.paintSmallRb -> PAINT_SIZE_SMALL
                    R.id.paintMediumRb -> PAINT_SIZE_MEDIUM
                    R.id.paintLargeRb -> PAINT_SIZE_LARGE
                    else -> PAINT_SIZE_SMALL
                }
            )
        })
        viewModel.saving.observe(this, Observer { saving ->
            when (saving) {
                false -> {
                    ToastUtil.showShortToast(R.string.save_success)
                }
                null -> {
                    ToastUtil.showShortToast(R.string.save_fail)
                }
            }
        })
        viewModel.word.observe(this, Observer { word ->
            binding.wordTv.text = word?.text
        })
        viewModel.essay.observe(this, Observer {
            GlideApp
                .with(this)
                .asBitmap()
                .skipMemoryCache(true)
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .load(it.content)
                .into(object : CustomTarget<Bitmap>() {
                    override fun onLoadCleared(placeholder: Drawable?) {}

                    override fun onResourceReady(
                        resource: Bitmap,
                        transition: Transition<in Bitmap>?
                    ) {
                        binding.freeDrawView.setBitmapAsBg(resource)
                    }
                })
        })
        viewModel.loading.observe(this, Observer {
            binding.freeDrawView.isEnabled = it != true
        })
    }

    private fun setupSupportActionBar(title: String? = null) {
        binding.toolbarBinding.apply {
            leftBtn.setImageDrawable(getDrawable(R.drawable.ic_back))
            rightBtn.setImageDrawable(
                getDrawable(R.drawable.ic_yes)!!.also {
                    it.setTint(ContextCompat.getColor(this@DrawingActivity, R.color.colorOrange))
                }
            )
            leftBtn.setOnClickListener { onBackPressed() }
            rightBtn.setOnClickListener {
                binding.freeDrawView.getDrawScreenshot(this@DrawingActivity)
            }
            this.title = title
        }
        setSupportActionBar(binding.toolbarBinding.toolbar)
        val actonBar = supportActionBar ?: return
        actonBar.title = null
    }

    override fun onDrawCreated(draw: Bitmap) {
        viewModel.saveDrawing(draw)
    }

    override fun onDrawCreationError() {
        ToastUtil.showShortToast(R.string.save_fail)
    }

    companion object {

        private const val TAG = "DrawingActivity"

        private const val PAINT_SIZE_SMALL = 3f

        private const val PAINT_SIZE_MEDIUM = 7f

        private const val PAINT_SIZE_LARGE = 9f

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
                Intent(activity, DrawingActivity::class.java).also {
                    it.putExtra(WORD_ID, wordId)
                    it.putExtra(ESSAY_ID, essayId)
                }
            )
        }
    }
}