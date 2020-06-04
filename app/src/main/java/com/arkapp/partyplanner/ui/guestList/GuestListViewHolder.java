package com.arkapp.partyplanner.ui.guestList;

import androidx.recyclerview.widget.RecyclerView.ViewHolder;

import com.arkapp.partyplanner.databinding.RvGuestListBinding;

import org.jetbrains.annotations.NotNull;

public final class GuestListViewHolder extends ViewHolder {
    @NotNull
    private final RvGuestListBinding binding;

    public GuestListViewHolder(@NotNull RvGuestListBinding binding) {
        super(binding.getRoot());
        this.binding = binding;
    }

    @NotNull
    public final RvGuestListBinding getBinding() {
        return this.binding;
    }
}
