package com.arkapp.partyplanner.data.models

/**
 * Created by Abdul Rehman on 24-05-2020.
 * Contact email - abdulrehman0796@gmail.com
 */


/**
 * This model class is used to store the checklist status(checked or not)
 * */
data class CheckedItem(
    var itemName: String?,
    var selected: Boolean
)