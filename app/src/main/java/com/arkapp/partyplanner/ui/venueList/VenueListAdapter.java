package com.arkapp.partyplanner.ui.venueList;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.databinding.DataBindingUtil;
import androidx.navigation.ActivityKt;
import androidx.navigation.NavController;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.RecyclerView.ViewHolder;

import com.arkapp.partyplanner.R;
import com.arkapp.partyplanner.data.models.PartyDetails;
import com.arkapp.partyplanner.data.models.Venue;
import com.arkapp.partyplanner.data.repository.PrefRepository;
import com.arkapp.partyplanner.data.room.AppDatabase.Companion;
import com.arkapp.partyplanner.data.room.SummaryDetailsDao;
import com.arkapp.partyplanner.databinding.RvVenueBinding;
import com.arkapp.partyplanner.utils.AppDataKt;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import static com.arkapp.partyplanner.utils.ViewUtilsKt.toast;


public final class VenueListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private final Gson gson = new Gson();
    private final Type type = new TypeToken<ArrayList<String>>() {
    }.getType();

    private final Activity context;
    private final List<Venue> venueList;
    private final NavController navController;
    private final PrefRepository prefRepository;

    public VenueListAdapter(@NotNull Activity context, @NotNull List<Venue> venueList, @NotNull NavController navController, @NotNull PrefRepository prefRepository) {
        super();
        this.context = context;
        this.venueList = venueList;
        this.navController = navController;
        this.prefRepository = prefRepository;
    }

    @NotNull
    public ViewHolder onCreateViewHolder(@NotNull ViewGroup parent, int viewType) {
        return (ViewHolder) (new VenueListViewHolder((RvVenueBinding) DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.rv_venue, parent, false)));
    }

    @SuppressLint({"ClickableViewAccessibility", "SetTextI18n"})
    public void onBindViewHolder(@NotNull ViewHolder holder, int position) {
        RvVenueBinding binding = ((VenueListViewHolder) holder).getViewBinding();
        final Venue venueData = venueList.get(position);

        binding.venueName.setText(venueData.getName());
        binding.venueAdd.setText(venueData.getAddress());
        binding.capacity.setText(venueData.getCapacity() + " Guest");
        binding.contact.setText(venueData.getContact());
        binding.price.setText("$" + venueData.getPrice());
        binding.location.setText(venueData.getLocation());

        ArrayList<String> partyTypes = gson.fromJson(venueData.getPartyType(), type);
        StringBuilder partyTypeString = new StringBuilder();

        for (String x : partyTypes) {
            partyTypeString.append(x + ", ");
        }
        binding.suitable.setText(partyTypeString.toString().substring(0, partyTypeString.toString().length() - 2));


        binding.parent.setOnClickListener(view -> {
            PartyDetails details = prefRepository.getCurrentPartyDetails();
            details.setSelectedDestination(venueData);
            prefRepository.setCurrentPartyDetails(details);

            if (prefRepository.getCurrentPartyDetails().getCheckedItemList() != null &&
                    !prefRepository.getCurrentPartyDetails().getCheckedItemList().isEmpty()) {
                toast(context, "Please wait saving data...");
                new UpdateSummaryAsyncTask(context, prefRepository).execute();
            } else
                navController.navigate(R.id.action_venueListFragment_to_specialSelectionFragment);
        });
    }

    public int getItemCount() {
        return this.venueList.size();
    }

    public long getItemId(int position) {
        return (long) (venueList.get(position)).hashCode();
    }

    private static final class UpdateSummaryAsyncTask extends AsyncTask<Void, Void, Void> {
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
            SummaryDetailsDao var10000 = (new Companion()).getDatabase((Context) context).summaryDao();
            var10000.delete(prefRepository.getCurrentUser().getUid());
            var10000.insert(AppDataKt.convertSummary(prefRepository.getCurrentPartyDetails(), prefRepository.getCurrentUser().getUid()));
            return null;
        }

        @SuppressLint({"SetTextI18n"})
        @Override
        protected void onPostExecute(@Nullable Void summaryData) {
            ActivityKt.findNavController(context, R.id.fragment).navigate(R.id.action_venueListFragment_to_finalChecklistFragment);
        }
    }
}
