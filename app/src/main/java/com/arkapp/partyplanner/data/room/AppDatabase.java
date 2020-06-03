package com.arkapp.partyplanner.data.room;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.arkapp.partyplanner.data.models.Caterer;
import com.arkapp.partyplanner.data.models.HistorySummary;
import com.arkapp.partyplanner.data.models.SummaryDetails;
import com.arkapp.partyplanner.data.models.UnfinishedDetails;
import com.arkapp.partyplanner.data.models.UserLogin;
import com.arkapp.partyplanner.data.models.Venue;

import org.jetbrains.annotations.NotNull;

/**
 * Created by Abdul Rehman on 17-05-2020.
 * Contact email - abdulrehman0796@gmail.com
 */


/**
 * This class is used to create SQL db for the app. All tables are created and defined in this class.
 */
@Database(entities = {UserLogin.class, Venue.class, Caterer.class, UnfinishedDetails.class, SummaryDetails.class, HistorySummary.class}, version = 1)
public abstract class AppDatabase extends RoomDatabase {
    private static final String dbName = "PARTY_PLANNER_DB";

    @NotNull
    public abstract UserLoginDao userLoginDao();

    @NotNull
    public abstract VenueDao venueDao();

    @NotNull
    public abstract CatererDao catererDao();

    @NotNull
    public abstract UnfinishedDetailsDao unfinishedDao();

    @NotNull
    public abstract SummaryDetailsDao summaryDao();

    @NotNull
    public abstract HistorySummaryDao historySummaryDao();

    public static final class Companion {
        @NotNull
        public final AppDatabase getDatabase(@NotNull Context context) {
            return Room.databaseBuilder(context.getApplicationContext(), AppDatabase.class, AppDatabase.dbName).build();
        }
    }
}
