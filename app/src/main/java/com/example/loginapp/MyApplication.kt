package com.example.loginapp

import android.app.Application
import android.content.Context
import androidx.room.Room
import com.example.loginapp.model.AppDataBase


class MyApplication : Application() {
    private var _appDataBase: AppDataBase? = null
    val appDataBase get() = requireNotNull(_appDataBase)

    override fun onCreate() {
        super.onCreate()
        _appDataBase = Room
            .databaseBuilder(
                this,
                AppDataBase::class.java,
                "app_database"
            )
            .allowMainThreadQueries()
            .build()
    }
}

val Context.appDatabase: AppDataBase
    get() = when {
        this is MyApplication -> appDataBase
        else -> applicationContext.appDatabase
    }