package com.arkapp.partyplanner.ui.guestList;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;

import androidx.activity.OnBackPressedCallback;
import androidx.fragment.app.Fragment;
import androidx.navigation.ActivityKt;
import androidx.recyclerview.widget.RecyclerView;

import com.arkapp.partyplanner.R;
import com.arkapp.partyplanner.R.id;
import com.arkapp.partyplanner.data.models.PartyDetails;
import com.arkapp.partyplanner.data.repository.PrefRepository;
import com.arkapp.partyplanner.data.room.AppDatabase.Companion;
import com.arkapp.partyplanner.data.room.HistorySummaryDao;
import com.arkapp.partyplanner.data.room.SummaryDetailsDao;
import com.arkapp.partyplanner.utils.AppDataKt;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import static com.arkapp.partyplanner.utils.AppDataKt.OPTION_CHECKLIST;
import static com.arkapp.partyplanner.utils.AppDataKt.OPTION_CREATE;
import static com.arkapp.partyplanner.utils.ViewUtilsKt.initVerticalAdapter;
import static com.arkapp.partyplanner.utils.ViewUtilsKt.toast;
import static java.sql.DriverManager.println;

public final class GuestListFragment extends Fragment {
    private PrefRepository prefRepository;

    public GuestListFragment() {
        super(R.layout.fragment_guest_list);
    }

    public void onViewCreated(@NotNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        prefRepository = new PrefRepository(requireContext());

        AppDataKt.setOPENED_GUEST_LIST(true);

        GuestListAdapter adapter = new GuestListAdapter(AppDataKt.getGUEST_LIST_NAMES());
        RecyclerView rv = getView().findViewById(id.guestListRv);
        initVerticalAdapter(rv, adapter, true);

        //Handling back click
        OnBackPressedCallback callback = new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                // Handle the back button event
                println("guest list back pressed");
                PartyDetails partyDetails = prefRepository.getCurrentPartyDetails();
                partyDetails.setGuestNameList(AppDataKt.getGUEST_LIST_NAMES());
                prefRepository.setCurrentPartyDetails(partyDetails);

                //Storing the guest check list in the SQL when back is pressed.
                this.remove();
                if (AppDataKt.getCURRENT_SELECTED_OPTION() == OPTION_CHECKLIST || AppDataKt.getCURRENT_SELECTED_OPTION() == OPTION_CREATE) {
                    toast(requireContext(), "Please wait... Saving data!");
                    new UpdateSummaryAsyncTask(requireActivity(), prefRepository).execute();
                } else
                    new UpdateHistorySummaryAsyncTask(requireActivity(), prefRepository).execute();
            }
        };
        requireActivity().getOnBackPressedDispatcher().addCallback(getViewLifecycleOwner(), callback);
    }

    private final class UpdateSummaryAsyncTask extends AsyncTask<Void, Void, Void> {
        private final Activity context;
        private final PrefRepository prefRepository;

        public UpdateSummaryAsyncTask(@NotNull Activity context, @NotNull PrefRepository prefRepository) {
            super();
            this.context = context;
            this.prefRepository = prefRepository;
        }

        @Nullable
        @Override
        protected Void doInBackground(@NotNull Void... params) {
            SummaryDetailsDao summaryDao = (new Companion()).getDatabase(context).summaryDao();

            summaryDao.delete(prefRepository.getCurrentUser().getUid());
            summaryDao.insert(AppDataKt.convertSummary(prefRepository.getCurrentPartyDetails(), prefRepository.getCurrentUser().getUid()));
            return null;
        }

        @SuppressLint({"SetTextI18n"})
        @Override
        protected void onPostExecute(@Nullable Void summaryData) {
            ActivityKt.findNavController(this.context, R.id.fragment).navigate(R.id.action_guestListFragment_to_finalChecklistFragment);
        }

    }

    private class UpdateHistorySummaryAsyncTask extends AsyncTask<Void, Void, Void> {
        private final Activity context;
        private final PrefRepository prefRepository;

        public UpdateHistorySummaryAsyncTask(@NotNull Activity context, @NotNull PrefRepository prefRepository) {
            super();
            this.context = context;
            this.prefRepository = prefRepository;
        }

        @Nullable
        @Override
        protected Void doInBackground(@NotNull Void... params) {
            HistorySummaryDao summaryDao = (new Companion()).getDatabase(context).historySummaryDao();

            summaryDao.delete(prefRepository.getCurrentPartyDetails().getId());
            summaryDao.insert(AppDataKt.convertHistorySummary(prefRepository.getCurrentPartyDetails(), prefRepository.getCurrentUser().getUid()));
            return null;
        }

        @SuppressLint({"SetTextI18n"})
        protected void onPostExecute(@Nullable Void summaryList) {
            ActivityKt.findNavController(this.context, id.fragment).navigate(R.id.action_guestListFragment_to_finalChecklistFragment);
        }
    }
}
