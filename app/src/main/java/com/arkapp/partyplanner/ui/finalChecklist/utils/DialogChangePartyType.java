package com.arkapp.partyplanner.ui.finalChecklist.utils;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;

import com.arkapp.partyplanner.data.repository.PrefRepository;
import com.arkapp.partyplanner.databinding.DialogChangePartyTypeBinding;
import com.arkapp.partyplanner.ui.home.PartyTypeAdapter;
import com.arkapp.partyplanner.utils.AppDataKt;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import static com.arkapp.partyplanner.utils.AppDataKt.getPartyTypeFromStringArray;
import static com.arkapp.partyplanner.utils.ViewUtilsKt.initGridAdapter;
import static com.arkapp.partyplanner.utils.ViewUtilsKt.setFullWidth;
import static com.arkapp.partyplanner.utils.ViewUtilsKt.setTransparentEdges;
import static com.arkapp.partyplanner.utils.ViewUtilsKt.toastShort;
/**
 * Created by Abdul Rehman on 29-05-2020.
 * Contact email - abdulrehman0796@gmail.com
 */

/**
 * This dialog is used for changing the party type in the checklist screen
 */

public final class DialogChangePartyType extends Dialog {
    private final PrefRepository prefRepository;

    public DialogChangePartyType(@NotNull Context context, @NotNull PrefRepository prefRepository) {
        super(context);
        this.prefRepository = prefRepository;
    }

    protected void onCreate(@Nullable Bundle savedInstanceState) {
        DialogChangePartyTypeBinding binding = DialogChangePartyTypeBinding.inflate(LayoutInflater.from(getContext()));
        setContentView(binding.getRoot());

        setFullWidth(getWindow());
        setTransparentEdges(getWindow());

        setCanceledOnTouchOutside(false);
        setCancelable(false);

        //Setting the party type in the recycler view
        PartyTypeAdapter partyTypeAdapter = new PartyTypeAdapter(AppDataKt.getPartyTypes(), prefRepository);
        partyTypeAdapter.selectedPartyType = getPartyTypeFromStringArray(prefRepository.getCurrentPartyDetails().getPartyType());
        initGridAdapter(binding.selectedPartyTypeRv, partyTypeAdapter, true, 3);

        binding.doneBtn.setOnClickListener(v -> {
            if (prefRepository.getCurrentPartyDetails().getPartyType().isEmpty()) {
                toastShort(getContext(), "Please select party type");
                return;
            }
            dismiss();
        });

    }
}
