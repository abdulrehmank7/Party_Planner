package com.arkapp.partyplanner.data.room

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.arkapp.partyplanner.data.models.Caterer

/**
 * Created by Abdul Rehman on 17-05-2020.
 * Contact email - abdulrehman0796@gmail.com
 */

@Dao
interface CatererDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(vararg caterer: Caterer)

    @Query("SELECT * FROM CATERER")
    suspend fun getAllCaterers(): List<Caterer>

    @Query("SELECT * FROM CATERER WHERE price*:guestCount<=:budget")
    suspend fun getAllCaterersInBudget(budget: Double, guestCount: Int): List<Caterer>
}