package com.arkapp.partyplanner.utils

import android.content.Context
import androidx.lifecycle.LifecycleCoroutineScope
import com.arkapp.partyplanner.R
import com.arkapp.partyplanner.data.models.*
import com.arkapp.partyplanner.data.repository.PrefRepository
import com.arkapp.partyplanner.data.room.AppDatabase
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.*
import kotlin.collections.ArrayList

/**
 * Created by Abdul Rehman on 17-05-2020.
 * Contact email - abdulrehman0796@gmail.com
 */

const val LOW_BUDGED_LIMIT = 200.0
const val MEDIUM_BUDGED_LIMIT = 400.0
const val HIGH_BUDGED_LIMIT = 600.0
const val VERY_HIGH_BUDGED_LIMIT = 100000.0

const val OPTION_CREATE = 0
const val OPTION_CHECKLIST = 1
const val OPTION_UNFINISHED = 2
const val OPTION_PAST = 3

const val PARTY_TYPE_BABY_SHOWER = "Baby Shower"
const val PARTY_TYPE_SWEET_18 = "Sweet 18"
const val PARTY_TYPE_SWEET_21 = "Sweet 21"
const val PARTY_TYPE_REUNION = "Reunion"
const val PARTY_TYPE_TEA_PARTY = "Tea Party"
const val PARTY_TYPE_BREAK_FAST = "Breakfast"
const val PARTY_TYPE_BBQ_PARTY = "BBQ Party"
const val PARTY_TYPE_BACHELOR_PARTY = "Bachelor Party"
const val PARTY_TYPE_KIDS_PARTY = "Kids Party"
const val PARTY_TYPE_FORMAL_PARTY = "Formal Party"
const val PARTY_TYPE_CHRISTMAS_PARTY = "Christmas Party"
const val PARTY_TYPE_ALCOHOL = "Alcohol"
const val PARTY_TYPE_MAGIC_SHOW = "Magic Show"
const val PARTY_TYPE_DECORATION = "Party Decoration"

const val LOCATION_NORTH = "North"
const val LOCATION_SOUTH = "South"
const val LOCATION_EAST = "East"
const val LOCATION_WEST = "West"
const val LOCATION_CITY = "City"
const val LOCATION_NORTH_EAST = "North East"
const val LOCATION_CENTRAL = "Central"

const val CB_PARTY_TYPE = "CB_PARTY_TYPE"
const val CB_CATERER = "CB_CATERER"
const val CB_VENUE = "CB_VENUE"
const val CB_DECORATOR = "CB_DECORATOR"
const val CB_MAGIC_SHOW = "CB_MAGIC_SHOW"
const val CB_ALCOHOL = "CB_ALCOHOL"
const val CB_BUDGET = "CB_BUDGET"


var ENTERED_USER_NAME: String = ""
var CURRENT_SELECTED_OPTION: Int = OPTION_CREATE
var GUEST_LIST_NAMES = ArrayList<CheckedItem>()
var OPENED_GUEST_LIST = false

val gson = Gson()

fun getPartyTypes(): ArrayList<PartyType> {
    val list = ArrayList<PartyType>()

    list.add(PartyType(PARTY_TYPE_SWEET_18, R.drawable.ic_birthday))
    list.add(PartyType(PARTY_TYPE_BABY_SHOWER, R.drawable.ic_baby))
    list.add(PartyType(PARTY_TYPE_SWEET_21, R.drawable.ic_birthday))
    list.add(PartyType(PARTY_TYPE_REUNION, R.drawable.ic_team))
    list.add(PartyType(PARTY_TYPE_TEA_PARTY, R.drawable.ic_tea))
    list.add(PartyType(PARTY_TYPE_BREAK_FAST, R.drawable.ic_bread))
    list.add(PartyType(PARTY_TYPE_BBQ_PARTY, R.drawable.ic_meat))
    list.add(PartyType(PARTY_TYPE_ALCOHOL, R.drawable.ic_food))
    list.add(PartyType(PARTY_TYPE_BACHELOR_PARTY, R.drawable.ic_fun))
    list.add(PartyType(PARTY_TYPE_KIDS_PARTY, R.drawable.ic_toy))
    list.add(PartyType(PARTY_TYPE_FORMAL_PARTY, R.drawable.ic_dress_code))
    list.add(PartyType(PARTY_TYPE_CHRISTMAS_PARTY, R.drawable.ic_christmas_tree))

    return list
}

fun getLocation(): ArrayList<Location> {
    val list = ArrayList<Location>()

    list.add(Location(LOCATION_NORTH, R.drawable.ic_compass))
    list.add(Location(LOCATION_SOUTH, R.drawable.ic_compass))
    list.add(Location(LOCATION_CENTRAL, R.drawable.ic_compass))
    list.add(Location(LOCATION_EAST, R.drawable.ic_compass))
    list.add(Location(LOCATION_WEST, R.drawable.ic_compass))
    list.add(Location(LOCATION_NORTH_EAST, R.drawable.ic_compass))
    list.add(Location(LOCATION_CITY, R.drawable.ic_urban))

    return list
}

fun getPartyTypeFromStringArray(stringList: ArrayList<String>): ArrayList<PartyType> {
    val newPartyTypeList = ArrayList<PartyType>()
    val allPartyType = getPartyTypes()

    for (partyTypeName in stringList) {
        allPartyType.find { it.title == partyTypeName }.also {
            if (it != null) newPartyTypeList.add(it)
        }
    }

    return newPartyTypeList
}

