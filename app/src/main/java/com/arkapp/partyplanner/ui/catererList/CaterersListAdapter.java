package com.arkapp.partyplanner.ui.catererList;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.databinding.DataBindingUtil;
import androidx.navigation.ActivityKt;
import androidx.navigation.NavController;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.RecyclerView.ViewHolder;

import com.arkapp.partyplanner.R;
import com.arkapp.partyplanner.data.models.Caterer;
import com.arkapp.partyplanner.data.models.PartyDetails;
import com.arkapp.partyplanner.data.repository.PrefRepository;
import com.arkapp.partyplanner.data.room.AppDatabase.Companion;
import com.arkapp.partyplanner.data.room.SummaryDetailsDao;
import com.arkapp.partyplanner.databinding.RvCatererBinding;
import com.arkapp.partyplanner.utils.AddUnfinishedAsyncTask;
import com.arkapp.partyplanner.utils.AppDataKt;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public final class CaterersListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private final Activity context;
    private final List<Caterer> caterersList;
    private final NavController navController;
    private final PrefRepository prefRepository;

    private final Gson gson = new Gson();
    private final Type type = new TypeToken<ArrayList<String>>() {
    }.getType();

    public CaterersListAdapter(@NotNull Activity context, @NotNull List<Caterer> caterersList, @NotNull NavController navController, @NotNull PrefRepository prefRepository) {
        this.context = context;
        this.caterersList = caterersList;
        this.navController = navController;
        this.prefRepository = prefRepository;
    }

    @NotNull
    public ViewHolder onCreateViewHolder(@NotNull ViewGroup parent, int viewType) {
        return (ViewHolder) (new CaterersListViewHolder((RvCatererBinding)
                DataBindingUtil
                        .inflate(
                                LayoutInflater.from(parent.getContext()),
                                R.layout.rv_caterer,
                                parent,
                                false)));
    }

    @SuppressLint({"ClickableViewAccessibility", "SetTextI18n"})
    public void onBindViewHolder(@NotNull ViewHolder holder, int position) {
        RvCatererBinding binding = ((CaterersListViewHolder) holder).getViewBinding();
        final Caterer caterersData = (Caterer) this.caterersList.get(position);

        binding.name.setText(caterersData.getName().trim());
        binding.price.setText("$" + caterersData.getPricePerPax());
        binding.totalGuestPriceTv.setText(prefRepository.getCurrentPartyDetails().getPartyGuest() + " Pax total");
        binding.totalGuestPrice.setText("$" + (caterersData.getPricePerPax() * prefRepository.getCurrentPartyDetails().getPartyGuest()));

        //This will show all the supported party type of the caterer
        ArrayList<String> partyTypes = gson.fromJson(caterersData.getPartyType(), type);
        StringBuilder partyTypeString = new StringBuilder();

        for (String x : partyTypes) {
            partyTypeString.append(x + ", ");
        }
        binding.partyTypeValue.setText(partyTypeString.toString().substring(0, partyTypeString.toString().length() - 2));

        //On clicking the caterer it open the different screen according to party detail
        binding.parent.setOnClickListener(v -> {
            PartyDetails details = prefRepository.getCurrentPartyDetails();
            details.setSelectedCaterer(caterersData);
            prefRepository.setCurrentPartyDetails(details);

            if (prefRepository.getCurrentPartyDetails().getPartyDestination().equals(context.getString(R.string.home)))
                navController.navigate(R.id.action_caterersListFragment_to_specialSelectionFragment);

            else if (prefRepository.getCurrentPartyDetails().getCheckedItemList() != null &&
                    !prefRepository.getCurrentPartyDetails().getCheckedItemList().isEmpty())
                new UpdateSummaryAsyncTask(context, prefRepository).execute();
            else {
                navController.navigate(R.id.action_caterersListFragment_to_venueLocationFragment);
                new AddUnfinishedAsyncTask(context, prefRepository).execute();
            }
        });
    }

    public int getItemCount() {
        return this.caterersList.size();
    }

    public long getItemId(int position) {
        return (long) (caterersList.get(position)).hashCode();
    }

    private static final class UpdateSummaryAsyncTask extends AsyncTask<Void, Void, Void> {
        private final Activity context;
        private final PrefRepository prefRepository;

        public UpdateSummaryAsyncTask(@NotNull Activity context, @NotNull PrefRepository prefRepository) {
            this.context = context;
            this.prefRepository = prefRepository;
        }

        @Override
        protected Void doInBackground(@NotNull Void... params) {
            SummaryDetailsDao summaryDao = (new Companion()).getDatabase(context).summaryDao();
            summaryDao.delete(prefRepository.getCurrentUser().getUid());
            summaryDao.insert(AppDataKt.convertSummary(
                    prefRepository.getCurrentPartyDetails(),
                    prefRepository.getCurrentUser().getUid()));
            return null;
        }

        @SuppressLint({"SetTextI18n"})
        @Override
        protected void onPostExecute(@Nullable Void summaryData) {
            ActivityKt.findNavController(context, R.id.fragment).navigate(R.id.action_caterersListFragment_to_finalChecklistFragment);
        }
    }
}
