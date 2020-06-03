package com.arkapp.partyplanner.ui.signup;

import com.arkapp.partyplanner.data.models.UserLogin;

import java.util.List;

/**
 * Created by Abdul Rehman on 03-06-2020.
 * Contact email - abdulrehman0796@gmail.com
 */
public interface AddUserListener {
    void onTaskEnded();

    void onTaskEnded(List<UserLogin> data);
}