fun getVenueList(): ArrayList<Venue> {
    val venueList = ArrayList<Venue>()

    venueList.add(
        Venue(
            null,
            "Main Dining Room",
            "2 Circular Road, Singapore 049358",
            50,
            LOCATION_CITY,
            gson.toJson(arrayListOf(PARTY_TYPE_SWEET_21,
                                    PARTY_TYPE_SWEET_18,
                                    PARTY_TYPE_BACHELOR_PARTY)),
            "6805 818 1",
            "7000"))

    venueList.add(
        Venue(
            null,
            "Sky Lounge",
            "5 Coleman StreetSingapore 179805",
            40,
            LOCATION_CITY,
            gson.toJson(arrayListOf(PARTY_TYPE_REUNION,
                                    PARTY_TYPE_CHRISTMAS_PARTY,
                                    PARTY_TYPE_SWEET_21,
                                    PARTY_TYPE_SWEET_18,
                                    PARTY_TYPE_BACHELOR_PARTY)),
            "6416 1033",
            "1500"))

    venueList.add(
        Venue(
            null,
            "Rustic Studio Perfect for 21st Party",
            "203D Lavender StreetSingapore 338763",
            35,
            LOCATION_CITY,
            gson.toJson(arrayListOf(PARTY_TYPE_CHRISTMAS_PARTY,
                                    PARTY_TYPE_BACHELOR_PARTY)),
            "6416 1033",
            "500"))

    venueList.add(
        Venue(
            null,
            "Beautiful Rooftop Perfect for 21st birthday",
            "203D Lavender Street Singapore 338763",
            35,
            LOCATION_CITY,
            gson.toJson(arrayListOf(PARTY_TYPE_CHRISTMAS_PARTY, PARTY_TYPE_SWEET_21,
                                    PARTY_TYPE_SWEET_18,
                                    PARTY_TYPE_BACHELOR_PARTY)),
            "90234901",
            "500"))

    venueList.add(
        Venue(
            null,
            "Shake Farm HQ Level 2 Lounge",
            "126 Telok Ayer StreetSingapore 0689595",
            40,
            LOCATION_CITY,
            gson.toJson(arrayListOf(PARTY_TYPE_FORMAL_PARTY, PARTY_TYPE_BBQ_PARTY,
                                    PARTY_TYPE_REUNION, PARTY_TYPE_CHRISTMAS_PARTY,
                                    PARTY_TYPE_BACHELOR_PARTY)),
            "85029150",
            "500"))

    venueList.add(
        Venue(
            null,
            "Private Dining Space",
            "Blk 8D Dempsey Road #01-01ASingapore 249672",
            50,
            LOCATION_CENTRAL,
            gson.toJson(arrayListOf(PARTY_TYPE_CHRISTMAS_PARTY,
                                    PARTY_TYPE_REUNION, PARTY_TYPE_SWEET_21,
                                    PARTY_TYPE_SWEET_18,
                                    PARTY_TYPE_BACHELOR_PARTY)),
            "90107418",
            "2000"))

    venueList.add(
        Venue(
            null,
            "Dining Lounge",
            "2 Emerald Hill Road SingaporeSingapore 229287",
            35,
            LOCATION_CENTRAL,
            gson.toJson(arrayListOf(PARTY_TYPE_FORMAL_PARTY,
                                    PARTY_TYPE_CHRISTMAS_PARTY,
                                    PARTY_TYPE_REUNION, PARTY_TYPE_SWEET_21,
                                    PARTY_TYPE_SWEET_18,
                                    PARTY_TYPE_BACHELOR_PARTY)),
            "67388818",
            "1000"))

    venueList.add(
        Venue(
            null,
            "Private Room",
            "2 Emerald Hill Road Singapore Singapore 229287",
            50,
            LOCATION_CENTRAL, gson.toJson(arrayListOf(PARTY_TYPE_BABY_SHOWER,
                                                      PARTY_TYPE_CHRISTMAS_PARTY,
                                                      PARTY_TYPE_REUNION, PARTY_TYPE_SWEET_21,
                                                      PARTY_TYPE_SWEET_18,
                                                      PARTY_TYPE_BACHELOR_PARTY)),
            "67388818",
            "1000"))

    venueList.add(
        Venue(
            null,
            "Urbana",
            "99 Irrawaddy RoadSingapore 329568",
            34,
            LOCATION_CENTRAL, gson.toJson(arrayListOf(PARTY_TYPE_BABY_SHOWER,
                                                      PARTY_TYPE_CHRISTMAS_PARTY,
                                                      PARTY_TYPE_REUNION, PARTY_TYPE_SWEET_21,
                                                      PARTY_TYPE_SWEET_18,
                                                      PARTY_TYPE_BACHELOR_PARTY)),
            "62500303",
            "4000"))

    venueList.add(
        Venue(
            null,
            "Indoors Dining Area",
            "33 Cuppage Road, Cuppage TerraceSingapore 229458",
            36,
            LOCATION_CENTRAL,
            gson.toJson(arrayListOf(PARTY_TYPE_TEA_PARTY,
                                    PARTY_TYPE_FORMAL_PARTY,
                                    PARTY_TYPE_BBQ_PARTY, PARTY_TYPE_CHRISTMAS_PARTY,
                                    PARTY_TYPE_REUNION, PARTY_TYPE_SWEET_21,
                                    PARTY_TYPE_SWEET_18,
                                    PARTY_TYPE_BACHELOR_PARTY)),
            "93378432",
            "1000"))

    venueList.add(
        Venue(
            null,
            "Fully Equipped Training Space",
            "201 Henderson Road #07-26Singapore 159545",
            50,
            LOCATION_SOUTH,
            gson.toJson(arrayListOf(PARTY_TYPE_FORMAL_PARTY,
                                    PARTY_TYPE_BBQ_PARTY,
                                    PARTY_TYPE_CHRISTMAS_PARTY,
                                    PARTY_TYPE_REUNION,
                                    PARTY_TYPE_SWEET_21,
                                    PARTY_TYPE_SWEET_18,
                                    PARTY_TYPE_BACHELOR_PARTY)),
            "91872149",
            "101"))

    venueList.add(
        Venue(
            null,
            "Swing Zone Area",
            "1 Harbourfront Walk | #01-57 VivoCitySingapore 098585",
            50,
            LOCATION_SOUTH,
            gson.toJson(arrayListOf(PARTY_TYPE_FORMAL_PARTY,
                                    PARTY_TYPE_BBQ_PARTY, PARTY_TYPE_CHRISTMAS_PARTY,
                                    PARTY_TYPE_REUNION, PARTY_TYPE_SWEET_21,
                                    PARTY_TYPE_SWEET_18,
                                    PARTY_TYPE_BACHELOR_PARTY)),
            "91834910",
            "1500"))

    venueList.add(
        Venue(
            null,
            "Restaurant Space",
            "5 Yong Siak StreetSingapore 168643",
            48,
            LOCATION_SOUTH,
            gson.toJson(arrayListOf(PARTY_TYPE_CHRISTMAS_PARTY,
                                    PARTY_TYPE_REUNION, PARTY_TYPE_SWEET_21,
                                    PARTY_TYPE_SWEET_18,
                                    PARTY_TYPE_BACHELOR_PARTY)),
            "98246294",
            "5000"))

    venueList.add(
        Venue(
            null,
            "Upper Place",
            "",
            40,
            LOCATION_SOUTH,
            gson.toJson(arrayListOf(PARTY_TYPE_FORMAL_PARTY,
                                    PARTY_TYPE_CHRISTMAS_PARTY,
                                    PARTY_TYPE_REUNION, PARTY_TYPE_SWEET_21,
                                    PARTY_TYPE_SWEET_18,
                                    PARTY_TYPE_BACHELOR_PARTY)),
            "65951380",
            "2000"))

    venueList.add(
        Venue(
            null,
            "Function Room",
            "1 Larkhill Road, Sentosa Island, Singapore 099394Singapore 099394",
            50,
            LOCATION_SOUTH,
            gson.toJson(arrayListOf(PARTY_TYPE_CHRISTMAS_PARTY,
                                    PARTY_TYPE_REUNION, PARTY_TYPE_SWEET_21,
                                    PARTY_TYPE_SWEET_18,
                                    PARTY_TYPE_BACHELOR_PARTY)),
            "68253827",
            "1000"))

    venueList.add(
        Venue(
            null,
            "BAR SPACE (L•T•A LONG TIME AGO @ BOAT QUAY)",
            "1 Fusionopolis Way #02-07 ConnexisSingapore 138632",
            50,
            LOCATION_WEST,
            gson.toJson(arrayListOf(PARTY_TYPE_CHRISTMAS_PARTY,
                                    PARTY_TYPE_REUNION, PARTY_TYPE_SWEET_21,
                                    PARTY_TYPE_SWEET_18,
                                    PARTY_TYPE_BACHELOR_PARTY)),
            "98761338",
            "500"))

    venueList.add(
        Venue(
            null,
            "Outdoor Event Space",
            "44 Rochester ParkSingapore 139248",
            40,
            LOCATION_WEST,
            gson.toJson(arrayListOf(PARTY_TYPE_CHRISTMAS_PARTY,
                                    PARTY_TYPE_REUNION, PARTY_TYPE_SWEET_21,
                                    PARTY_TYPE_SWEET_18,
                                    PARTY_TYPE_BACHELOR_PARTY)),
            "81577236",
            "1000"))

    venueList.add(
        Venue(
            null,
            "Rooftop Private Event Space",
            "30 Tuas Bay DriveSingapore 637548",
            50,
            LOCATION_WEST,
            gson.toJson(arrayListOf(PARTY_TYPE_CHRISTMAS_PARTY,
                                    PARTY_TYPE_REUNION, PARTY_TYPE_SWEET_21,
                                    PARTY_TYPE_SWEET_18,
                                    PARTY_TYPE_BACHELOR_PARTY)),
            "97120047",
            "1000"))

    venueList.add(
        Venue(
            null,
            "Rooftop Gallery Space",
            "3Ikea TampinesSingapore 528559",
            50,
            LOCATION_EAST,
            gson.toJson(arrayListOf(PARTY_TYPE_CHRISTMAS_PARTY,
                                    PARTY_TYPE_REUNION, PARTY_TYPE_SWEET_21,
                                    PARTY_TYPE_SWEET_18,
                                    PARTY_TYPE_BACHELOR_PARTY)),
            "88509813",
            "1000"))

    venueList.add(
        Venue(
            null,
            "Private Room",
            "Kinex (Previously OneKM), #02-21, 11 Tanjong Katong Road Singapore 436950",
            35,
            LOCATION_EAST,
            gson.toJson(arrayListOf(PARTY_TYPE_CHRISTMAS_PARTY,
                                    PARTY_TYPE_REUNION, PARTY_TYPE_SWEET_21,
                                    PARTY_TYPE_SWEET_18,
                                    PARTY_TYPE_BACHELOR_PARTY)),
            "83281198",
            "500"))
    venueList.add(
        Venue(
            null,
            "The Seagrill",
            "Changi Beach Park, 260 Nicoll DriveSingapore 498991",
            50,
            LOCATION_EAST,
            gson.toJson(arrayListOf(PARTY_TYPE_CHRISTMAS_PARTY,
                                    PARTY_TYPE_REUNION, PARTY_TYPE_SWEET_21,
                                    PARTY_TYPE_SWEET_18,
                                    PARTY_TYPE_BACHELOR_PARTY)),
            "92279928",
            "2000"))
    venueList.add(
        Venue(
            null,
            "Alfresco Dining Area",
            "5 Changi Business Park Central #01-68/69 Changi City Point Singapore (486038)Singapore 486038",
            33,
            LOCATION_EAST,
            gson.toJson(arrayListOf(PARTY_TYPE_CHRISTMAS_PARTY,
                                    PARTY_TYPE_REUNION, PARTY_TYPE_SWEET_21,
                                    PARTY_TYPE_SWEET_18,
                                    PARTY_TYPE_BACHELOR_PARTY)),
            "90014554",
            "1500"))
    venueList.add(
        Venue(
            null,
            "Event Room",
            "1 Tampines Walk, #03-03 Our Tampines HubSingapore 520940",
            50,
            LOCATION_EAST,
            gson.toJson(arrayListOf(PARTY_TYPE_CHRISTMAS_PARTY,
                                    PARTY_TYPE_REUNION, PARTY_TYPE_SWEET_21,
                                    PARTY_TYPE_SWEET_18,
                                    PARTY_TYPE_BACHELOR_PARTY)),
            "67059416",
            "250"))

    venueList.add(
        Venue(
            null,
            "Meeting Room",
            "217 Syed Alwi RoadSingapore 207776",
            40,
            LOCATION_NORTH_EAST,
            gson.toJson(arrayListOf(PARTY_TYPE_CHRISTMAS_PARTY,
                                    PARTY_TYPE_REUNION, PARTY_TYPE_SWEET_21,
                                    PARTY_TYPE_SWEET_18,
                                    PARTY_TYPE_BACHELOR_PARTY)),
            "91464302",
            "500"))

    venueList.add(
        Venue(
            null,
            "Alfresco Space",
            "3 Punggol Point Road The Punggol Settlement #01-05Singapore 828694",
            40,
            LOCATION_NORTH_EAST,
            gson.toJson(arrayListOf(PARTY_TYPE_CHRISTMAS_PARTY,
                                    PARTY_TYPE_REUNION, PARTY_TYPE_SWEET_21,
                                    PARTY_TYPE_SWEET_18,
                                    PARTY_TYPE_BACHELOR_PARTY)),
            "96458881",
            "1000"))

    venueList.add(
        Venue(
            null,
            "Kingfisher Room",
            "3 Punggol Point Road The Punggol Settlement #01-05 Singapore 828694",
            40,
            LOCATION_NORTH,
            gson.toJson(arrayListOf(PARTY_TYPE_CHRISTMAS_PARTY,
                                    PARTY_TYPE_REUNION, PARTY_TYPE_SWEET_21,
                                    PARTY_TYPE_SWEET_18,
                                    PARTY_TYPE_BACHELOR_PARTY)),
            "64860872",
            "500"))
    return venueList
}

