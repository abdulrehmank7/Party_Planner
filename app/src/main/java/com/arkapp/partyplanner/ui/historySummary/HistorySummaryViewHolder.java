package com.arkapp.partyplanner.ui.historySummary;

import androidx.recyclerview.widget.RecyclerView.ViewHolder;

import com.arkapp.partyplanner.databinding.RvHistorySummaryBinding;

import org.jetbrains.annotations.NotNull;

public class HistorySummaryViewHolder extends ViewHolder {
    @NotNull
    private final RvHistorySummaryBinding viewBinding;

    public HistorySummaryViewHolder(@NotNull RvHistorySummaryBinding viewBinding) {
        super(viewBinding.getRoot());
        this.viewBinding = viewBinding;
    }

    @NotNull
    public final RvHistorySummaryBinding getViewBinding() {
        return this.viewBinding;
    }
}
