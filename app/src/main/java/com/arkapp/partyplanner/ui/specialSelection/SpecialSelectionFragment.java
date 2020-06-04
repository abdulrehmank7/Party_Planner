package com.arkapp.partyplanner.ui.specialSelection;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.arkapp.partyplanner.R;
import com.arkapp.partyplanner.R.id;
import com.arkapp.partyplanner.data.models.PartyDetails;
import com.arkapp.partyplanner.data.repository.PrefRepository;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import static androidx.navigation.fragment.NavHostFragment.findNavController;
import static com.arkapp.partyplanner.utils.AppDataKt.PARTY_TYPE_DECORATION;
import static com.arkapp.partyplanner.utils.AppDataKt.PARTY_TYPE_MAGIC_SHOW;
import static com.arkapp.partyplanner.utils.GlideUtilsKt.loadImage;
import static com.arkapp.partyplanner.utils.ViewUtilsKt.hide;
import static com.arkapp.partyplanner.utils.ViewUtilsKt.show;

/**
 * Special option selection fragment for magic show and decoration.
 */
public class SpecialSelectionFragment extends Fragment {
    private PrefRepository prefRepository;

    public SpecialSelectionFragment() {
        super(R.layout.fragment_special_selection);
    }

    @Override
    public void onViewCreated(@NotNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        prefRepository = new PrefRepository(requireContext());

        View magic = getView().findViewById(R.id.magician);
        View decoration = getView().findViewById(R.id.decoration);

        loadImage(magic.findViewById(id.icon), R.drawable.ic_tarot);
        ((TextView) (magic.findViewById(id.title))).setText(PARTY_TYPE_MAGIC_SHOW);

        loadImage(decoration.findViewById(id.icon), R.drawable.ic_balloon);
        ((TextView) (decoration.findViewById(id.title))).setText(PARTY_TYPE_DECORATION);


        magic.setOnClickListener(v -> {
            if (magic.findViewById(id.tick).getVisibility() == View.VISIBLE) {

                PartyDetails details = prefRepository.getCurrentPartyDetails();
                details.getPartyType().remove(PARTY_TYPE_MAGIC_SHOW);
                prefRepository.setCurrentPartyDetails(details);
                hide(magic.findViewById(id.tick));
            } else {
                PartyDetails details = prefRepository.getCurrentPartyDetails();
                details.getPartyType().add(PARTY_TYPE_MAGIC_SHOW);
                prefRepository.setCurrentPartyDetails(details);
                show(magic.findViewById(id.tick));
            }
        });

        decoration.setOnClickListener(v -> {
            if (decoration.findViewById(id.tick).getVisibility() == View.VISIBLE) {
                PartyDetails details = prefRepository.getCurrentPartyDetails();
                details.getPartyType().remove(PARTY_TYPE_DECORATION);
                prefRepository.setCurrentPartyDetails(details);

                hide(decoration.findViewById(id.tick));
            } else {
                PartyDetails details = prefRepository.getCurrentPartyDetails();
                details.getPartyType().add(PARTY_TYPE_DECORATION);
                prefRepository.setCurrentPartyDetails(details);

                show(decoration.findViewById(id.tick));
            }
        });

        getView().findViewById(id.proceedBtn).setOnClickListener(v ->
                findNavController(this).navigate(id.action_specialSelectionFragment_to_finalChecklistFragment));
    }
}
