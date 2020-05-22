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

const val FOOD_TYPE_ALCOHOL = "ALCOHOL"
const val FOOD_TYPE_CAKE = "CAKE"
const val FOOD_TYPE_MAIN_COURSE = "MAIN_COURSE"

const val LOW_BUDGED_LIMIT = 50.0
const val MEDIUM_BUDGED_LIMIT = 100.0
const val HIGH_BUDGED_LIMIT = 200.0
const val VERY_HIGH_BUDGED_LIMIT = 100000.0


const val OPTION_CREATE = 0
const val OPTION_CHECKLIST = 1
const val OPTION_UNFINISHED = 2
const val OPTION_PAST = 3

const val PARTY_TYPE_BABY_SHOWER = "BABY_SHOWER"
const val PARTY_TYPE_OTHER = "OTHER"

var ENTERED_USER_NAME: String = ""
var CURRENT_SELECTED_OPTION: Int = OPTION_CREATE

fun getFoodList(): ArrayList<Food> {
    val foodList = ArrayList<Food>()

    foodList.add(Food(null, "Cup Cake", 10.0, R.drawable.img_cake4, FOOD_TYPE_CAKE))
    foodList.add(Food(null, "Marble Slab Creamery", 20.0, R.drawable.img_cake2, FOOD_TYPE_CAKE))
    foodList.add(Food(null, "Chocolates", 5.0, R.drawable.img_cake5, FOOD_TYPE_CAKE))
    foodList.add(Food(null, "Jara Petit", 10.0, R.drawable.img_cake1, FOOD_TYPE_CAKE))
    foodList.add(Food(null, "The White Ombré", 5.0, R.drawable.img_cake3, FOOD_TYPE_CAKE))
    foodList.add(Food(null,
                      "Butler’s Triple Chocalate",
                      20.0,
                      R.drawable.img_cake6,
                      FOOD_TYPE_CAKE))

    foodList.add(Food(null, "Laksa", 3.0, R.drawable.img_main1, FOOD_TYPE_MAIN_COURSE))
    foodList.add(Food(null, "Bak Kut The", 7.0, R.drawable.img_main2, FOOD_TYPE_MAIN_COURSE))
    foodList.add(Food(null, "Hokkien Mee", 4.0, R.drawable.img_main3, FOOD_TYPE_MAIN_COURSE))
    foodList.add(Food(null, "Chicken Rice", 12.0, R.drawable.img_main4, FOOD_TYPE_MAIN_COURSE))
    foodList.add(Food(null, "Char Kway Teow", 4.0, R.drawable.img_main5, FOOD_TYPE_MAIN_COURSE))
    foodList.add(Food(null,
                      "Carrot Cake (Chai Tow Kway)",
                      3.0,
                      R.drawable.img_main6,
                      FOOD_TYPE_MAIN_COURSE))
    foodList.add(Food(null, "Wanton Mee", 3.0, R.drawable.img_main7, FOOD_TYPE_MAIN_COURSE))
    foodList.add(Food(null, "Fish Bee Hoon", 5.0, R.drawable.img_main8, FOOD_TYPE_MAIN_COURSE))
    foodList.add(Food(null, "Bak Chor Mee", 10.0, R.drawable.img_main9, FOOD_TYPE_MAIN_COURSE))
    foodList.add(Food(null,
                      "Oyster omelette (Orh Luak)",
                      8.0,
                      R.drawable.img_main10,
                      FOOD_TYPE_MAIN_COURSE))
    foodList.add(Food(null, "Yong Tau Foo", 4.0, R.drawable.img_main11, FOOD_TYPE_MAIN_COURSE))
    foodList.add(Food(null,
                      "Roast Meat / Roast Duck",
                      5.0,
                      R.drawable.img_main12,
                      FOOD_TYPE_MAIN_COURSE))
    foodList.add(Food(null, "Peranakan Food", 15.0, R.drawable.img_main13, FOOD_TYPE_MAIN_COURSE))
    foodList.add(Food(null, "Zi Char Meal", 34.0, R.drawable.img_main14, FOOD_TYPE_MAIN_COURSE))
    foodList.add(Food(null, "Tong Ah Kopitiam", 5.0, R.drawable.img_main15, FOOD_TYPE_MAIN_COURSE))
    foodList.add(Food(null, "Rojak", 3.0, R.drawable.img_main16, FOOD_TYPE_MAIN_COURSE))
    foodList.add(Food(null, "Satay", 6.5, R.drawable.img_main17, FOOD_TYPE_MAIN_COURSE))
    foodList.add(Food(null, "Nasi Padang", 7.0, R.drawable.img_main18, FOOD_TYPE_MAIN_COURSE))
    foodList.add(Food(null, "Nasi Lemak", 5.5, R.drawable.img_main19, FOOD_TYPE_MAIN_COURSE))
    foodList.add(Food(null, "Murtabak", 8.0, R.drawable.img_main20, FOOD_TYPE_MAIN_COURSE))
    foodList.add(Food(null,
                      "Chili Crab / Black Pepper Crab",
                      100.0,
                      R.drawable.img_main21,
                      FOOD_TYPE_MAIN_COURSE))
    foodList.add(Food(null, "Sambal Stingray", 15.0, R.drawable.img_main22, FOOD_TYPE_MAIN_COURSE))
    foodList.add(Food(null,
                      "Leng Heng BBQ & Claypot Deluxe",
                      20.0,
                      R.drawable.img_main23,
                      FOOD_TYPE_MAIN_COURSE))
    foodList.add(Food(null, "Fish Head Curry", 61.0, R.drawable.img_main24, FOOD_TYPE_MAIN_COURSE))
    foodList.add(Food(null, "Ice Kacang", 1.5, R.drawable.img_main25, FOOD_TYPE_MAIN_COURSE))
    foodList.add(Food(null, "Durian", 32.0, R.drawable.img_main26, FOOD_TYPE_MAIN_COURSE))

    foodList.add(Food(null, "Malibu Coconut", 48.0, R.drawable.img_alc1, FOOD_TYPE_ALCOHOL))
    foodList.add(Food(null, "ABSOLUT APEACH", 60.0, R.drawable.img_alc2, FOOD_TYPE_ALCOHOL))
    foodList.add(Food(null, "TIGER BEER CAN", 48.0, R.drawable.img_alc3, FOOD_TYPE_ALCOHOL))
    foodList.add(Food(null, "DON JULIO 1942", 219.0, R.drawable.img_alc4, FOOD_TYPE_ALCOHOL))
    foodList.add(Food(null, "BACARDI APPLE", 60.0, R.drawable.img_alc5, FOOD_TYPE_ALCOHOL))
    foodList.add(Food(null, "MARTELL VSOP", 105.0, R.drawable.img_alc6, FOOD_TYPE_ALCOHOL))
    foodList.add(Food(null, "FERNET BRANCA", 61.0, R.drawable.img_alc7, FOOD_TYPE_ALCOHOL))
    foodList.add(Food(null,
                      "CAPT. MORGAN SPICED RUM",
                      58.0,
                      R.drawable.img_alc8,
                      FOOD_TYPE_ALCOHOL))
    foodList.add(Food(null,
                      "LITTLE CREATURES PALE ALE",
                      118.0,
                      R.drawable.img_alc9,
                      FOOD_TYPE_ALCOHOL))
    foodList.add(Food(null,
                      "BOTTEGA GOLD PROSECCO DOC BRUT",
                      60.0,
                      R.drawable.img_alc10,
                      FOOD_TYPE_ALCOHOL))

    return foodList
}

