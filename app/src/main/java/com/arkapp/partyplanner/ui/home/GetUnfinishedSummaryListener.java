package com.arkapp.partyplanner.ui.home;

import com.arkapp.partyplanner.data.models.PartyDetails;

/**
 * Created by Abdul Rehman on 03-06-2020.
 * Contact email - abdulrehman0796@gmail.com
 */
public interface GetUnfinishedSummaryListener {
    void onTaskEnded(PartyDetails data);
}
