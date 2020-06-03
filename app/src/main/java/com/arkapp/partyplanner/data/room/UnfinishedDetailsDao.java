package com.arkapp.partyplanner.data.room;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.arkapp.partyplanner.data.models.UnfinishedDetails;

import java.util.List;

/**
 * Created by Abdul Rehman on 17-05-2020.
 * Contact email - abdulrehman0796@gmail.com
 */

/**
 * This class is used to define query for the Unfinished data table
 */
@Dao
public interface UnfinishedDetailsDao {
    @Insert(onConflict = 1)
    void insert(UnfinishedDetails unfinishedDetails);

    @Query("SELECT * FROM UNFINISHED WHERE uid = :userUid")
    List<UnfinishedDetails> getUserUnfinished(int userUid);

    @Query("DELETE FROM UNFINISHED WHERE uid = :userUid")
    void delete(int userUid);
}
