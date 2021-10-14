package com.tony.datastoresample

import android.app.Application
import android.util.Log
import com.tencent.mmkv.MMKV

class AppContext : Application() {


    override fun onCreate() {
        super.onCreate()

        val rootDir = MMKV.initialize(this)
        Log.e(TAG, "MMKV 默认保存路径: $rootDir")

    }


    companion object {
        const val TAG = "AppContext"

    }



}