fun getCatererList(): ArrayList<Caterer> {

    val catererList = ArrayList<Caterer>()

    catererList.add(Caterer(null,
                            "BBQ House Singapore Pte. Ltd foodline",
                            "https://www.foodline.sg/catering/BBQ-House-Singapore-Pte.-Ltd/",
                            "6100 0029",
                            7.0,
                            gson.toJson(arrayListOf(PARTY_TYPE_BBQ_PARTY,
                                                    PARTY_TYPE_BACHELOR_PARTY))
    ))

    catererList.add(Caterer(null,
                            "Mmmm! foodline",
                            "https://www.foodline.sg/catering/Mmmm!/",
                            "6100 0029",
                            6.0,
                            gson.toJson(arrayListOf(PARTY_TYPE_BBQ_PARTY,
                                                    PARTY_TYPE_BACHELOR_PARTY))
    ))

    catererList.add(Caterer(null,
                            "Catering Culture",
                            "https://www.foodline.sg/catering/Catering-Culture/",
                            "6100 0029",
                            9.0,
                            gson.toJson(arrayListOf(PARTY_TYPE_BBQ_PARTY,
                                                    PARTY_TYPE_CHRISTMAS_PARTY))
    ))

    catererList.add(Caterer(null,
                            "Angel's Restaurant foodline",
                            "https://www.foodline.sg/catering/Angels-Restaurant/",
                            "6100 0029",
                            15.0,
                            gson.toJson(arrayListOf(PARTY_TYPE_BBQ_PARTY,
                                                    PARTY_TYPE_REUNION,
                                                    PARTY_TYPE_BACHELOR_PARTY))
    ))
    catererList.add(Caterer(null,
                            "FattyDaddyFattyMummy foodline",
                            ": https://www.foodline.sg/catering/FattyDaddyFattyMummy/",
                            "6100 0029",
                            19.0,
                            gson.toJson(arrayListOf(PARTY_TYPE_BBQ_PARTY,
                                                    PARTY_TYPE_CHRISTMAS_PARTY,
                                                    PARTY_TYPE_BACHELOR_PARTY))
    ))
    catererList.add(Caterer(null,
                            "Occasions Catering foodline",
                            "https://www.foodline.sg/catering/Occasions-Catering/",
                            "6100 0029",
                            28.0,
                            gson.toJson(arrayListOf(PARTY_TYPE_BBQ_PARTY,
                                                    PARTY_TYPE_REUNION,
                                                    PARTY_TYPE_BACHELOR_PARTY))
    ))
    catererList.add(Caterer(null,
                            "Opah Satay foodline",
                            "https://www.foodline.sg/catering/Opah-Satay/",
                            "6100 0029",
                            35.0,
                            gson.toJson(arrayListOf(PARTY_TYPE_BBQ_PARTY,
                                                    PARTY_TYPE_REUNION,
                                                    PARTY_TYPE_BACHELOR_PARTY))
    ))
    catererList.add(Caterer(null,
                            "The Connoisseur Concerto- TCC foodline",
                            "https://www.foodline.sg/catering/The-Connoisseur-Concerto--TCC/",
                            "6100 0029",
                            4.0,
                            gson.toJson(arrayListOf(PARTY_TYPE_TEA_PARTY, PARTY_TYPE_FORMAL_PARTY))
    ))
    catererList.add(Caterer(null,
                            "Fung Kee",
                            "https://www.foodline.sg/catering/Fung-Kee/",
                            "6100 0029",
                            6.0,
                            gson.toJson(arrayListOf(PARTY_TYPE_TEA_PARTY,
                                                    PARTY_TYPE_CHRISTMAS_PARTY,
                                                    PARTY_TYPE_FORMAL_PARTY, PARTY_TYPE_BREAK_FAST))
    ))
    catererList.add(Caterer(null,
                            "QQ Catering",
                            "https://www.foodline.sg/catering/QQ-Catering/",
                            "6100 0029",
                            8.0,
                            gson.toJson(arrayListOf(PARTY_TYPE_TEA_PARTY,
                                                    PARTY_TYPE_CHRISTMAS_PARTY,
                                                    PARTY_TYPE_FORMAL_PARTY, PARTY_TYPE_BREAK_FAST))
    ))
    catererList.add(Caterer(null,
                            "Papitto Gelato",
                            "https://www.foodline.sg/catering/Papitto-Gelato/",
                            "6100 0029",
                            13.0,
                            gson.toJson(arrayListOf(PARTY_TYPE_TEA_PARTY,
                                                    PARTY_TYPE_FORMAL_PARTY,
                                                    PARTY_TYPE_BREAK_FAST))
    ))
    catererList.add(Caterer(null,
                            "Qi Ji",
                            "https://www.foodline.sg/catering/Qi-Ji/",
                            "6100 0029",
                            6.0,
                            gson.toJson(arrayListOf(PARTY_TYPE_BACHELOR_PARTY,
                                                    PARTY_TYPE_CHRISTMAS_PARTY,
                                                    PARTY_TYPE_BREAK_FAST))
    ))
    catererList.add(Caterer(null,
                            "A-One Signature foodline",
                            "https://www.foodline.sg/catering/A-One-Signature/",
                            "6100 0029",
                            12.0,
                            gson.toJson(arrayListOf(PARTY_TYPE_BACHELOR_PARTY,
                                                    PARTY_TYPE_BBQ_PARTY, PARTY_TYPE_BREAK_FAST))
    ))
    catererList.add(Caterer(null,
                            "Curry & Tandoor Pte Ltd",
                            "https://www.foodline.sg/catering/Curry-N-Tandoor-Pte-Ltd/",
                            "6100 0029",
                            15.0,
                            gson.toJson(arrayListOf(PARTY_TYPE_BACHELOR_PARTY,
                                                    PARTY_TYPE_CHRISTMAS_PARTY,
                                                    PARTY_TYPE_BREAK_FAST))
    ))
    catererList.add(Caterer(null,
                            "79 After Dark Catering",
                            "https://www.foodline.sg/catering/79-After-Dark-Catering/",
                            "6100 0029",
                            20.0,
                            gson.toJson(arrayListOf(PARTY_TYPE_BACHELOR_PARTY, PARTY_TYPE_REUNION))
    ))
    catererList.add(Caterer(null,
                            "Nosh Kitchen foodline",
                            "https://www.foodline.sg/catering/Nosh-Kitchen/",
                            "6100 0029",
                            25.0,
                            gson.toJson(arrayListOf(PARTY_TYPE_BACHELOR_PARTY, PARTY_TYPE_REUNION))
    ))
    catererList.add(Caterer(null,
                            "Fu Kwee Caterer Pte Ltd foodline",
                            ": https://www.foodline.sg/catering/Fu-Kwee-Caterer-Pte-Ltd/",
                            "6100 0029",
                            30.0,
                            gson.toJson(arrayListOf(PARTY_TYPE_BACHELOR_PARTY, PARTY_TYPE_REUNION))
    ))
    catererList.add(Caterer(null,
                            "Wee Nam Kee Chicken Rice foodline",
                            "https://www.foodline.sg/catering/Wee-Nam-Kee-Chicken-Rice/",
                            "6100 0029",
                            32.0,
                            gson.toJson(arrayListOf(PARTY_TYPE_BACHELOR_PARTY, PARTY_TYPE_REUNION))
    ))
    catererList.add(Caterer(null,
                            "Grain",
                            "https://www.foodline.sg/catering/Grain/",
                            "6100 0029",
                            35.0,
                            gson.toJson(arrayListOf(PARTY_TYPE_BACHELOR_PARTY, PARTY_TYPE_REUNION))
    ))
    catererList.add(Caterer(null,
                            "Time For Thai foodline",
                            "https://www.foodline.sg/catering/Time-For-Thai/",
                            "6100 0029",
                            38.0,
                            gson.toJson(arrayListOf(PARTY_TYPE_BACHELOR_PARTY, PARTY_TYPE_REUNION))
    ))
    catererList.add(Caterer(null,
                            "Mum’s Kitchen",
                            "https://www.foodline.sg/catering/Mums-Kitchen/",
                            "6100 0029",
                            20.0,
                            gson.toJson(arrayListOf(PARTY_TYPE_BACHELOR_PARTY,
                                                    PARTY_TYPE_CHRISTMAS_PARTY, PARTY_TYPE_REUNION))
    ))
    catererList.add(Caterer(null,
                            "Big Boys Sizzling Hot Plate Western foodline",
                            "https://www.foodline.sg/catering/Big-Boys-Sizzling-Hot-Plate-Western/",
                            "6100 0029",
                            45.0,
                            gson.toJson(arrayListOf(PARTY_TYPE_BACHELOR_PARTY, PARTY_TYPE_REUNION))
    ))
    catererList.add(Caterer(null,
                            "Be Frank foodline",
                            "https://www.foodline.sg/catering/Be-Frank/",
                            "6100 0029",
                            6.0,
                            gson.toJson(arrayListOf(PARTY_TYPE_BABY_SHOWER,
                                                    PARTY_TYPE_KIDS_PARTY,
                                                    PARTY_TYPE_SWEET_21,
                                                    PARTY_TYPE_SWEET_18))
    ))
    catererList.add(Caterer(null,
                            "East West Fusion foodline",
                            "https://www.foodline.sg/catering/East-West-Fusion/",
                            "6100 0029",
                            10.0,
                            gson.toJson(arrayListOf(PARTY_TYPE_BABY_SHOWER,
                                                    PARTY_TYPE_SWEET_18,
                                                    PARTY_TYPE_FORMAL_PARTY,
                                                    PARTY_TYPE_KIDS_PARTY,
                                                    PARTY_TYPE_SWEET_21))
    ))
    catererList.add(Caterer(null,
                            "Delizio Catering (Thai Specialties) foodline",
                            "https://www.foodline.sg/catering/Delizio-Catering-(Thai-Specialties)/",
                            "6100 0029",
                            13.0,
                            gson.toJson(arrayListOf(PARTY_TYPE_BABY_SHOWER,
                                                    PARTY_TYPE_SWEET_18,
                                                    PARTY_TYPE_FORMAL_PARTY,
                                                    PARTY_TYPE_KIDS_PARTY,
                                                    PARTY_TYPE_SWEET_21))
    ))
    catererList.add(Caterer(null,
                            "WORD. Events and Catering foodline",
                            "https://www.foodline.sg/catering/WORD.-Events-and-Catering/",
                            "6100 0029",
                            15.0,
                            gson.toJson(arrayListOf(PARTY_TYPE_BABY_SHOWER,
                                                    PARTY_TYPE_KIDS_PARTY,
                                                    PARTY_TYPE_SWEET_21,
                                                    PARTY_TYPE_SWEET_18))
    ))
    catererList.add(Caterer(null,
                            "Sembawang Eating House Seafood Restaurant foodline",
                            "https://www.foodline.sg/catering/Sembawang-Eating-House-Seafood-Restaurant/",
                            "6100 0029",
                            20.0,
                            gson.toJson(arrayListOf(PARTY_TYPE_BABY_SHOWER,
                                                    PARTY_TYPE_SWEET_18,
                                                    PARTY_TYPE_FORMAL_PARTY,
                                                    PARTY_TYPE_KIDS_PARTY,
                                                    PARTY_TYPE_SWEET_21))
    ))
    catererList.add(Caterer(null,
                            "Xiang's Catering foodline",
                            "https://www.foodline.sg/catering/Xiangs-Catering/",
                            "6100 0029",
                            25.0,
                            gson.toJson(arrayListOf(PARTY_TYPE_BABY_SHOWER,
                                                    PARTY_TYPE_KIDS_PARTY,
                                                    PARTY_TYPE_SWEET_21,
                                                    PARTY_TYPE_SWEET_18))
    ))
    catererList.add(Caterer(null,
                            "Good Chance Catering foodline",
                            "https://www.foodline.sg/catering/Good-Chance-Catering/",
                            "6100 0029",
                            30.0,
                            gson.toJson(arrayListOf(PARTY_TYPE_BABY_SHOWER,
                                                    PARTY_TYPE_KIDS_PARTY,
                                                    PARTY_TYPE_SWEET_21,
                                                    PARTY_TYPE_SWEET_18))
    ))
    catererList.add(Caterer(null,
                            "The Catering Concerto by TCC foodline",
                            "https://www.foodline.sg/catering/The-Catering-Concerto-by-TCC/",
                            "6100 0029",
                            8.0,
                            gson.toJson(arrayListOf(PARTY_TYPE_BREAK_FAST, PARTY_TYPE_TEA_PARTY))
    ))
    catererList.add(Caterer(null,
                            "BellyGood Caterer foodline",
                            "https://www.foodline.sg/catering/BellyGood-Caterer/",
                            "6100 0029",
                            10.0,
                            gson.toJson(arrayListOf(PARTY_TYPE_BREAK_FAST, PARTY_TYPE_TEA_PARTY))
    ))
    catererList.add(Caterer(null,
                            "Serve Best Pte Ltd foodline",
                            "https://www.foodline.sg/catering/Serve-Best-Pte-Ltd/",
                            "6100 0029",
                            15.0,
                            gson.toJson(arrayListOf(PARTY_TYPE_BREAK_FAST, PARTY_TYPE_TEA_PARTY))
    ))
    catererList.add(Caterer(null,
                            "Katong Catering foodline",
                            "https://www.foodline.sg/catering/Katong-Catering/",
                            "6100 0029",
                            17.0,
                            gson.toJson(arrayListOf(PARTY_TYPE_BREAK_FAST, PARTY_TYPE_TEA_PARTY))
    ))
    catererList.add(Caterer(null,
                            "WE Cater foodline",
                            "https://www.foodline.sg/catering/WE-Cater/",
                            "6100 0029",
                            20.0,
                            gson.toJson(arrayListOf(PARTY_TYPE_BREAK_FAST, PARTY_TYPE_TEA_PARTY))
    ))
    catererList.add(Caterer(null,
                            " East West Noodles (S11) @ Matrix Cafeteria ITE College West foodline",
                            "https://www.foodline.sg/catering/East-West-Noodles-(S11)-@-Matrix-Cafeteria-ITE-College-West/",
                            "6100 0029",
                            22.0,
                            gson.toJson(arrayListOf(PARTY_TYPE_BREAK_FAST, PARTY_TYPE_TEA_PARTY))
    ))
    catererList.add(Caterer(null,
                            "Rasa Rasa Catering Services Pte Ltd foodline",
                            "https://www.foodline.sg/catering/Rasa-Rasa-Catering-Services-Pte-Ltd/",
                            "6100 0029",
                            20.0,
                            gson.toJson(arrayListOf(PARTY_TYPE_SWEET_18,
                                                    PARTY_TYPE_KIDS_PARTY,
                                                    PARTY_TYPE_SWEET_21,
                                                    PARTY_TYPE_CHRISTMAS_PARTY))
    ))
    catererList.add(Caterer(null,
                            "Fusion Spoon Catering Services foodline",
                            "https://www.foodline.sg/catering/Fusion-Spoon-Catering-Services/",
                            "6100 0029",
                            25.0,
                            gson.toJson(arrayListOf(PARTY_TYPE_SWEET_18,
                                                    PARTY_TYPE_KIDS_PARTY,
                                                    PARTY_TYPE_SWEET_21,
                                                    PARTY_TYPE_CHRISTMAS_PARTY))
    ))
    catererList.add(Caterer(null,
                            "WEEAT foodline",
                            "https://www.foodline.sg/catering/WEEAT/",
                            "6100 0029",
                            30.0,
                            gson.toJson(arrayListOf(PARTY_TYPE_SWEET_18,
                                                    PARTY_TYPE_KIDS_PARTY,
                                                    PARTY_TYPE_SWEET_21,
                                                    PARTY_TYPE_CHRISTMAS_PARTY))
    ))
    catererList.add(Caterer(null,
                            "Mixed Greens foodline",
                            " https://www.foodline.sg/catering/Mixed-Greens/",
                            "6100 0029",
                            10.0,
                            gson.toJson(arrayListOf(PARTY_TYPE_FORMAL_PARTY,
                                                    PARTY_TYPE_REUNION,
                                                    PARTY_TYPE_CHRISTMAS_PARTY))
    ))
    catererList.add(Caterer(null,
                            "THAI-LICIOUS 泰好吃 foodline",
                            "https://www.foodline.sg/catering/THAI-LICIOUS--%E6%B3%B0%E5%A5%BD%E5%90%83/",
                            "6100 0029",
                            30.0,
                            gson.toJson(arrayListOf(PARTY_TYPE_FORMAL_PARTY,
                                                    PARTY_TYPE_REUNION,
                                                    PARTY_TYPE_CHRISTMAS_PARTY))
    ))
    catererList.add(Caterer(null,
                            "Stamford Catering foodline",
                            "https://www.foodline.sg/catering/Stamford-Catering/",
                            "6100 0029",
                            25.0,
                            gson.toJson(arrayListOf(PARTY_TYPE_CHRISTMAS_PARTY,
                                                    PARTY_TYPE_REUNION,
                                                    PARTY_TYPE_BREAK_FAST))
    ))


    return catererList
}

