package com.arkapp.partyplanner.data.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.arkapp.partyplanner.data.models.*

/**
 * Created by Abdul Rehman on 17-05-2020.
 * Contact email - abdulrehman0796@gmail.com
 */


/**
 * This class is used to create SQL db for the app. All tables are created and defined in this class.
 * */
@Database(entities = [
    UserLogin::class,
    Venue::class,
    Caterer::class,
    UnfinishedDetails::class,
    SummaryDetails::class,
    HistorySummary::class], version = 1)
abstract class AppDatabase : RoomDatabase() {

    abstract fun userLoginDao(): UserLoginDao

    abstract fun venueDao(): VenueDao

    abstract fun catererDao(): CatererDao

    abstract fun unfinishedDao(): UnfinishedDetailsDao

    abstract fun summaryDao(): SummaryDetailsDao

    abstract fun historySummaryDao(): HistorySummaryDao

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