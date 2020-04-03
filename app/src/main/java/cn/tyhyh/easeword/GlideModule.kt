package cn.tyhyh.easeword

import android.content.Context
import com.bumptech.glide.GlideBuilder
import com.bumptech.glide.annotation.GlideModule
import com.bumptech.glide.load.engine.cache.InternalCacheDiskCacheFactory
import com.bumptech.glide.module.AppGlideModule

/**
 * author: tiny
 * created on: 20-3-27 下午4:43
 */
@GlideModule
class GlideModule : AppGlideModule() {

    companion object {
        private const val DISK_CACHE_FOLDER_NAME = "glide_cache"
    }

    override fun applyOptions(context: Context, builder: GlideBuilder) {
        val diskCacheSize = 1024 * 1024 * 50L
        builder.setDiskCache(InternalCacheDiskCacheFactory(context, DISK_CACHE_FOLDER_NAME, diskCacheSize))
    }
}