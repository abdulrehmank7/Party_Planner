package com.arkapp.partyplanner.data.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Created by Abdul Rehman on 17-05-2020.
 * Contact email - abdulrehman0796@gmail.com
 */

@Entity(tableName = "CATERER")
data class Caterer(
    @PrimaryKey(autoGenerate = true)
    val uid: Int?,
    @ColumnInfo(name = "name")
    val name: String,
    @ColumnInfo(name = "website")
    val website: String,
    @ColumnInfo(name = "contact")
    val contact: String,
    @ColumnInfo(name = "price")
    val pricePerPax: Double,
    @ColumnInfo(name = "partyType")
    val partyType: String

)