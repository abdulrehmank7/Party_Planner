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
 * Venue SQL table is created using the following class definition
 * This sql table is used for storing all venue data.
 */
@Entity(tableName = "VENUE")
public final class Venue {
    @PrimaryKey(autoGenerate = true)
    @Nullable
    private final Integer uid;
    @ColumnInfo(name = "name")
    @NotNull
    private final String name;
    @ColumnInfo(name = "address")
    @NotNull
    private final String address;
    @ColumnInfo(name = "capacity")
    private final int capacity;
    @ColumnInfo(name = "location")
    @NotNull
    private final String location;
    @ColumnInfo(name = "partyType")
    @NotNull
    private final String partyType;
    @ColumnInfo(name = "contact")
    @NotNull
    private final String contact;
    @ColumnInfo(name = "price")
    @NotNull
    private final String price;

    public Venue(@Nullable Integer uid, @NotNull String name, @NotNull String address, int capacity, @NotNull String location, @NotNull String partyType, @NotNull String contact, @NotNull String price) {
        this.uid = uid;
        this.name = name;
        this.address = address;
        this.capacity = capacity;
        this.location = location;
        this.partyType = partyType;
        this.contact = contact;
        this.price = price;
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
    public final String getAddress() {
        return this.address;
    }

    public final int getCapacity() {
        return this.capacity;
    }

    @NotNull
    public final String getLocation() {
        return this.location;
    }

    @NotNull
    public final String getPartyType() {
        return this.partyType;
    }

    @NotNull
    public final String getContact() {
        return this.contact;
    }

    @NotNull
    public final String getPrice() {
        return this.price;
    }

    @NotNull
    public String toString() {
        return "Venue(uid=" + this.uid + ", name=" + this.name + ", address=" + this.address + ", capacity=" + this.capacity + ", location=" + this.location + ", partyType=" + this.partyType + ", contact=" + this.contact + ", price=" + this.price + ")";
    }
}
