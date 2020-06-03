package com.arkapp.partyplanner.data.models;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Created by Abdul Rehman on 02-06-2020.
 * Contact email - abdulrehman0796@gmail.com
 */

/**
 * Caterer SQL table is created using the following class definition
 * This table is used to store caterer data
 */
@Entity(tableName = "CATERER")
public final class Caterer {
    @PrimaryKey(autoGenerate = true)
    @Nullable
    private final Integer uid;
    @ColumnInfo(name = "name")
    @NotNull
    private final String name;
    @ColumnInfo(name = "website")
    @NotNull
    private final String website;
    @ColumnInfo(name = "contact")
    @NotNull
    private final String contact;
    @ColumnInfo(name = "price")
    private final double pricePerPax;
    @ColumnInfo(name = "partyType")
    @NotNull
    private final String partyType;

    public Caterer(@Nullable Integer uid, @NotNull String name, @NotNull String website, @NotNull String contact, double pricePerPax, @NotNull String partyType) {
        this.uid = uid;
        this.name = name;
        this.website = website;
        this.contact = contact;
        this.pricePerPax = pricePerPax;
        this.partyType = partyType;
    }

    @Nullable
    public final Integer getUid() {
        return this.uid;
    }

    @NotNull
    public final String getName() {
        return this.name;
    }

    @NotNull
    public final String getWebsite() {
        return this.website;
    }

    @NotNull
    public final String getContact() {
        return this.contact;
    }

    public final double getPricePerPax() {
        return this.pricePerPax;
    }

    @NotNull
    public final String getPartyType() {
        return this.partyType;
    }

    @NotNull
    public String toString() {
        return "Caterer(uid=" + this.uid + ", name=" + this.name + ", website=" + this.website + ", contact=" + this.contact + ", pricePerPax=" + this.pricePerPax + ", partyType=" + this.partyType + ")";
    }

}
