package org.bedelibry.mobile

import android.app.Application

class MyApplication : Application() {
    // Called when the application is starting, before any other application objects have been created.
    // Overriding this method is totally optional!

    var appConfig: AppConfig? = null

    override fun onCreate() {
        super.onCreate()
        // Required initialization logic here!\
        val dir = filesDir.absolutePath
        this.appConfig = AppConfig(dir)
    }
}