package com.arkapp.partyplanner.ui.finalChecklist.utils;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;

import com.arkapp.partyplanner.R;
import com.arkapp.partyplanner.data.models.PartyDetails;
import com.arkapp.partyplanner.data.repository.PrefRepository;
import com.arkapp.partyplanner.databinding.DialogDestinationSelectionBinding;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import static com.arkapp.partyplanner.utils.ViewUtilsKt.getDrawableRes;
import static com.arkapp.partyplanner.utils.ViewUtilsKt.setFullWidth;
import static com.arkapp.partyplanner.utils.ViewUtilsKt.setTransparentEdges;

/**
 * Created by Abdul Rehman on 29-05-2020.
 * Contact email - abdulrehman0796@gmail.com
 */

/**
 * This dialog is used for changing the destination(Home/Other venue) in the checklist screen
 */
public final class DialogDestinationSelection extends Dialog {
    private final PrefRepository prefRepository;

    public DialogDestinationSelection(@NotNull Context context, @NotNull PrefRepository prefRepository) {
        super(context);
        this.prefRepository = prefRepository;
    }

    protected void onCreate(@Nullable Bundle savedInstanceState) {
        final DialogDestinationSelectionBinding binding = DialogDestinationSelectionBinding.inflate(LayoutInflater.from(this.getContext()));
        setContentView(binding.getRoot());

        setFullWidth(getWindow());
        setTransparentEdges(getWindow());

        binding.homeParty.setOnClickListener(v -> {
            binding.homeParty.setBackground(getDrawableRes(getContext(), R.drawable.bg_selected_start));
            binding.venueParty.setBackground(getDrawableRes(getContext(), R.drawable.bg_unselected_end));

            PartyDetails details = prefRepository.getCurrentPartyDetails();
            details.setPartyDestination(getContext().getString(R.string.home));
            prefRepository.setCurrentPartyDetails(details);
        });

        binding.venueParty.setOnClickListener(v -> {
            binding.homeParty.setBackground(getDrawableRes(getContext(), R.drawable.bg_unselected_start));
            binding.venueParty.setBackground(getDrawableRes(getContext(), R.drawable.bg_selected_end));

            PartyDetails details = prefRepository.getCurrentPartyDetails();
            details.setPartyDestination(getContext().getString(R.string.other_venue));
            prefRepository.setCurrentPartyDetails(details);
        });

        binding.doneBtn.setOnClickListener(v -> dismiss());
    }
}
