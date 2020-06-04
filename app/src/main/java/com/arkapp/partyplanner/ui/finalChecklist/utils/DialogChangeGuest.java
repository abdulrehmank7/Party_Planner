package com.arkapp.partyplanner.ui.finalChecklist.utils;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;

import com.arkapp.partyplanner.data.models.PartyDetails;
import com.arkapp.partyplanner.data.repository.PrefRepository;
import com.arkapp.partyplanner.databinding.DialogChangeGuestBinding;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import static com.arkapp.partyplanner.utils.ViewUtilsKt.setFullWidth;
import static com.arkapp.partyplanner.utils.ViewUtilsKt.setTransparentEdges;

/**
 * Created by Abdul Rehman on 29-05-2020.
 * Contact email - abdulrehman0796@gmail.com
 */

/**
 * This dialog is used for changing the guest count in the checklist screen
 */

public class DialogChangeGuest extends Dialog {

    private final PrefRepository prefRepository;

    public DialogChangeGuest(@NotNull Context context, @NotNull PrefRepository prefRepository) {
        super(context);
        this.prefRepository = prefRepository;
    }

    protected void onCreate(@Nullable Bundle savedInstanceState) {
        final DialogChangeGuestBinding binding = DialogChangeGuestBinding.inflate(LayoutInflater.from(getContext()));
        setContentView(binding.getRoot());

        setFullWidth(getWindow());
        setTransparentEdges(getWindow());

        binding.guestEt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (!TextUtils.isEmpty(s.toString())) {
                    binding.guest.setError(null);
                    PartyDetails details = prefRepository.getCurrentPartyDetails();
                    details.setPartyGuest(Integer.parseInt(s.toString()));
                    prefRepository.setCurrentPartyDetails(details);
                }
            }
        });


        binding.doneBtn.setOnClickListener(v -> {
            if (binding.guestEt.getText().toString().isEmpty() ||
                    Integer.parseInt(binding.guestEt.getText().toString()) <= 0) {
                binding.guest.setError("Please enter guest count!");
                return;
            }

            dismiss();
        });
    }
}
