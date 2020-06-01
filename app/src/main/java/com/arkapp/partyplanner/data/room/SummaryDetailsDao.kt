package com.arkapp.partyplanner.data.room

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.arkapp.partyplanner.data.models.SummaryDetails

/**
 * Created by Abdul Rehman on 17-05-2020.
 * Contact email - abdulrehman0796@gmail.com
 */


/**
 * This class is used to define query for the Summary table
 * */

@Dao
interface SummaryDetailsDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(vararg summaryDetails: SummaryDetails)

    @Query("SELECT * FROM SUMMARY WHERE uid = :userUid")
    suspend fun getUserSummary(userUid: Int): List<SummaryDetails>

    @Query("DELETE FROM SUMMARY WHERE uid = :userUid")
    suspend fun delete(userUid: Int)
}