package com.arkapp.partyplanner.data.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Created by Abdul Rehman on 17-05-2020.
 * Contact email - abdulrehman0796@gmail.com
 */


/**
 * Unfinished SQL table is created using the following class definition
 * This sql table is used for storing unfinished checklist.
 * */

@Entity(tableName = "UNFINISHED")
data class UnfinishedDetails(
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
    @ColumnInfo(name = "selectedDestination")
    var selectedDestination: String?,
    @ColumnInfo(name = "selectedCaterer")
    var selectedCaterers: String?,
    @ColumnInfo(name = "extraNote")
    var extraNote: String?,
    @ColumnInfo(name = "guestNameList")
    var guestNameList: String?,
    @ColumnInfo(name = "checkedItemList")
    var checkedItemList: String?,
    @ColumnInfo(name = "locations")
    var locations: String?
)