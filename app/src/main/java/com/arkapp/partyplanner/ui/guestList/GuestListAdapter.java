// GuestListAdapter.java
package com.arkapp.partyplanner.ui.guestList;

import android.annotation.SuppressLint;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.RecyclerView.ViewHolder;

import com.arkapp.partyplanner.R;
import com.arkapp.partyplanner.data.models.CheckedItem;
import com.arkapp.partyplanner.databinding.RvGuestListBinding;

import org.jetbrains.annotations.NotNull;

import java.util.List;

/**
 * Created by Abdul Rehman on 28-02-2020.
 * Contact email - abdulrehman0796@gmail.com
 */


/**
 * Recycler view adapter of guest checklist in the checklist screen
 */
public class GuestListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private final List<CheckedItem> guestList;

    public GuestListAdapter(@NotNull List<CheckedItem> guestList) {
        super();
        this.guestList = guestList;
    }

    @NotNull
    @Override
    public ViewHolder onCreateViewHolder(@NotNull ViewGroup parent, int viewType) {
        return (ViewHolder) (new GuestListViewHolder((RvGuestListBinding) DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.rv_guest_list, parent, false)));
    }

    @SuppressLint({"ClickableViewAccessibility", "SetTextI18n"})
    @Override
    public void onBindViewHolder(@NotNull ViewHolder holder, final int position) {
        final RvGuestListBinding binding = ((GuestListViewHolder) holder).getBinding();

        //Showing the guest name and selection status
        CheckedItem guest = guestList.get(position);

        binding.guestEt.setText(guest.getItemName());
        binding.guestCb.setChecked(guest.getSelected());

        binding.guestCb.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (TextUtils.isEmpty(binding.guestEt.getText())) {
                binding.guestName.setError("Required!");
                buttonView.setChecked(false);
            } else
                guestList.get(position).setSelected(isChecked);
        });

        binding.guestEt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                guestList.get(position).setItemName(s.toString());
            }
        });

    }

    public int getItemCount() {
        return this.guestList.size();
    }

    public long getItemId(int position) {
        return (long) (guestList.get(position)).hashCode();
    }
}