fun getVenueList(): ArrayList<Venue> {
    val venueList = ArrayList<Venue>()

    venueList.add(Venue(null,
                        "Entire Venue at Zouk",
                        "River Valley Road The Cannery, 3C #01-05, Singapore, 179022",
                        R.drawable.img_venue15))
    venueList.add(Venue(null,
                        "Rooftop Terrace at ClubCo The Quadrant",
                        "Cecil Street, 19, Singapore, 049704",
                        R.drawable.img_venue16))
    venueList.add(Venue(null,
                        "Entire Venue at OSG Bar",
                        "Temasek Boulevard, 3, Singapore, 038983",
                        R.drawable.img_venue17))
    venueList.add(Venue(null,
                        "Island-style bar at Mogambo Bar & Restaurant",
                        "Canton Street, 3, Singapore, 049745",
                        R.drawable.img_venue18))
    venueList.add(Venue(null,
                        "The Cocktail Atelier at Grand Park Orchard",
                        "Orchard Road, 270, Singapore, 238857",
                        R.drawable.img_venue19))
    venueList.add(Venue(null,
                        "Entire Venue at Bikini Bar",
                        "Siloso Beach Walk, 50, Singapore, 099000",
                        R.drawable.img_venue20))
    venueList.add(Venue(null,
                        "Entire Space at Angie’s Oyster Bar",
                        "Raffles Place, 50, Singapore, 048623",
                        R.drawable.img_venue21))
    venueList.add(Venue(null,
                        "Exclusive Hire at Nickeldime Novena",
                        "Thomson Road, #01-01 Regency, , 275 , Singapore, 307645",
                        R.drawable.img_venue22))
    venueList.add(Venue(null,
                        "The Cocktail Bar at Monarchy Cocktail Bar & Ultra-Lounge",
                        "Tras Street, 56/58, Singapore, 078995",
                        R.drawable.img_venue23))
    venueList.add(Venue(null,
                        "Dining Lounge at Alley Bar",
                        "Emerald Hill Road, 2, Singapore, 229287",
                        R.drawable.img_venue24))
    venueList.add(Venue(null,
                        "Into The Woods at Into The Woods",
                        "Lavender Street, 213, Singapore, 338770",
                        R.drawable.img_venue25))
    venueList.add(Venue(null,
                        "Private Event Space at Design Hub Rooftop Event Space",
                        "Tuas Bay Drive, 30, Singapore, 637548",
                        R.drawable.img_venue26))
    venueList.add(Venue(null,
                        "Hyperspace Studio at HYPERSPACE",
                        "Lavender Street, 91a, Singapore, 338719",
                        R.drawable.img_venue27))
    venueList.add(Venue(null,
                        "Rooftop Lounge & Terrace at the Hive New Bridge Road",
                        "New Bridge Road, 59, Singapore, 059405",
                        R.drawable.img_venue28))
    venueList.add(Venue(null,
                        "Entire Venue at HERE",
                        "Serangoon Road, 576A, Singapore, 218190",
                        R.drawable.img_venue29))
    venueList.add(Venue(null,
                        "Outdoor Terrace at PARKROYAL on Pickering",
                        "Upper Pickering Street, 3, Singapore, 058289",
                        R.drawable.img_venue30))
    venueList.add(Venue(null,
                        "Terrace Lounge at Zafferano Italian Restaurant & Lounge",
                        "Collyer Quay, Ocean Financial Centre, Level 43, 10, Singapore, 049315",
                        R.drawable.img_venue31))
    venueList.add(Venue(null,
                        "Entire Space at LDF KALLANG RIVERSIDE UPSTAIRS 楼上",
                        "Kampong Bugis, 66, Singapore, 338987",
                        R.drawable.img_venue32))
    venueList.add(Venue(null,
                        "Infinity at Village Hotel Changi",
                        "Netheravon Road, 1, Singapore, 508502",
                        R.drawable.img_venue33))
    venueList.add(Venue(null,
                        "Roof Deck Alfresco dining at Jayleen 1918",
                        "Carpenter Street, 42, Singapore, 059921",
                        R.drawable.img_venue34))
    venueList.add(Venue(null,
                        "Hive Café & Event Space at The Hive Lavender",
                        "Kallang Junction, Level 6, 1, Singapore, 339263 ",
                        R.drawable.img_venue35))
    venueList.add(Venue(null,
                        "Long Beach at Coastes",
                        "Siloso Beach Walk, 50, Singapore, 099000 ",
                        R.drawable.img_venue36))
    venueList.add(Venue(null,
                        "Whole Venue at HYPERSPACE",
                        "Lavender Street, 91a, Singapore, 338719",
                        R.drawable.img_venue37))

    return venueList
}

