package com.arkapp.partyplanner.ui.venueLocation;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.RecyclerView.ViewHolder;

import com.arkapp.partyplanner.R;
import com.arkapp.partyplanner.data.models.Location;
import com.arkapp.partyplanner.data.models.PartyDetails;
import com.arkapp.partyplanner.data.repository.PrefRepository;
import com.arkapp.partyplanner.databinding.RvLocationBinding;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

import static com.arkapp.partyplanner.utils.GlideUtilsKt.loadImage;
import static com.arkapp.partyplanner.utils.ViewUtilsKt.hide;
import static com.arkapp.partyplanner.utils.ViewUtilsKt.show;

/**
 * Created by Abdul Rehman on 28-02-2020.
 * Contact email - abdulrehman0796@gmail.com
 */


/**
 * Recycler view adapter of location selection before the venue selection
 */
public class LocationAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private final PrefRepository prefRepository;
    private List<Location> locations;
    private ArrayList<Location> locationSelected = new ArrayList<>();


    public LocationAdapter(@NotNull List<Location> locations, @NotNull PrefRepository prefRepository) {
        super();
        this.locations = locations;
        this.prefRepository = prefRepository;
    }

    @NotNull
    @Override
    public ViewHolder onCreateViewHolder(@NotNull ViewGroup parent, int viewType) {
        return (ViewHolder) (new LocationViewHolder(
                (RvLocationBinding) DataBindingUtil
                        .inflate(LayoutInflater.from(
                                parent.getContext()),
                                R.layout.rv_location,
                                parent,
                                false)));
    }

    @SuppressLint({"ClickableViewAccessibility", "SetTextI18n"})
    @Override
    public void onBindViewHolder(@NotNull ViewHolder holder, int position) {
        RvLocationBinding binding = ((LocationViewHolder) holder).getViewBinding();

        Location location = locations.get(position);
        binding.title.setText(location.getName());
        loadImage(binding.icon, location.getResId());

        if (locationSelected.contains(location))
            show(binding.tick);

        //Rotating the compass icon in different location
        switch (position) {
            case 0:
                binding.icon.setRotation(-45f);
                break;
            case 2:
                binding.icon.setRotation(135f);
                break;
            case 3:
                binding.icon.setRotation(-135f);
                break;
            case 4:
                binding.icon.setRotation(45f);
                break;
        }

        //Storing the selected location in the shared preferences
        binding.parent.setOnClickListener(v -> {
            if (binding.tick.getVisibility() == View.VISIBLE) {
                locationSelected.remove(location);
                hide(binding.tick);

                PartyDetails details = prefRepository.getCurrentPartyDetails();
                details.getLocations().remove(location.getName());
                prefRepository.setCurrentPartyDetails(details);

            } else {
                locationSelected.add(location);
                show(binding.tick);

                PartyDetails details = prefRepository.getCurrentPartyDetails();
                details.getLocations().add(location.getName());
                prefRepository.setCurrentPartyDetails(details);
            }
        });
    }

    public int getItemCount() {
        return this.locations.size();
    }

    public long getItemId(int position) {
        return (long) (locations.get(position)).hashCode();
    }
}
