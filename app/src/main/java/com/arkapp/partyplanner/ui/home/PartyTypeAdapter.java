package com.arkapp.partyplanner.ui.home;

import android.annotation.SuppressLint;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.RecyclerView.ViewHolder;

import com.arkapp.partyplanner.R;
import com.arkapp.partyplanner.data.models.PartyDetails;
import com.arkapp.partyplanner.data.models.PartyType;
import com.arkapp.partyplanner.data.repository.PrefRepository;
import com.arkapp.partyplanner.databinding.RvPartyTypeBinding;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

import static com.arkapp.partyplanner.utils.GlideUtilsKt.loadImage;
import static com.arkapp.partyplanner.utils.ViewUtilsKt.hide;
import static com.arkapp.partyplanner.utils.ViewUtilsKt.show;

public class PartyTypeAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    public ArrayList<PartyType> selectedPartyType = new ArrayList<>();
    private List<PartyType> partyTypes;
    private PrefRepository prefRepository;

    public PartyTypeAdapter(@NotNull List<PartyType> partyTypes, @NotNull PrefRepository prefRepository) {
        super();
        this.partyTypes = partyTypes;
        this.prefRepository = prefRepository;
    }

    @NotNull
    @Override
    public ViewHolder onCreateViewHolder(@NotNull ViewGroup parent, int viewType) {
        return (ViewHolder) (new PartyTypeViewHolder((RvPartyTypeBinding) DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.rv_party_type, parent, false)));
    }

    @SuppressLint({"ClickableViewAccessibility", "SetTextI18n"})
    @Override
    public void onBindViewHolder(@NotNull ViewHolder holder, int position) {
        RvPartyTypeBinding binding = ((PartyTypeViewHolder) holder).getViewBinding();

        PartyType party = partyTypes.get(position);
        binding.title.setText(party.getTitle());
        loadImage(binding.icon, party.getResId());

        Log.d("tagggg", "inside recyceler view party size " + selectedPartyType.size());


        for (PartyType x : selectedPartyType) {
            if (x.getTitle().equals(party.getTitle())) {
                show(binding.tick);
                break;
            }
        }

        binding.parent.setOnClickListener(v -> {
            if (binding.tick.getVisibility() == View.VISIBLE) {
                selectedPartyType.remove(party);
                hide(binding.tick);

                PartyDetails details = prefRepository.getCurrentPartyDetails();
                details.getPartyType().remove(party.getTitle());
                prefRepository.setCurrentPartyDetails(details);

            } else {
                selectedPartyType.add(party);
                show(binding.tick);

                PartyDetails details = prefRepository.getCurrentPartyDetails();
                details.getPartyType().add(party.getTitle());
                prefRepository.setCurrentPartyDetails(details);
            }
        });
    }

    public int getItemCount() {
        return this.partyTypes.size();
    }

    public long getItemId(int position) {
        return (long) ((PartyType) this.partyTypes.get(position)).hashCode();
    }
}
