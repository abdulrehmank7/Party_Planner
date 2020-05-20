package com.arkapp.partyplanner.data.room

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.arkapp.partyplanner.data.models.UnfinishedDetails

/**
 * Created by Abdul Rehman on 17-05-2020.
 * Contact email - abdulrehman0796@gmail.com
 */

@Dao
interface UnfinishedDetailsDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(vararg unfinishedDetails: UnfinishedDetails)

    @Query("SELECT * FROM UNFINISHED WHERE uid = :userUid")
    suspend fun getUserUnfinished(userUid: Int): List<UnfinishedDetails>
}