fun convertUnfinished(partyDetails: PartyDetails, uid: Int): UnfinishedDetails {
    return UnfinishedDetails(
        partyDetails.id,
        uid,
        gson.toJson(partyDetails.partyDate),
        partyDetails.partyBudget,
        partyDetails.partyDestination,
        partyDetails.partyGuest,
        gson.toJson(partyDetails.partyType),
        gson.toJson(partyDetails.selectedDestination),
        gson.toJson(partyDetails.selectedCaterer),
        partyDetails.extraNote,
        gson.toJson(partyDetails.guestNameList),
        gson.toJson(partyDetails.checkedItemList),
        gson.toJson(partyDetails.locations)
    )
}

fun convertSummary(partyDetails: PartyDetails, uid: Int): SummaryDetails {
    return SummaryDetails(
        partyDetails.id,
        uid,
        gson.toJson(partyDetails.partyDate),
        partyDetails.partyBudget,
        partyDetails.partyDestination,
        partyDetails.partyGuest,
        gson.toJson(partyDetails.partyType),
        gson.toJson(partyDetails.selectedDestination),
        gson.toJson(partyDetails.selectedCaterer),
        partyDetails.extraNote,
        gson.toJson(partyDetails.guestNameList),
        gson.toJson(partyDetails.checkedItemList),
        gson.toJson(partyDetails.locations)
    )
}

