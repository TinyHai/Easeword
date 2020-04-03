package cn.tyhyh.easeword.util

import android.annotation.SuppressLint
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.WindowManager
import android.widget.Toast
import cn.tyhyh.easeword.EaseWordApplication
import cn.tyhyh.easeword.databinding.ToastTextWithActionBinding

@SuppressLint("InflateParams")
object ToastUtil {
    private val context = EaseWordApplication.getApplication().applicationContext

    private val toast = Toast.makeText(context, "", Toast.LENGTH_SHORT)

    private lateinit var actionToastViewBinding: ToastTextWithActionBinding

    private val actionToast = {
        val layoutInflater = LayoutInflater.from(context)
        actionToastViewBinding = ToastTextWithActionBinding.inflate(layoutInflater)
        val toast = Toast.makeText(context, "", Toast.LENGTH_SHORT)
        val view = toast.view
        toast.view = actionToastViewBinding.root
        try {
            val mTnField = toast::class.java.getDeclaredField("mTN").also {
                if (!it.isAccessible) it.isAccessible = true
            }
            val mTN = mTnField.get(toast)
            if (mTN != null) {
                val mParamField = mTN::class.java.getDeclaredField("mParams").also {
                    if (!it.isAccessible) {
                        it.isAccessible = true
                    }
                }
                val mParam = mParamField.get(mTN)
                if (mParam is WindowManager.LayoutParams) {
                    mParam.flags = WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON or WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        toast
    }()

    fun showShortToast(msgResId: Int) {
        showShortToast(context.getString(msgResId))
    }

    fun showShortToast(msg: CharSequence) {
        val view = toast.view
        Log.d(TAG, view.toString())
        toast.setText(msg)
        toast.duration = Toast.LENGTH_SHORT
        toast.show()
    }

    fun showLongToast(msgResId: Int) {
        showLongToast(context.getString(msgResId))
    }

    fun showLongToast(msg: CharSequence) {
        toast.setText(msg)
        toast.duration = Toast.LENGTH_LONG
        toast.show()
    }

    fun showShortActionToast(msgResId: Int, actionNameResId: Int, action: (v: View) -> Unit) {
        showShortActionToast(context.getString(msgResId), context.getString(actionNameResId), action)
    }

    fun showShortActionToast(msg: CharSequence, actionMsg: CharSequence, action: (v: View) -> Unit) {
        actionToastViewBinding.textTv.text = msg
        actionToastViewBinding.actionBtn.text = actionMsg
        actionToastViewBinding.actionBtn.setOnClickListener {
            action(it)
            it.setOnClickListener(null)
        }
        actionToast.duration = Toast.LENGTH_SHORT
        actionToast.show()
    }

    fun showLongActionToast(msgResId: Int, actionNameResId: Int,  action: (v: View) -> Unit) {
        showLongActionToast(context.getString(msgResId), context.getString(actionNameResId), action)
    }

    fun showLongActionToast(msg: CharSequence, actionMsg: CharSequence,  action: (v: View) -> Unit) {
        actionToastViewBinding.textTv.text = msg
        actionToastViewBinding.actionBtn.text = actionMsg
        actionToastViewBinding.actionBtn.setOnClickListener(action)
        actionToast.duration = Toast.LENGTH_LONG
        actionToast.show()
    }

    private const val TAG = "ToastUtil"
}