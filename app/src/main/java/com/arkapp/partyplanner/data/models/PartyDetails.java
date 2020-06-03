package com.arkapp.partyplanner.data.models;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Date;

import kotlin.jvm.internal.Intrinsics;

/**
 * Created by Abdul Rehman on 02-06-2020.
 * Contact email - abdulrehman0796@gmail.com
 */

/**
 * This model class is used to store the store the party details before storing them in SQL
 */
public final class PartyDetails {
    @Nullable
    private Integer id;
    @Nullable
    private Date partyDate;
    @Nullable
    private String partyBudget;
    @Nullable
    private String partyDestination;
    @Nullable
    private Integer partyGuest;
    @NotNull
    private ArrayList<String> partyType;
    @Nullable
    private Caterer selectedCaterer;
    @Nullable
    private Venue selectedDestination;
    @Nullable
    private String extraNote;
    @Nullable
    private ArrayList<CheckedItem> guestNameList;
    @Nullable
    private ArrayList<CheckedItem> checkedItemList;
    @Nullable
    private ArrayList<String> locations;

    public PartyDetails(@Nullable Integer id, @Nullable Date partyDate, @Nullable String partyBudget, @Nullable String partyDestination, @Nullable Integer partyGuest, @NotNull ArrayList partyType, @Nullable Caterer selectedCaterer, @Nullable Venue selectedDestination, @Nullable String extraNote, @Nullable ArrayList guestNameList, @Nullable ArrayList checkedItemList, @Nullable ArrayList locations) {
        Intrinsics.checkParameterIsNotNull(partyType, "partyType");
        this.id = id;
        this.partyDate = partyDate;
        this.partyBudget = partyBudget;
        this.partyDestination = partyDestination;
        this.partyGuest = partyGuest;
        this.partyType = partyType;
        this.selectedCaterer = selectedCaterer;
        this.selectedDestination = selectedDestination;
        this.extraNote = extraNote;
        this.guestNameList = guestNameList;
        this.checkedItemList = checkedItemList;
        this.locations = locations;
    }

    @Nullable
    public final Integer getId() {
        return this.id;
    }

    public final void setId(@Nullable Integer var1) {
        this.id = var1;
    }

    @Nullable
    public final Date getPartyDate() {
        return this.partyDate;
    }

    public final void setPartyDate(@Nullable Date var1) {
        this.partyDate = var1;
    }

    @Nullable
    public final String getPartyBudget() {
        return this.partyBudget;
    }

    public final void setPartyBudget(@Nullable String var1) {
        this.partyBudget = var1;
    }

    @Nullable
    public final String getPartyDestination() {
        return this.partyDestination;
    }

    public final void setPartyDestination(@Nullable String var1) {
        this.partyDestination = var1;
    }

    @Nullable
    public final Integer getPartyGuest() {
        return this.partyGuest;
    }

    public final void setPartyGuest(@Nullable Integer var1) {
        this.partyGuest = var1;
    }

    @NotNull
    public final ArrayList<String> getPartyType() {
        return this.partyType;
    }

    public final void setPartyType(@NotNull ArrayList<String> var1) {
        this.partyType = var1;
    }

    @Nullable
    public final Caterer getSelectedCaterer() {
        return this.selectedCaterer;
    }

    public final void setSelectedCaterer(@Nullable Caterer var1) {
        this.selectedCaterer = var1;
    }

    @Nullable
    public final Venue getSelectedDestination() {
        return selectedDestination;
    }

    public final void setSelectedDestination(@Nullable Venue var1) {
        this.selectedDestination = var1;
    }

    @Nullable
    public final String getExtraNote() {
        return this.extraNote;
    }

    public final void setExtraNote(@Nullable String var1) {
        this.extraNote = var1;
    }

    @Nullable
    public final ArrayList<CheckedItem> getGuestNameList() {
        return this.guestNameList;
    }

    public final void setGuestNameList(@Nullable ArrayList<CheckedItem> var1) {
        this.guestNameList = var1;
    }

    @Nullable
    public final ArrayList<CheckedItem> getCheckedItemList() {
        return this.checkedItemList;
    }

    public final void setCheckedItemList(@Nullable ArrayList<CheckedItem> var1) {
        this.checkedItemList = var1;
    }

    @Nullable
    public final ArrayList<String> getLocations() {
        return this.locations;
    }

    public final void setLocations(@Nullable ArrayList<String> var1) {
        this.locations = var1;
    }

    @NotNull
    public String toString() {
        return "PartyDetails(id=" + this.id + ", partyDate=" + this.partyDate + ", partyBudget=" + this.partyBudget + ", partyDestination=" + this.partyDestination + ", partyGuest=" + this.partyGuest + ", partyType=" + this.partyType + ", selectedCaterer=" + this.selectedCaterer + ", selectedDestination=" + this.selectedDestination + ", extraNote=" + this.extraNote + ", guestNameList=" + this.guestNameList + ", checkedItemList=" + this.checkedItemList + ", locations=" + this.locations + ")";
    }
}