fun convertHistorySummary(partyDetails: PartyDetails, uid: Int): HistorySummary {
    return HistorySummary(
        partyDetails.id,
        uid,
        gson.toJson(partyDetails.partyDate),
        partyDetails.partyBudget,
        partyDetails.partyDestination,
        partyDetails.partyGuest,
        gson.toJson(partyDetails.partyType),
        gson.toJson(partyDetails.selectedDestination),
        gson.toJson(partyDetails.selectedCaterer),
        partyDetails.extraNote,
        gson.toJson(partyDetails.guestNameList),
        gson.toJson(partyDetails.checkedItemList),
        gson.toJson(partyDetails.locations)
    )
}

fun convertPartyFromUnfinished(unfinishedDetails: UnfinishedDetails): PartyDetails {

    val type1 = object : TypeToken<ArrayList<String>>() {}.type
    val type2 = object : TypeToken<ArrayList<CheckedItem>>() {}.type

    val selectedPartyType = gson.fromJson<ArrayList<String>>(unfinishedDetails.partyType, type1)
    val guestList = gson.fromJson<ArrayList<CheckedItem>>(unfinishedDetails.guestNameList, type2)
    val checkedItemList = gson.fromJson<ArrayList<CheckedItem>>(unfinishedDetails.checkedItemList,
                                                                type2)
    val selectedLocation = gson.fromJson<ArrayList<String>>(unfinishedDetails.locations, type1)

    return PartyDetails(
        unfinishedDetails.id,
        gson.fromJson(unfinishedDetails.partyDate, Date::class.java),
        unfinishedDetails.partyBudget,
        unfinishedDetails.partyDestination,
        unfinishedDetails.partyGuest,
        selectedPartyType,
        gson.fromJson(unfinishedDetails.selectedCaterers, Caterer::class.java),
        gson.fromJson(unfinishedDetails.selectedDestination, Venue::class.java),
        unfinishedDetails.extraNote,
        guestList,
        checkedItemList,
        selectedLocation
    )
}

