package com.arkapp.partyplanner.data.models;

import org.jetbrains.annotations.NotNull;

/**
 * Created by Abdul Rehman on 02-06-2020.
 * Contact email - abdulrehman0796@gmail.com
 */

/**
 * This model class is used to store venue locations
 */
public class Location {
    @NotNull
    private final String name;
    private final int resId;

    public Location(@NotNull String name, int resId) {
        this.name = name;
        this.resId = resId;
    }

    @NotNull
    public final String getName() {
        return this.name;
    }

    public final int getResId() {
        return this.resId;
    }

    @NotNull
    public String toString() {
        return "Location(name=" + this.name + ", resId=" + this.resId + ")";
    }
}
