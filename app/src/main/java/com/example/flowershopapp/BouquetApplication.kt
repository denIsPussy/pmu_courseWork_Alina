package com.example.flowershopapp

import android.app.Application
import com.example.flowershopapp.Database.AppContainer
import com.example.flowershopapp.Database.AppDataContainer

class BouquetApplication : Application() {
    lateinit var container: AppContainer

    override fun onCreate() {
        super.onCreate()
        container = AppDataContainer(this)
    }
}