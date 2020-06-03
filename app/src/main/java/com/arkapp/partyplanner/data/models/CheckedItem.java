package com.arkapp.partyplanner.data.models;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Created by Abdul Rehman on 02-06-2020.
 * Contact email - abdulrehman0796@gmail.com
 */

/**
 * This model class is used to store the checklist status(checked or not)
 */
public class CheckedItem {
    @Nullable
    private String itemName;
    private boolean selected;

    public CheckedItem(@Nullable String itemName, boolean selected) {
        this.itemName = itemName;
        this.selected = selected;
    }

    @Nullable
    public final String getItemName() {
        return this.itemName;
    }

    public final void setItemName(@Nullable String var1) {
        this.itemName = var1;
    }

    public final boolean getSelected() {
        return this.selected;
    }

    public final void setSelected(boolean var1) {
        this.selected = var1;
    }

    @NotNull
    public String toString() {
        return "CheckedItem(itemName=" + this.itemName + ", selected=" + this.selected + ")";
    }
}