fun getCatererList(): ArrayList<Caterers> {
    val catererList = ArrayList<Caterers>()

    catererList.add(Caterers(null, "Sungei Road Laksa", "27 Jalan Berseh", R.drawable.img_venue1))
    catererList.add(Caterers(null,
                             "Outram Park Yahua Rou Gu Cha",
                             "7 Keppel Rd, PSA Tanjong Pagar Complex, 089053",
                             R.drawable.img_venue2))
    catererList.add(Caterers(null,
                             "Nam Sing Fried Hokkien Mee",
                             "Old Airport Food Centre, 51 Old Airport Rd",
                             R.drawable.img_venue3))
    catererList.add(Caterers(null,
                             "Tian Tian Chicken Rice",
                             "Maxwell Food Centre, 1 Kadayanallur St",
                             R.drawable.img_venue4))
    catererList.add(Caterers(null,
                             "Hill Street Char Kway Teow",
                             ": #01-41,16 Bedok South Rd",
                             R.drawable.img_venue5))
    catererList.add(Caterers(null,
                             "Song Zhou Luo Bo Gao",
                             "#01-18, Bedok Interchange Food Centre, 207 New Upper Changi Road",
                             R.drawable.img_venue6))
    catererList.add(Caterers(null,
                             "Jing Hua Sliced Fish Bee Hoon",
                             "Stall #77 at Maxwell Food Centre, 1 Kadayanallur St",
                             R.drawable.img_venue7))
    catererList.add(Caterers(null,
                             "Hill Street Tai Hwa Pork Noodles",
                             "Block 466 Crawford Lane #01-12 Singapore 190465",
                             R.drawable.img_venue8))
    catererList.add(Caterers(null,
                             "Ah Chuan Fried Oyster Omelette",
                             "Toa Payoh Lor 7 Food Centre Stall #01-25, Singapore",
                             R.drawable.img_venue9))
    catererList.add(Caterers(null,
                             "Yong Xiang Xing Tou Foo",
                             "32 New Market Rd, 01-1084 People’s Park Food Centre",
                             R.drawable.img_venue10))
    catererList.add(Caterers(null,
                             "Kim Heng Roasted Meat",
                             "214 Serangoon Avenue 4, #01-88, Singapore 550214",
                             R.drawable.img_venue11))
    catererList.add(Caterers(null,
                             "Kok Sen Restaurant",
                             "30 Keong Saik Rd, Singapore",
                             R.drawable.img_venue12))
    catererList.add(Caterers(null,
                             "Two Chefs Eating House",
                             "116 Commonwealth Crescent #01-129, Singapore",
                             R.drawable.img_venue13))
    catererList.add(Caterers(null,
                             "Selera Rasa Nasi Lemak",
                             "2 Adam Rd, #01-02, Adam Road Food Centre, Singapore",
                             R.drawable.img_venue14))

    return catererList
}

