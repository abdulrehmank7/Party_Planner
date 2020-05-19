package com.arkapp.partyplanner.data.room

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.arkapp.partyplanner.data.models.Venue

/**
 * Created by Abdul Rehman on 17-05-2020.
 * Contact email - abdulrehman0796@gmail.com
 */

@Dao
interface VenueDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(vararg venue: Venue)

    @Query("SELECT * FROM VENUE")
    suspend fun getAllVenues(): List<Venue>
}