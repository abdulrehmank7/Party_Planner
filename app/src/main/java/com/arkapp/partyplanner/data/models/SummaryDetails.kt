package com.arkapp.partyplanner.data.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Created by Abdul Rehman on 17-05-2020.
 * Contact email - abdulrehman0796@gmail.com
 */

@Entity(tableName = "SUMMARY")
data class SummaryDetails(
    @PrimaryKey(autoGenerate = true)
    val id: Int?,
    @ColumnInfo(name = "uid")
    val uid: Int?,
    @ColumnInfo(name = "partyDate")
    var partyDate: String?,
    @ColumnInfo(name = "partyBudget")
    var partyBudget: String?,
    @ColumnInfo(name = "partyDestination")
    var partyDestination: String?,
    @ColumnInfo(name = "partyGuest")
    var partyGuest: Int?,
    @ColumnInfo(name = "partyType")
    var partyType: String?,
    @ColumnInfo(name = "selectedFood")
    var selectedFood: String?,
    @ColumnInfo(name = "selectedDestination")
    var selectedDestination: String?
)