package cn.tyhyh.easeword

import android.app.Application
import cn.tyhyh.easeword.data.db.EaseWordDB
import org.litepal.LitePal

class EaseWordApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        instance = this
        LitePal.initialize(this)
        EaseWordDB.initFromAsset()
    }

    companion object {

        private lateinit var instance: EaseWordApplication

        fun getApplication() = instance
    }
}