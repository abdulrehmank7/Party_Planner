package com.arkapp.partyplanner.ui.venueList;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.navigation.ActivityKt;
import androidx.recyclerview.widget.RecyclerView;

import com.arkapp.partyplanner.R;
import com.arkapp.partyplanner.R.id;
import com.arkapp.partyplanner.data.models.Venue;
import com.arkapp.partyplanner.data.repository.PrefRepository;
import com.arkapp.partyplanner.data.room.AppDatabase.Companion;
import com.arkapp.partyplanner.data.room.VenueDao;
import com.arkapp.partyplanner.utils.ViewUtilsKt;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import static java.sql.DriverManager.println;


public class VenueListFragment extends Fragment {
    private PrefRepository prefRepository;


    public VenueListFragment() {
        super(R.layout.fragment_venue_list);
    }

    @Override
    public void onViewCreated(@NotNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        prefRepository = new PrefRepository(requireContext());
        new VenueListFragment.VenueAsyncTask(requireActivity(), prefRepository).execute();
    }

    private static final class VenueAsyncTask extends AsyncTask<Void, Void, List<Venue>> {
        private final Activity context;
        private final PrefRepository prefRepository;

        public VenueAsyncTask(@NotNull Activity context, @NotNull PrefRepository prefRepository) {
            super();
            this.context = context;
            this.prefRepository = prefRepository;
        }

        @Nullable
        @Override
        protected List<Venue> doInBackground(@NotNull Void... params) {
            VenueDao venueDao = (new Companion()).getDatabase((Context) this.context).venueDao();
            return venueDao.getAllVenues();
        }

        @SuppressLint({"SetTextI18n"})
        protected void onPostExecute(@Nullable List<Venue> venueList) {
            ArrayList<String> selectedLocation = prefRepository.getCurrentPartyDetails().getLocations();
            ArrayList<String> selectedPartyType = prefRepository.getCurrentPartyDetails().getPartyType();

            ArrayList<Venue> filteredVenueList = new ArrayList<>();
            Type type = new TypeToken<ArrayList<String>>() {
            }.getType();
            Gson gson = new Gson();


            for (Venue venue : venueList) {
                if (selectedLocation.contains(venue.getLocation())) {
                    println("venue location ${venue.location}");
                    ArrayList<String> venuePartyType = gson.fromJson(venue.getPartyType(), type);
                    for (String partyType : venuePartyType) {
                        if (selectedPartyType.contains(partyType)) {
                            filteredVenueList.add(venue);
                            break;
                        }
                    }
                }
            }
            if (filteredVenueList.isEmpty()) {
                TextView noItemFound = context.findViewById(R.id.noItemFound);
                noItemFound.setText("No venue found with the selection.\nChange selection and try again");
                ViewUtilsKt.show(noItemFound);
                return;
            }

            VenueListAdapter adapter = new VenueListAdapter(context, filteredVenueList, ActivityKt.findNavController(context, R.id.fragment), prefRepository);
            RecyclerView var16 = context.findViewById(id.venueListRv);
            ViewUtilsKt.initVerticalAdapter(var16, adapter, true);
        }

    }
}
