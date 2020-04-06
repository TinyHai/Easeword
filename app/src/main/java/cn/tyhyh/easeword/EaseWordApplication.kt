package cn.tyhyh.easeword

import android.app.Application
import org.litepal.LitePal

class EaseWordApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        instance = this
        LitePal.initialize(this)
    }

    companion object {

        private lateinit var instance: EaseWordApplication

        fun getApplication() = instance
    }
}