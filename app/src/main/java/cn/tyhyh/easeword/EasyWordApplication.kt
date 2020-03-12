package cn.tyhyh.easeword

import android.app.Application

class EasyWordApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        instance = this
    }

    companion object {

        lateinit var instance: EasyWordApplication

        fun getApplication() = instance
    }
}