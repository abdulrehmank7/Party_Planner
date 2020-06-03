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
 * User login SQL table is created using the following class definition
 * This sql table is used for storing login details of user.
 */
@Entity(tableName = "USER_LOGIN")
public class UserLogin {
    @PrimaryKey(autoGenerate = true)
    @Nullable
    private final Integer uid;
    @ColumnInfo(name = "userName")
    @Nullable
    private final String userName;
    @ColumnInfo(name = "password")
    @Nullable
    private final String password;

    public UserLogin(@Nullable Integer uid, @Nullable String userName, @Nullable String password) {
        this.uid = uid;
        this.userName = userName;
        this.password = password;
    }

    @Nullable
    public final Integer getUid() {
        return this.uid;
    }

    @Nullable
    public final String getUserName() {
        return this.userName;
    }

    @Nullable
    public final String getPassword() {
        return this.password;
    }

    @NotNull
    public String toString() {
        return "UserLogin(uid=" + this.uid + ", userName=" + this.userName + ", password=" + this.password + ")";
    }
}