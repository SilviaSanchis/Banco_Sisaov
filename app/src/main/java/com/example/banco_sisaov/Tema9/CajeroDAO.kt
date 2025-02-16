package com.example.banco_sisaov.Tema9

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update

@Dao
interface CajeroDAO {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(CajeroEntityList: List<CajeroEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun addCajero(cajeroEntity: CajeroEntity)

    @Delete
    fun deleteCajero(cajeroEntity: CajeroEntity)

    @Update
    fun updateCajero(cajeroEntity: CajeroEntity)

    @Query("SELECT * FROM cajeros")
    fun getAllCajeros(): MutableList<CajeroEntity>
}