package com.example.banco_sisaov.Tema9

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [CajeroEntity::class], version = 1)
abstract class CajerosDataBase : RoomDatabase(){
    abstract fun cajeroDao(): CajeroDAO
}