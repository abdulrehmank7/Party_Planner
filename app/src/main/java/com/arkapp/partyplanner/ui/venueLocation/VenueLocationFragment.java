package com.arkapp.partyplanner.ui.venueLocation;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;

import androidx.fragment.app.Fragment;
import androidx.navigation.ActivityKt;

import com.arkapp.partyplanner.R;
import com.arkapp.partyplanner.R.id;
import com.arkapp.partyplanner.data.models.PartyDetails;
import com.arkapp.partyplanner.data.repository.PrefRepository;
import com.arkapp.partyplanner.data.room.AppDatabase.Companion;
import com.arkapp.partyplanner.data.room.SummaryDetailsDao;
import com.arkapp.partyplanner.utils.AddUnfinishedAsyncTask;
import com.arkapp.partyplanner.utils.AppDataKt;
import com.google.android.material.button.MaterialButton;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import static androidx.navigation.fragment.NavHostFragment.findNavController;
import static com.arkapp.partyplanner.utils.AppDataKt.getLocation;
import static com.arkapp.partyplanner.utils.ViewUtilsKt.initGridAdapter;
import static com.arkapp.partyplanner.utils.ViewUtilsKt.toast;


public class VenueLocationFragment extends Fragment {

    private PrefRepository prefRepository;

    public VenueLocationFragment() {
        super(R.layout.fragment_venue_location);
    }

    @Override
    public void onViewCreated(@NotNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        prefRepository = new PrefRepository(requireContext());

        PartyDetails partyDetails = prefRepository.getCurrentPartyDetails();
        partyDetails.getLocations().clear();


        prefRepository.setCurrentPartyDetails(partyDetails);

        //Location Recycler view to show all the location type
        LocationAdapter adapter = new LocationAdapter(getLocation(), prefRepository);
        initGridAdapter(getView().findViewById(id.locationRv), adapter, true, 2);

        if (prefRepository.getCurrentPartyDetails().getCheckedItemList() != null &&
                !prefRepository.getCurrentPartyDetails().getCheckedItemList().isEmpty())
            ((MaterialButton) getView().findViewById(id.proceedBtn)).setText("Done");

        ((MaterialButton) getView().findViewById(id.proceedBtn)).setOnClickListener(v -> {
            if (prefRepository.getCurrentPartyDetails().getCheckedItemList() != null &&
                    !prefRepository.getCurrentPartyDetails().getCheckedItemList().isEmpty()) {
                toast(requireContext(), "Please wait saving data...");
                new UpdateSummaryAsyncTask(requireActivity(), prefRepository).execute();
            } else {
                new AddUnfinishedAsyncTask(requireActivity(), prefRepository).execute();
                findNavController(this).navigate(R.id.action_venueLocationFragment_to_venueListFragment);
            }
        });

    }

    private class UpdateSummaryAsyncTask extends AsyncTask<Void, Void, Void> {
        private final Activity context;
        private final PrefRepository prefRepository;

        public UpdateSummaryAsyncTask(@NotNull Activity context, @NotNull PrefRepository prefRepository) {
            this.context = context;
            this.prefRepository = prefRepository;
        }

        @Nullable
        @Override
        protected Void doInBackground(@NotNull Void... params) {
            SummaryDetailsDao summaryDao = (new Companion()).getDatabase((Context) this.context).summaryDao();
            summaryDao.delete(prefRepository.getCurrentUser().getUid());
            summaryDao.insert(AppDataKt.convertSummary(prefRepository.getCurrentPartyDetails(), prefRepository.getCurrentUser().getUid()));
            return null;
        }

        @SuppressLint({"SetTextI18n"})
        @Override
        protected void onPostExecute(@Nullable Void summaryData) {
            ActivityKt.findNavController(context, R.id.fragment).navigate(R.id.action_venueLocationFragment_to_finalChecklistFragment);
        }
    }
}
