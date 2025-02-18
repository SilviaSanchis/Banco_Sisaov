package com.example.banco_sisaov.Tema9

import android.app.Application
import androidx.room.Room

class CajeroApplication: Application() {
    companion object {
        lateinit var dataBase: CajerosDataBase
    }

    override fun onCreate() {
        super.onCreate()

        dataBase = Room.databaseBuilder(
            this,
            CajerosDataBase::class.java,
            "CajeroDataBase"
        ).build()
    }
}