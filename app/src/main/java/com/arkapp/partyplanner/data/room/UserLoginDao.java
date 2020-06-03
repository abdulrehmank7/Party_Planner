package com.arkapp.partyplanner.data.room;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.arkapp.partyplanner.data.models.UserLogin;

import java.util.List;

/**
 * Created by Abdul Rehman on 17-05-2020.
 * Contact email - abdulrehman0796@gmail.com
 */

/**
 * This class is used to define query for the Login table
 */
@Dao
public interface UserLoginDao {
    @Insert(onConflict = 5)
    void insert(UserLogin var2);

    @Query("SELECT * FROM USER_LOGIN WHERE userName = :userName AND password =:password")
    List<UserLogin> getLoggedInUser(String userName, String password);

    @Query("SELECT * FROM USER_LOGIN WHERE userName = :userName")
    List<UserLogin> checkLoggedInUser(String userName);
}
