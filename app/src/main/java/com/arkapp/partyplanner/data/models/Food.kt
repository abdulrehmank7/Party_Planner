package com.arkapp.partyplanner.data.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Created by Abdul Rehman on 17-05-2020.
 * Contact email - abdulrehman0796@gmail.com
 */

@Entity(tableName = "FOOD_TYPE")
data class Food(
    @PrimaryKey(autoGenerate = true)
    val uid: Int?,
    @ColumnInfo(name = "name")
    val name: String,
    @ColumnInfo(name = "price")
    val price: Double,
    @ColumnInfo(name = "resId")
    val resId: Int,
    @ColumnInfo(name = "foodType")
    val foodType: String
)