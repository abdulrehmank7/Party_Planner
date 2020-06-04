package com.arkapp.partyplanner.ui.historySummary;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.databinding.DataBindingUtil;
import androidx.navigation.NavController;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.RecyclerView.ViewHolder;

import com.arkapp.partyplanner.R;
import com.arkapp.partyplanner.data.models.HistorySummary;
import com.arkapp.partyplanner.data.models.PartyDetails;
import com.arkapp.partyplanner.data.repository.PrefRepository;
import com.arkapp.partyplanner.databinding.RvHistorySummaryBinding;
import com.arkapp.partyplanner.utils.AppDataKt;

import org.jetbrains.annotations.NotNull;

import java.util.List;

import kotlin.jvm.internal.Intrinsics;

import static com.arkapp.partyplanner.utils.ViewUtilsKt.getFormattedDate;

/**
 * Created by Abdul Rehman on 28-02-2020.
 * Contact email - abdulrehman0796@gmail.com
 */

/**
 * Recycler view adapter of history screen
 */
public final class HistorySummaryListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private final List<HistorySummary> foodList;
    private final NavController navController;
    private final PrefRepository prefRepository;

    public HistorySummaryListAdapter(@NotNull List<HistorySummary> foodList, @NotNull NavController navController, @NotNull PrefRepository prefRepository) {
        super();
        this.foodList = foodList;
        this.navController = navController;
        this.prefRepository = prefRepository;
    }

    @NotNull
    @Override
    public ViewHolder onCreateViewHolder(@NotNull ViewGroup parent, int viewType) {
        Intrinsics.checkParameterIsNotNull(parent, "parent");
        return (ViewHolder) (new HistorySummaryViewHolder((RvHistorySummaryBinding) DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.rv_history_summary, parent, false)));
    }

    @SuppressLint({"ClickableViewAccessibility", "SetTextI18n"})
    @Override
    public void onBindViewHolder(@NotNull ViewHolder holder, int position) {

        RvHistorySummaryBinding binding = ((HistorySummaryViewHolder) holder).getViewBinding();
        HistorySummary summary = foodList.get(position);
        final PartyDetails partyData = AppDataKt.convertPartyFromHistorySummary(summary);

        binding.partyDate.setText(getFormattedDate(partyData.getPartyDate()));
        binding.partyDestination.setText(partyData.getPartyDestination());
        binding.guest.setText(partyData.getPartyGuest() + " Guests");

        //On clicking the history will open the check list screen.
        binding.parent.setOnClickListener(v -> {
            prefRepository.setCurrentPartyDetails(partyData);
            navController.navigate(R.id.action_historySummaryFragment_to_finalChecklistFragment);
        });

    }

    public int getItemCount() {
        return this.foodList.size();
    }

    public long getItemId(int position) {
        return (long) (foodList.get(position)).hashCode();
    }
}
