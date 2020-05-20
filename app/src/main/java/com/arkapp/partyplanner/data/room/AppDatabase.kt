package com.arkapp.partyplanner.data.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.arkapp.partyplanner.data.models.Food
import com.arkapp.partyplanner.data.models.UnfinishedDetails
import com.arkapp.partyplanner.data.models.UserLogin
import com.arkapp.partyplanner.data.models.Venue

/**
 * Created by Abdul Rehman on 17-05-2020.
 * Contact email - abdulrehman0796@gmail.com
 */

@Database(entities = [UserLogin::class, Food::class, Venue::class, UnfinishedDetails::class],
          version = 1)
abstract class AppDatabase : RoomDatabase() {

    abstract fun userLoginDao(): UserLoginDao

    abstract fun foodDao(): FoodDao

    abstract fun venueDao(): VenueDao

    abstract fun unfinishedDao(): UnfinishedDetailsDao

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