fun convertUnfinished(partyDetails: PartyDetails, uid: Int): UnfinishedDetails {
    val gson = Gson()
    return UnfinishedDetails(
        null,
        uid,
        gson.toJson(partyDetails.partyDate),
        partyDetails.partyBudget,
        partyDetails.partyDestination,
        partyDetails.partyGuest,
        partyDetails.partyType,
        gson.toJson(partyDetails.selectedFood),
        gson.toJson(partyDetails.selectedDestination),
        gson.toJson(partyDetails.selectedCaterers)
    )
}

fun convertSummary(partyDetails: PartyDetails, uid: Int): SummaryDetails {
    val gson = Gson()
    return SummaryDetails(
        null,
        uid,
        gson.toJson(partyDetails.partyDate),
        partyDetails.partyBudget,
        partyDetails.partyDestination,
        partyDetails.partyGuest,
        partyDetails.partyType,
        gson.toJson(partyDetails.selectedFood),
        gson.toJson(partyDetails.selectedDestination),
        gson.toJson(partyDetails.selectedCaterers)
    )
}

fun convertHistorySummary(partyDetails: PartyDetails, uid: Int): HistorySummary {
    val gson = Gson()
    return HistorySummary(
        null,
        uid,
        gson.toJson(partyDetails.partyDate),
        partyDetails.partyBudget,
        partyDetails.partyDestination,
        partyDetails.partyGuest,
        partyDetails.partyType,
        gson.toJson(partyDetails.selectedFood),
        gson.toJson(partyDetails.selectedDestination),
        gson.toJson(partyDetails.selectedCaterers)
    )
}

fun convertPartyFromSummary(summary: SummaryDetails): PartyDetails {
    val gson = Gson()

    val type = object : TypeToken<ArrayList<Food>>() {}.type
    val selectedFood = gson.fromJson<ArrayList<Food>>(summary.selectedFood, type)

    return PartyDetails(
        gson.fromJson(summary.partyDate, Date::class.java),
        summary.partyBudget,
        summary.partyDestination,
        summary.partyGuest,
        summary.partyType,
        selectedFood,
        gson.fromJson(summary.selectedDestination, Venue::class.java),
        gson.fromJson(summary.selectedCaterers, Caterers::class.java)
    )
}

fun convertPartyFromHistorySummary(summary: HistorySummary): PartyDetails {
    val gson = Gson()

    val type = object : TypeToken<ArrayList<Food>>() {}.type
    val selectedFood = gson.fromJson<ArrayList<Food>>(summary.selectedFood, type)

    return PartyDetails(
        gson.fromJson(summary.partyDate, Date::class.java),
        summary.partyBudget,
        summary.partyDestination,
        summary.partyGuest,
        summary.partyType,
        selectedFood,
        gson.fromJson(summary.selectedDestination, Venue::class.java),
        gson.fromJson(summary.selectedCaterers, Caterers::class.java)
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