package com.arkapp.partyplanner.ui.venueList;

import androidx.recyclerview.widget.RecyclerView.ViewHolder;

import com.arkapp.partyplanner.databinding.RvVenueBinding;

import org.jetbrains.annotations.NotNull;

/**
 * Created by Abdul Rehman on 28-02-2020.
 * Contact email - abdulrehman0796@gmail.com
 */

public final class VenueListViewHolder extends ViewHolder {
    @NotNull
    private final RvVenueBinding viewBinding;

    public VenueListViewHolder(@NotNull RvVenueBinding viewBinding) {
        super(viewBinding.getRoot());
        this.viewBinding = viewBinding;
    }

    @NotNull
    public final RvVenueBinding getViewBinding() {
        return this.viewBinding;
    }
}
