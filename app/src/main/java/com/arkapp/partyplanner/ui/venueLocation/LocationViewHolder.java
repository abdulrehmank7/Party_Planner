package com.arkapp.partyplanner.ui.venueLocation;

import androidx.recyclerview.widget.RecyclerView.ViewHolder;

import com.arkapp.partyplanner.databinding.RvLocationBinding;

import org.jetbrains.annotations.NotNull;

public final class LocationViewHolder extends ViewHolder {
    @NotNull
    private final RvLocationBinding viewBinding;

    public LocationViewHolder(@NotNull RvLocationBinding viewBinding) {
        super(viewBinding.getRoot());
        this.viewBinding = viewBinding;
    }

    @NotNull
    public final RvLocationBinding getViewBinding() {
        return this.viewBinding;
    }
}
