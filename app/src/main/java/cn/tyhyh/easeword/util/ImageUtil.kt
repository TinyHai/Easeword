package cn.tyhyh.easeword.util

import android.app.Activity
import android.content.Context
import android.view.View
import android.widget.ImageView
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import cn.tyhyh.easeword.GlideApp

/**
 * author: tiny
 * created on: 20-3-27 下午4:45
 */
object ImageUtil {

    fun setImage(imageView: ImageView, any: Any) {
        GlideApp.with(imageView).load(any).into(imageView)
    }

    fun setImage(any: Any, image: Any, imageView: ImageView) {
        when (any) {
            is FragmentActivity -> GlideApp.with(any).load(image).into(imageView)
            is Activity -> GlideApp.with(any).load(image).into(imageView)
            is Context -> GlideApp.with(any).load(image).into(imageView)
            is Fragment -> GlideApp.with(any).load(image).into(imageView)
            is View -> GlideApp.with(any).load(image).into(imageView)
        }
    }
}