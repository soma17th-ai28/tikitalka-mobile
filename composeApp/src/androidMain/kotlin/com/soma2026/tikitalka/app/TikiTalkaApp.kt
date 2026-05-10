package com.soma2026.tikitalka.app

import android.app.Application
import com.soma2026.tikitalka.di.initKoin
import org.koin.android.ext.koin.androidContext

class TikiTalkaApp : Application() {
    override fun onCreate() {
        super.onCreate()
        initKoin {
            androidContext(this@TikiTalkaApp)
        }
    }
}