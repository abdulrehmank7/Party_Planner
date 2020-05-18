package com.arkapp.partyplanner.utils

import com.arkapp.partyplanner.R
import com.arkapp.partyplanner.data.models.Food

/**
 * Created by Abdul Rehman on 17-05-2020.
 * Contact email - abdulrehman0796@gmail.com
 */

const val FOOD_TYPE_ALCOHOL = "ALCOHOL"
const val FOOD_TYPE_CAKE = "CAKE"
const val FOOD_TYPE_MAIN_COURSE = "MAIN_COURSE"

fun getFoodList(): ArrayList<Food> {
    val foodList = ArrayList<Food>()

    foodList.add(Food(null, "Jara Petit", 10.0, R.drawable.img_cake1, FOOD_TYPE_CAKE))
    foodList.add(Food(null, "Marble Slab Creamery", 20.0, R.drawable.img_cake2, FOOD_TYPE_CAKE))
    foodList.add(Food(null, "The White Ombré", 5.0, R.drawable.img_cake3, FOOD_TYPE_CAKE))
    foodList.add(Food(null, "Cup Cake", 10.0, R.drawable.img_cake4, FOOD_TYPE_CAKE))
    foodList.add(Food(null, "Chocolates", 5.0, R.drawable.img_cake5, FOOD_TYPE_CAKE))
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