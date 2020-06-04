package com.arkapp.partyplanner.ui.finalChecklist;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.RecyclerView.ViewHolder;

import com.arkapp.partyplanner.R;
import com.arkapp.partyplanner.data.models.PartyType;
import com.arkapp.partyplanner.databinding.RvPartyTypeBinding;
import com.arkapp.partyplanner.ui.home.PartyTypeViewHolder;

import org.jetbrains.annotations.NotNull;

import java.util.List;

import static com.arkapp.partyplanner.utils.GlideUtilsKt.loadImage;

/**
 * Created by Abdul Rehman on 28-02-2020.
 * Contact email - abdulrehman0796@gmail.com
 */

/**
 * Recycler view adapter of selected party type in the checklist screen
 */

public final class SelectedPartyTypeAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private final List<PartyType> partyTypes;

    public SelectedPartyTypeAdapter(@NotNull List<PartyType> partyTypes) {
        super();
        this.partyTypes = partyTypes;
    }

    @NotNull
    @Override
    public ViewHolder onCreateViewHolder(@NotNull ViewGroup parent, int viewType) {
        return new PartyTypeViewHolder((RvPartyTypeBinding) DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.rv_party_type, parent, false));
    }

    @SuppressLint({"ClickableViewAccessibility", "SetTextI18n"})
    @Override
    public void onBindViewHolder(@NotNull ViewHolder holder, int position) {

        RvPartyTypeBinding binding = ((PartyTypeViewHolder) holder).getViewBinding();
        PartyType party = partyTypes.get(position);
        binding.title.setText(party.getTitle());
        loadImage(binding.icon, party.getResId());

        binding.parent.setEnabled(false);
    }

    public int getItemCount() {
        return this.partyTypes.size();
    }

    public long getItemId(int position) {
        return (long) (partyTypes.get(position)).hashCode();
    }
}
