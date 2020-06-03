package com.arkapp.partyplanner.data.room;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.arkapp.partyplanner.data.models.Caterer;

import org.jetbrains.annotations.NotNull;

import java.util.List;

/**
 * Created by Abdul Rehman on 17-05-2020.
 * Contact email - abdulrehman0796@gmail.com
 */


/**
 * This class is used to define query for the Caterer table
 */
@Dao
public interface CatererDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insert(@NotNull Caterer var2);

    @Query("SELECT * FROM CATERER")
    List<Caterer> getAllCaterers();

    @Query("SELECT * FROM CATERER WHERE price*:guestCount<=:budget")
    List<Caterer> getAllCaterersInBudget(double budget, int guestCount);
}
