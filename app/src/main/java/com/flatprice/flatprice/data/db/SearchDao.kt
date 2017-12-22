package com.flatprice.flatprice.data.db

import android.arch.persistence.room.Dao
import android.arch.persistence.room.Delete
import android.arch.persistence.room.Insert
import android.arch.persistence.room.Query


@Dao
interface SearchDao {

    @Insert
    fun insert(entity: SearchEntity)

    @Query("SELECT * FROM search ORDER BY date DESC")
    fun queryAll(): List<SearchEntity>

    @Delete
    fun delete(entity: SearchEntity)
}