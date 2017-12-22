package com.flatprice.flatprice.data.db

import android.arch.persistence.room.Database
import android.arch.persistence.room.RoomDatabase
import android.arch.persistence.room.TypeConverters


@Database(entities = [(SearchEntity::class)], version = 1)
@TypeConverters(Converters::class)
abstract class SearchDB : RoomDatabase() {
    abstract fun dao(): SearchDao
}