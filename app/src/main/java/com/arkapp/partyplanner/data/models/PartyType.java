package com.arkapp.partyplanner.data.models;

import org.jetbrains.annotations.NotNull;

/**
 * Created by Abdul Rehman on 02-06-2020.
 * Contact email - abdulrehman0796@gmail.com
 */


/**
 * This model class is used to store the party type UI
 */
public final class PartyType {
    @NotNull
    private final String title;
    private final int resId;

    public PartyType(@NotNull String title, int resId) {
        this.title = title;
        this.resId = resId;
    }

    @NotNull
    public final String getTitle() {
        return this.title;
    }

    public final int getResId() {
        return this.resId;
    }

    @NotNull
    public String toString() {
        return "PartyType(title=" + this.title + ", resId=" + this.resId + ")";
    }

}
