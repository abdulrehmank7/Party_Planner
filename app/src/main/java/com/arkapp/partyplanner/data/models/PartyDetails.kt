package com.arkapp.partyplanner.data.models

import java.util.*

/**
 * Created by Abdul Rehman on 17-05-2020.
 * Contact email - abdulrehman0796@gmail.com
 */

data class PartyDetails(
    var partyDate: Date?,
    var partyBudget: String?,
    var partyDestination: String?,
    var partyGuest: Int?,
    var partyType: String?,
    var selectedFood: ArrayList<Food>?,
    var selectedDestination: Venue?
)