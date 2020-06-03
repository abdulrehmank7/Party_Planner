package com.arkapp.partyplanner.data.models;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Created by Abdul Rehman on 17-05-2020.
 * Contact email - abdulrehman0796@gmail.com
 */

/**
 * History summary SQL table is created using the following class definition
 * This sql table is used for storing history of user
 */

@Entity(tableName = "HISTORY_SUMMARY")
public final class HistorySummary {
    @PrimaryKey(autoGenerate = true)
    @Nullable
    private final Integer id;
    @ColumnInfo(name = "uid")
    @Nullable
    private final Integer uid;
    @ColumnInfo(name = "partyDate")
    @Nullable
    private String partyDate;
    @ColumnInfo(name = "partyBudget")
    @Nullable
    private String partyBudget;
    @ColumnInfo(name = "partyDestination")
    @Nullable
    private String partyDestination;
    @ColumnInfo(name = "partyGuest")
    @Nullable
    private Integer partyGuest;
    @ColumnInfo(name = "partyType")
    @Nullable
    private String partyType;
    @ColumnInfo(name = "selectedDestination")
    @Nullable
    private String selectedDestination;
    @ColumnInfo(name = "selectedCaterer")
    @Nullable
    private String selectedCaterers;
    @ColumnInfo(name = "extraNote")
    @Nullable
    private String extraNote;
    @ColumnInfo(name = "guestNameList")
    @Nullable
    private String guestNameList;
    @ColumnInfo(name = "checkedItemList")
    @Nullable
    private String checkedItemList;
    @ColumnInfo(name = "locations")
    @Nullable
    private String locations;

    public HistorySummary(@Nullable Integer id, @Nullable Integer uid, @Nullable String partyDate, @Nullable String partyBudget, @Nullable String partyDestination, @Nullable Integer partyGuest, @Nullable String partyType, @Nullable String selectedDestination, @Nullable String selectedCaterers, @Nullable String extraNote, @Nullable String guestNameList, @Nullable String checkedItemList, @Nullable String locations) {
        this.id = id;
        this.uid = uid;
        this.partyDate = partyDate;
        this.partyBudget = partyBudget;
        this.partyDestination = partyDestination;
        this.partyGuest = partyGuest;
        this.partyType = partyType;
        this.selectedDestination = selectedDestination;
        this.selectedCaterers = selectedCaterers;
        this.extraNote = extraNote;
        this.guestNameList = guestNameList;
        this.checkedItemList = checkedItemList;
        this.locations = locations;
    }

    @Nullable
    public final Integer getId() {
        return this.id;
    }

    @Nullable
    public final Integer getUid() {
        return this.uid;
    }

    @Nullable
    public final String getPartyDate() {
        return this.partyDate;
    }

    public final void setPartyDate(@Nullable String var1) {
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

    @Nullable
    public final String getPartyType() {
        return this.partyType;
    }

    public final void setPartyType(@Nullable String var1) {
        this.partyType = var1;
    }

    @Nullable
    public final String getSelectedDestination() {
        return this.selectedDestination;
    }

    public final void setSelectedDestination(@Nullable String var1) {
        this.selectedDestination = var1;
    }

    @Nullable
    public final String getSelectedCaterers() {
        return this.selectedCaterers;
    }

    public final void setSelectedCaterers(@Nullable String var1) {
        this.selectedCaterers = var1;
    }

    @Nullable
    public final String getExtraNote() {
        return this.extraNote;
    }

    public final void setExtraNote(@Nullable String var1) {
        this.extraNote = var1;
    }

    @Nullable
    public final String getGuestNameList() {
        return this.guestNameList;
    }

    public final void setGuestNameList(@Nullable String var1) {
        this.guestNameList = var1;
    }

    @Nullable
    public final String getCheckedItemList() {
        return this.checkedItemList;
    }

    public final void setCheckedItemList(@Nullable String var1) {
        this.checkedItemList = var1;
    }

    @Nullable
    public final String getLocations() {
        return this.locations;
    }

    public final void setLocations(@Nullable String var1) {
        this.locations = var1;
    }

    @NotNull
    public String toString() {
        return "HistorySummary(id=" + this.id + ", uid=" + this.uid + ", partyDate=" + this.partyDate + ", partyBudget=" + this.partyBudget + ", partyDestination=" + this.partyDestination + ", partyGuest=" + this.partyGuest + ", partyType=" + this.partyType + ", selectedDestination=" + this.selectedDestination + ", selectedCaterers=" + this.selectedCaterers + ", extraNote=" + this.extraNote + ", guestNameList=" + this.guestNameList + ", checkedItemList=" + this.checkedItemList + ", locations=" + this.locations + ")";
    }

}
