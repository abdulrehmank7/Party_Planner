package com.arkapp.partyplanner.data.room

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.arkapp.partyplanner.data.models.Food

/**
 * Created by Abdul Rehman on 17-05-2020.
 * Contact email - abdulrehman0796@gmail.com
 */

@Dao
interface FoodDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(vararg food: Food)

    @Query("SELECT * FROM FOOD_TYPE")
    suspend fun getAllFood(): List<Food>

    @Query("SELECT * FROM FOOD_TYPE WHERE price <= :budgetLimit AND foodType NOT IN ('ALCOHOL')")
    suspend fun getFoodListWithoutAlcohol(budgetLimit: Double): List<Food>

    @Query("SELECT * FROM FOOD_TYPE WHERE price <= :budgetLimit")
    suspend fun getFoodListWithAlcohol(budgetLimit: Double): List<Food>
}