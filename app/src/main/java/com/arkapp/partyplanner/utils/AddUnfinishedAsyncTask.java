package com.arkapp.partyplanner.utils;

import android.app.Activity;
import android.os.AsyncTask;

import com.arkapp.partyplanner.data.repository.PrefRepository;
import com.arkapp.partyplanner.data.room.AppDatabase;
import com.arkapp.partyplanner.data.room.UnfinishedDetailsDao;

import org.jetbrains.annotations.Nullable;

public final class AddUnfinishedAsyncTask extends AsyncTask<Void, Void, Void> {
    private Activity context;
    private PrefRepository prefRepository;

    public AddUnfinishedAsyncTask(Activity context, PrefRepository prefRepository) {
        super();
        this.context = context;
        this.prefRepository = prefRepository;
    }

    @Nullable
    protected Void doInBackground(Void... params) {
        UnfinishedDetailsDao unfinishedDao = (new AppDatabase.Companion()).getDatabase(context).unfinishedDao();
        unfinishedDao.insert(AppDataKt.convertUnfinished(prefRepository.getCurrentPartyDetails(), prefRepository.getCurrentUser().getUid()));
        return null;
    }
}
