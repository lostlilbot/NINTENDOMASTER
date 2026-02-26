package com.ndsemulator.app

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

/**
 * Main Application class for NDS Emulator.
 * Uses Hilt for dependency injection.
 */
@HiltAndroidApp
class NDSEmulatorApp : Application() {
    
    override fun onCreate() {
        super.onCreate()
        // Initialize any app-wide components here
    }
}
