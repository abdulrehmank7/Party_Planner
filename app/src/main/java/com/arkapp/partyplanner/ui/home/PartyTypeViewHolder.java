package com.arkapp.partyplanner.ui.home;

import androidx.recyclerview.widget.RecyclerView.ViewHolder;

import com.arkapp.partyplanner.databinding.RvPartyTypeBinding;

import org.jetbrains.annotations.NotNull;

public class PartyTypeViewHolder extends ViewHolder {
    @NotNull
    private final RvPartyTypeBinding viewBinding;

    public PartyTypeViewHolder(@NotNull RvPartyTypeBinding viewBinding) {
        super(viewBinding.getRoot());
        this.viewBinding = viewBinding;
    }

    @NotNull
    public final RvPartyTypeBinding getViewBinding() {
        return this.viewBinding;
    }
}
