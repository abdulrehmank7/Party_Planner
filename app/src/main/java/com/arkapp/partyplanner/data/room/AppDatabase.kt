package com.arkapp.partyplanner.data.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.arkapp.partyplanner.data.models.Food
import com.arkapp.partyplanner.data.models.UserLogin

/**
 * Created by Abdul Rehman on 17-05-2020.
 * Contact email - abdulrehman0796@gmail.com
 */

@Database(entities = [UserLogin::class, Food::class], version = 1)
abstract class AppDatabase : RoomDatabase() {


    abstract fun userLoginDao(): UserLoginDao

    abstract fun foodDao(): FoodDao

    companion object {
        private val dbName = "PARTY_PLANNER_DB"

        fun getDatabase(context: Context): AppDatabase {
            return Room.databaseBuilder(
                context.applicationContext,
                AppDatabase::class.java, dbName
            ).build()
        }
    }
}