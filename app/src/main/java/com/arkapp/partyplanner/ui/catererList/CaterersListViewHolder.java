package com.arkapp.partyplanner.ui.catererList;

import androidx.recyclerview.widget.RecyclerView.ViewHolder;

import com.arkapp.partyplanner.databinding.RvCatererBinding;

import org.jetbrains.annotations.NotNull;


public final class CaterersListViewHolder extends ViewHolder {
    @NotNull
    private final RvCatererBinding viewBinding;

    public CaterersListViewHolder(@NotNull RvCatererBinding viewBinding) {
        super(viewBinding.getRoot());
        this.viewBinding = viewBinding;
    }

    @NotNull
    public final RvCatererBinding getViewBinding() {
        return this.viewBinding;
    }
}
