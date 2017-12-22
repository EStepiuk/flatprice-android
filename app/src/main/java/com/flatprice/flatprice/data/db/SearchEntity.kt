package com.flatprice.flatprice.data.db

import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey
import java.util.*


@Entity(tableName = "search")
data class SearchEntity(var district: String? = null,
                        var square: Int? = null,
                        var room: Int? = null,
                        var date: Date? = null,
                        var price: Int? = null,
                        @PrimaryKey(autoGenerate = true) var id: Long = 0)