fun convertPartyFromSummary(summary: SummaryDetails): PartyDetails {

    val type1 = object : TypeToken<ArrayList<String>>() {}.type
    val type2 = object : TypeToken<ArrayList<CheckedItem>>() {}.type

    val selectedPartyType = gson.fromJson<ArrayList<String>>(summary.partyType, type1)
    val guestList = gson.fromJson<ArrayList<CheckedItem>>(summary.guestNameList, type2)
    val checkedItemList = gson.fromJson<ArrayList<CheckedItem>>(summary.checkedItemList, type2)
    val selectedLocation = gson.fromJson<ArrayList<String>>(summary.locations, type1)

    return PartyDetails(
        summary.id,
        gson.fromJson(summary.partyDate, Date::class.java),
        summary.partyBudget,
        summary.partyDestination,
        summary.partyGuest,
        selectedPartyType,
        gson.fromJson(summary.selectedCaterers, Caterer::class.java),
        gson.fromJson(summary.selectedDestination, Venue::class.java),
        summary.extraNote,
        guestList,
        checkedItemList,
        selectedLocation
    )
}

fun convertPartyFromHistorySummary(summary: HistorySummary): PartyDetails {

    val type1 = object : TypeToken<ArrayList<String>>() {}.type
    val type2 = object : TypeToken<ArrayList<CheckedItem>>() {}.type

    val selectedPartyType = gson.fromJson<ArrayList<String>>(summary.partyType, type1)
    val guestList = gson.fromJson<ArrayList<CheckedItem>>(summary.guestNameList, type2)
    val checkedItemList = gson.fromJson<ArrayList<CheckedItem>>(summary.checkedItemList, type2)
    val selectedLocation = gson.fromJson<ArrayList<String>>(summary.locations, type1)

    return PartyDetails(
        summary.id,
        gson.fromJson(summary.partyDate, Date::class.java),
        summary.partyBudget,
        summary.partyDestination,
        summary.partyGuest,
        selectedPartyType,
        gson.fromJson(summary.selectedCaterers, Caterer::class.java),
        gson.fromJson(summary.selectedDestination, Venue::class.java),
        summary.extraNote,
        guestList,
        checkedItemList,
        selectedLocation
    )
}

fun addUnfinishedData(lifecycleScope: LifecycleCoroutineScope,
                      context: Context,
                      prefRepository: PrefRepository) {
    lifecycleScope.launch(Dispatchers.Main) {
        val unfinishedDao = AppDatabase.getDatabase(context).unfinishedDao()
        unfinishedDao.insert(convertUnfinished(prefRepository.getCurrentPartyDetails(),
                                               prefRepository.getCurrentUser()?.uid!!))
    }
}

fun addEmptyGuest(totalGuest: Int) {
    if (GUEST_LIST_NAMES.size < totalGuest)
        for (guest in 0 until totalGuest - GUEST_LIST_NAMES.size) {
            GUEST_LIST_NAMES.add(CheckedItem("", false))
        }
}

fun getRandom() = (0..10000).random()