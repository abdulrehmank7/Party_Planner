package com.arkapp.partyplanner.data.room;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.arkapp.partyplanner.data.models.HistorySummary;

import java.util.List;

/**
 * Created by Abdul Rehman on 17-05-2020.
 * Contact email - abdulrehman0796@gmail.com
 */


/**
 * This class is used to define query for the History Summary table
 */

@Dao
public interface HistorySummaryDao {
    @Insert(onConflict = 1)
    void insert(HistorySummary historySummary);

    @Query("SELECT * FROM HISTORY_SUMMARY WHERE uid = :userUid")
    List<HistorySummary> getHistorySummary(int userUid);

    @Query("DELETE FROM HISTORY_SUMMARY WHERE id = :id")
    void delete(int id);
}
