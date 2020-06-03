package com.arkapp.partyplanner.ui.finalChecklist.utils;

import com.arkapp.partyplanner.data.models.SummaryDetails;

import java.util.List;

/**
 * Created by Abdul Rehman on 03-06-2020.
 * Contact email - abdulrehman0796@gmail.com
 */
public interface GetSummaryListener {
    void onTaskEnded(List<SummaryDetails> data);
}
