package com.arkapp.partyplanner.data.room;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.arkapp.partyplanner.data.models.Venue;

import java.util.List;

/**
 * Created by Abdul Rehman on 17-05-2020.
 * Contact email - abdulrehman0796@gmail.com
 */

/**
 * This class is used to define query for the Venue table
 */

@Dao
public interface VenueDao {
    @Insert(onConflict = 5)
    void insert(Venue var1);

    @Query("SELECT * FROM VENUE")
    List<Venue> getAllVenues();
}
