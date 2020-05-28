package com.arkapp.partyplanner.data.models

import java.util.*
import kotlin.collections.ArrayList

/**
 * Created by Abdul Rehman on 17-05-2020.
 * Contact email - abdulrehman0796@gmail.com
 */

data class PartyDetails(
    var id: Int?,
    var partyDate: Date?,
    var partyBudget: String?,
    var partyDestination: String?,
    var partyGuest: Int?,
    var partyType: ArrayList<String> = ArrayList(),
    var selectedCaterer: Caterer?,
    var selectedDestination: Venue?,
    var extraNote: String?,
    var guestNameList: ArrayList<CheckedItem>?,
    var checkedItemList: ArrayList<CheckedItem>?,
    var locations: ArrayList<String>?
)