package com.arkapp.partyplanner.data.room;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.arkapp.partyplanner.data.models.SummaryDetails;

import java.util.List;

/**
 * Created by Abdul Rehman on 17-05-2020.
 * Contact email - abdulrehman0796@gmail.com
 */


/**
 * This class is used to define query for the Summary table
 */

@Dao
public interface SummaryDetailsDao {
    @Insert(onConflict = 1)
    void insert(SummaryDetails summaryDetails);

    @Query("SELECT * FROM SUMMARY WHERE uid = :userUid")
    List<SummaryDetails> getUserSummary(int userUid);

    @Query("DELETE FROM SUMMARY WHERE uid = :userUid")
    void delete(int userUid);
}
