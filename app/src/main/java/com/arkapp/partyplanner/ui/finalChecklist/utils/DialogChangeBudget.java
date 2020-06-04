package com.arkapp.partyplanner.ui.finalChecklist.utils;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;

import com.arkapp.partyplanner.R;
import com.arkapp.partyplanner.data.models.PartyDetails;
import com.arkapp.partyplanner.data.repository.PrefRepository;
import com.arkapp.partyplanner.databinding.DialogChangeBudgetBinding;

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
 * This dialog is used for changing the budget in the checklist screen
 */
public class DialogChangeBudget extends Dialog {
    private final PrefRepository prefRepository;

    public DialogChangeBudget(@NotNull Context context, @NotNull PrefRepository prefRepository) {
        super(context);
        this.prefRepository = prefRepository;
    }

    protected void onCreate(@Nullable Bundle savedInstanceState) {
        final DialogChangeBudgetBinding binding = DialogChangeBudgetBinding.inflate(LayoutInflater.from(this.getContext()));
        setContentView(binding.getRoot());
        setFullWidth(getWindow());
        setTransparentEdges(getWindow());

        binding.lowBudget.setOnClickListener(v -> {

            binding.lowBudget.setBackground(getDrawableRes(getContext(), R.drawable.bg_selected_start));
            binding.mediumBudget.setBackground(getDrawableRes(getContext(), R.drawable.bg_unselected));
            binding.highBudget.setBackground(getDrawableRes(getContext(), R.drawable.bg_unselected));
            binding.veryHighBudget.setBackground(getDrawableRes(getContext(), R.drawable.bg_unselected_end));

            PartyDetails details = prefRepository.getCurrentPartyDetails();
            details.setPartyBudget(getContext().getString(R.string.low));
            prefRepository.setCurrentPartyDetails(details);
        });

        binding.mediumBudget.setOnClickListener(v -> {

            binding.lowBudget.setBackground(getDrawableRes(getContext(), R.drawable.bg_unselected_start));
            binding.mediumBudget.setBackground(getDrawableRes(getContext(), R.drawable.bg_selected));
            binding.highBudget.setBackground(getDrawableRes(getContext(), R.drawable.bg_unselected));
            binding.veryHighBudget.setBackground(getDrawableRes(getContext(), R.drawable.bg_unselected_end));

            PartyDetails details = prefRepository.getCurrentPartyDetails();
            details.setPartyBudget(getContext().getString(R.string.medium));
            prefRepository.setCurrentPartyDetails(details);
        });

        binding.highBudget.setOnClickListener(v -> {

            binding.lowBudget.setBackground(getDrawableRes(getContext(), R.drawable.bg_unselected_start));
            binding.mediumBudget.setBackground(getDrawableRes(getContext(), R.drawable.bg_unselected));
            binding.highBudget.setBackground(getDrawableRes(getContext(), R.drawable.bg_selected));
            binding.veryHighBudget.setBackground(getDrawableRes(getContext(), R.drawable.bg_unselected_end));

            PartyDetails details = prefRepository.getCurrentPartyDetails();
            details.setPartyBudget(getContext().getString(R.string.high));
            prefRepository.setCurrentPartyDetails(details);
        });

        binding.veryHighBudget.setOnClickListener(v -> {

            binding.lowBudget.setBackground(getDrawableRes(getContext(), R.drawable.bg_unselected_start));
            binding.mediumBudget.setBackground(getDrawableRes(getContext(), R.drawable.bg_unselected));
            binding.highBudget.setBackground(getDrawableRes(getContext(), R.drawable.bg_unselected));
            binding.veryHighBudget.setBackground(getDrawableRes(getContext(), R.drawable.bg_selected_end));

            PartyDetails details = prefRepository.getCurrentPartyDetails();
            details.setPartyBudget(getContext().getString(R.string.very_high));
            prefRepository.setCurrentPartyDetails(details);
        });

        binding.doneBtn.setOnClickListener(v -> dismiss());
    }
}
