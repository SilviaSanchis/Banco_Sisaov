package com.example.banco_sisaov.Tema9

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "cajeros")
data class CajeroEntity(
    @PrimaryKey(autoGenerate = true) @ColumnInfo(name = "cid") val id: Int = 0,
    @ColumnInfo(name = "direccion") val direccion: String?,
    @ColumnInfo(name = "latitud") val latitud: Double?,
    @ColumnInfo(name = "longitud") val longitud: Double?,
    @ColumnInfo(name = "zoom") val zoom: String?
)
