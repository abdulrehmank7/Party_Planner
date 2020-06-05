package com.arkapp.partyplanner.utils;

import com.arkapp.partyplanner.R;
import com.arkapp.partyplanner.data.models.Caterer;
import com.arkapp.partyplanner.data.models.CheckedItem;
import com.arkapp.partyplanner.data.models.HistorySummary;
import com.arkapp.partyplanner.data.models.Location;
import com.arkapp.partyplanner.data.models.PartyDetails;
import com.arkapp.partyplanner.data.models.PartyType;
import com.arkapp.partyplanner.data.models.SummaryDetails;
import com.arkapp.partyplanner.data.models.UnfinishedDetails;
import com.arkapp.partyplanner.data.models.Venue;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Date;

import kotlin.collections.CollectionsKt;
import kotlin.random.Random;
import kotlin.ranges.IntRange;
import kotlin.ranges.RangesKt;


/**
 * Created by Abdul Rehman on 17-05-2020.
 * Contact email - abdulrehman0796@gmail.com
 */

/**
 * This file contain all the constant value used in the app
 */
public class AppDataKt {

    public final static double LOW_BUDGED_LIMIT = 200.0D;
    public final static double MEDIUM_BUDGED_LIMIT = 400.0D;
    public final static double HIGH_BUDGED_LIMIT = 600.0D;
    public final static double VERY_HIGH_BUDGED_LIMIT = 100000.0D;
    public final static int OPTION_CREATE = 0;
    public final static int OPTION_CHECKLIST = 1;
    public final static int OPTION_UNFINISHED = 2;
    public final static int OPTION_PAST = 3;

    public static final String PARTY_TYPE_BABY_SHOWER = "Baby Shower";
    public static final String PARTY_TYPE_SWEET_18 = "Sweet 18";
    public static final String PARTY_TYPE_SWEET_21 = "Sweet 21";
    public static final String PARTY_TYPE_REUNION = "Reunion";
    public static final String PARTY_TYPE_TEA_PARTY = "Tea Party";
    public static final String PARTY_TYPE_BREAK_FAST = "Breakfast";
    public static final String PARTY_TYPE_BBQ_PARTY = "BBQ Party";
    public static final String PARTY_TYPE_BACHELOR_PARTY = "Bachelor Party";
    public static final String PARTY_TYPE_KIDS_PARTY = "Kids Party";
    public static final String PARTY_TYPE_FORMAL_PARTY = "Formal Party";
    public static final String PARTY_TYPE_CHRISTMAS_PARTY = "Christmas Party";
    public static final String PARTY_TYPE_ALCOHOL = "Alcohol";
    public static final String PARTY_TYPE_MAGIC_SHOW = "Magic Show";
    public static final String PARTY_TYPE_DECORATION = "Party Decoration";

    public static final String LOCATION_NORTH = "North";
    public static final String LOCATION_SOUTH = "South";
    public static final String LOCATION_EAST = "East";
    public static final String LOCATION_WEST = "West";
    public static final String LOCATION_CITY = "City";
    public static final String LOCATION_NORTH_EAST = "North East";
    public static final String LOCATION_CENTRAL = "Central";

    public static final String CB_PARTY_TYPE = "CB_PARTY_TYPE";
    public static final String CB_CATERER = "CB_CATERER";
    public static final String CB_VENUE = "CB_VENUE";
    public static final String CB_DECORATOR = "CB_DECORATOR";
    public static final String CB_MAGIC_SHOW = "CB_MAGIC_SHOW";
    public static final String CB_ALCOHOL = "CB_ALCOHOL";
    public static final String CB_BUDGET = "CB_BUDGET";

    private static String ENTERED_USER_NAME = "";

    private static int CURRENT_SELECTED_OPTION;

    private static ArrayList<CheckedItem> GUEST_LIST_NAMES = new ArrayList<>();

    private static boolean OPENED_GUEST_LIST;

    private static Gson gson = new Gson();


    public static String getENTERED_USER_NAME() {
        return ENTERED_USER_NAME;
    }

    public static void setENTERED_USER_NAME(String var0) {
        ENTERED_USER_NAME = var0;
    }

    public static int getCURRENT_SELECTED_OPTION() {
        return CURRENT_SELECTED_OPTION;
    }

    public static void setCURRENT_SELECTED_OPTION(int var0) {
        CURRENT_SELECTED_OPTION = var0;
    }


    public static ArrayList<CheckedItem> getGUEST_LIST_NAMES() {
        return GUEST_LIST_NAMES;
    }

    public static void setGUEST_LIST_NAMES(ArrayList<CheckedItem> var0) {
        GUEST_LIST_NAMES = var0;
    }

    public static boolean getOPENED_GUEST_LIST() {
        return OPENED_GUEST_LIST;
    }

    public static void setOPENED_GUEST_LIST(boolean var0) {
        OPENED_GUEST_LIST = var0;
    }

    //Get all the party type supported in app
    public static ArrayList<PartyType> getPartyTypes() {
        ArrayList<PartyType> list = new ArrayList<>();
        list.add(new PartyType(PARTY_TYPE_SWEET_18, R.drawable.ic_birthday));
        list.add(new PartyType(PARTY_TYPE_BABY_SHOWER, R.drawable.ic_baby));
        list.add(new PartyType(PARTY_TYPE_SWEET_21, R.drawable.ic_birthday));
        list.add(new PartyType(PARTY_TYPE_REUNION, R.drawable.ic_team));
        list.add(new PartyType(PARTY_TYPE_TEA_PARTY, R.drawable.ic_tea));
        list.add(new PartyType(PARTY_TYPE_BREAK_FAST, R.drawable.ic_bread));
        list.add(new PartyType(PARTY_TYPE_BBQ_PARTY, R.drawable.ic_meat));
        list.add(new PartyType(PARTY_TYPE_ALCOHOL, R.drawable.ic_food));
        list.add(new PartyType(PARTY_TYPE_BACHELOR_PARTY, R.drawable.ic_fun));
        list.add(new PartyType(PARTY_TYPE_KIDS_PARTY, R.drawable.ic_toy));
        list.add(new PartyType(PARTY_TYPE_FORMAL_PARTY, R.drawable.ic_dress_code));
        list.add(new PartyType(PARTY_TYPE_CHRISTMAS_PARTY, R.drawable.ic_christmas_tree));
        return list;
    }

    //Get all the location for the venues
    public static ArrayList<Location> getLocation() {
        ArrayList<Location> list = new ArrayList<>();
        list.add(new Location(LOCATION_NORTH, R.drawable.ic_compass));
        list.add(new Location(LOCATION_SOUTH, R.drawable.ic_compass));
        list.add(new Location(LOCATION_CENTRAL, R.drawable.ic_compass));
        list.add(new Location(LOCATION_EAST, R.drawable.ic_compass));
        list.add(new Location(LOCATION_WEST, R.drawable.ic_compass));
        list.add(new Location(LOCATION_NORTH_EAST, R.drawable.ic_compass));
        list.add(new Location(LOCATION_CITY, R.drawable.ic_urban));
        return list;
    }

    //Get all the party type from the SQL value
    public static ArrayList<PartyType> getPartyTypeFromStringArray(ArrayList<String> stringList) {
        ArrayList<PartyType> newPartyTypeList = new ArrayList<>();
        ArrayList<PartyType> allPartyType = getPartyTypes();

        for (String partyTypeName : stringList) {
            for (PartyType partyType : allPartyType) {
                if (partyType.getTitle().equals(partyTypeName)) {
                    newPartyTypeList.add(partyType);
                    break;
                }
            }
        }

        return newPartyTypeList;
    }

    //Get all the venues supported in app
    public static ArrayList<Venue> getVenueList() {
        ArrayList<Venue> venueList = new ArrayList<>();

        venueList.add(
                new Venue(
                        null,
                        "Main Dining Room",
                        "2 Circular Road, Singapore 049358",
                        50,
                        LOCATION_CITY,
                        gson.toJson(CollectionsKt.arrayListOf(PARTY_TYPE_SWEET_21,
                                PARTY_TYPE_SWEET_18,
                                PARTY_TYPE_FORMAL_PARTY,
                                PARTY_TYPE_TEA_PARTY,
                                PARTY_TYPE_BREAK_FAST,
                                PARTY_TYPE_BACHELOR_PARTY,
                                PARTY_TYPE_BABY_SHOWER)),
                        "6805 818 1",
                        "7000"));

        venueList.add(
                new Venue(
                        null,
                        "Sky Lounge",
                        "5 Coleman StreetSingapore 179805",
                        40,
                        LOCATION_CITY,
                        gson.toJson(CollectionsKt.arrayListOf(PARTY_TYPE_REUNION,
                                PARTY_TYPE_CHRISTMAS_PARTY,
                                PARTY_TYPE_SWEET_21,
                                PARTY_TYPE_BABY_SHOWER,
                                PARTY_TYPE_SWEET_18,
                                PARTY_TYPE_KIDS_PARTY,
                                PARTY_TYPE_FORMAL_PARTY,
                                PARTY_TYPE_BACHELOR_PARTY)),
                        "6416 1033",
                        "1500"));

        venueList.add(
                new Venue(
                        null,
                        "Rustic Studio Perfect for 21st Party",
                        "203D Lavender StreetSingapore 338763",
                        35,
                        LOCATION_CITY,
                        gson.toJson(CollectionsKt.arrayListOf(PARTY_TYPE_CHRISTMAS_PARTY,
                                PARTY_TYPE_BACHELOR_PARTY,
                                PARTY_TYPE_TEA_PARTY,
                                PARTY_TYPE_BBQ_PARTY,
                                PARTY_TYPE_KIDS_PARTY)),
                        "6416 1033",
                        "500"));

        venueList.add(
                new Venue(
                        null,
                        "Beautiful Rooftop Perfect for 21st birthday",
                        "203D Lavender Street Singapore 338763",
                        35,
                        LOCATION_CITY,
                        gson.toJson(CollectionsKt.arrayListOf(PARTY_TYPE_CHRISTMAS_PARTY,
                                PARTY_TYPE_SWEET_21,
                                PARTY_TYPE_SWEET_18,
                                PARTY_TYPE_BABY_SHOWER,
                                PARTY_TYPE_TEA_PARTY,
                                PARTY_TYPE_BBQ_PARTY,
                                PARTY_TYPE_BACHELOR_PARTY)),
                        "90234901",
                        "500"));

        venueList.add(
                new Venue(
                        null,
                        "Shake Farm HQ Level 2 Lounge",
                        "126 Telok Ayer StreetSingapore 0689595",
                        40,
                        LOCATION_CITY,
                        gson.toJson(CollectionsKt.arrayListOf(PARTY_TYPE_FORMAL_PARTY,
                                PARTY_TYPE_BBQ_PARTY,
                                PARTY_TYPE_REUNION,
                                PARTY_TYPE_BREAK_FAST,
                                PARTY_TYPE_CHRISTMAS_PARTY,
                                PARTY_TYPE_BACHELOR_PARTY,
                                PARTY_TYPE_KIDS_PARTY,
                                PARTY_TYPE_BABY_SHOWER)),
                        "85029150",
                        "500"));

        venueList.add(
                new Venue(
                        null,
                        "Private Dining Space",
                        "Blk 8D Dempsey Road #01-01ASingapore 249672",
                        50,
                        LOCATION_CENTRAL,
                        gson.toJson(CollectionsKt.arrayListOf(PARTY_TYPE_CHRISTMAS_PARTY,
                                PARTY_TYPE_REUNION,
                                PARTY_TYPE_SWEET_21,
                                PARTY_TYPE_SWEET_18,
                                PARTY_TYPE_FORMAL_PARTY,
                                PARTY_TYPE_BABY_SHOWER,
                                PARTY_TYPE_BACHELOR_PARTY)),
                        "90107418",
                        "2000"));

        venueList.add(
                new Venue(
                        null,
                        "Dining Lounge",
                        "2 Emerald Hill Road SingaporeSingapore 229287",
                        35,
                        LOCATION_CENTRAL,
                        gson.toJson(CollectionsKt.arrayListOf(PARTY_TYPE_FORMAL_PARTY,
                                PARTY_TYPE_CHRISTMAS_PARTY,
                                PARTY_TYPE_REUNION,
                                PARTY_TYPE_SWEET_21,
                                PARTY_TYPE_SWEET_18,
                                PARTY_TYPE_BABY_SHOWER,
                                PARTY_TYPE_BACHELOR_PARTY)),
                        "67388818",
                        "1000"));

        venueList.add(
                new Venue(
                        null,
                        "Private Room",
                        "2 Emerald Hill Road Singapore Singapore 229287",
                        50,
                        LOCATION_CENTRAL, gson.toJson(CollectionsKt.arrayListOf(PARTY_TYPE_BABY_SHOWER,
                        PARTY_TYPE_CHRISTMAS_PARTY,
                        PARTY_TYPE_REUNION,
                        PARTY_TYPE_SWEET_21,
                        PARTY_TYPE_BREAK_FAST,
                        PARTY_TYPE_SWEET_18,
                        PARTY_TYPE_BACHELOR_PARTY)),
                        "67388818",
                        "1000"));

        venueList.add(
                new Venue(
                        null,
                        "Urbana",
                        "99 Irrawaddy RoadSingapore 329568",
                        34,
                        LOCATION_CENTRAL, gson.toJson(CollectionsKt.arrayListOf(PARTY_TYPE_BABY_SHOWER,
                        PARTY_TYPE_CHRISTMAS_PARTY,
                        PARTY_TYPE_REUNION,
                        PARTY_TYPE_SWEET_21,
                        PARTY_TYPE_SWEET_18,
                        PARTY_TYPE_KIDS_PARTY,
                        PARTY_TYPE_BBQ_PARTY,
                        PARTY_TYPE_BACHELOR_PARTY)),
                        "62500303",
                        "4000"));

        venueList.add(
                new Venue(
                        null,
                        "Indoors Dining Area",
                        "33 Cuppage Road, Cuppage TerraceSingapore 229458",
                        36,
                        LOCATION_CENTRAL,
                        gson.toJson(CollectionsKt.arrayListOf(PARTY_TYPE_TEA_PARTY,
                                PARTY_TYPE_FORMAL_PARTY,
                                PARTY_TYPE_BBQ_PARTY,
                                PARTY_TYPE_CHRISTMAS_PARTY,
                                PARTY_TYPE_REUNION,
                                PARTY_TYPE_SWEET_21,
                                PARTY_TYPE_SWEET_18,
                                PARTY_TYPE_BABY_SHOWER)),
                        "93378432",
                        "1000"));

        venueList.add(
                new Venue(
                        null,
                        "Fully Equipped Training Space",
                        "201 Henderson Road #07-26Singapore 159545",
                        50,
                        LOCATION_SOUTH,
                        gson.toJson(CollectionsKt.arrayListOf(PARTY_TYPE_FORMAL_PARTY,
                                PARTY_TYPE_BBQ_PARTY,
                                PARTY_TYPE_CHRISTMAS_PARTY,
                                PARTY_TYPE_REUNION,
                                PARTY_TYPE_SWEET_21,
                                PARTY_TYPE_SWEET_18,
                                PARTY_TYPE_BREAK_FAST,
                                PARTY_TYPE_BABY_SHOWER)),
                        "91872149",
                        "101"));

        venueList.add(
                new Venue(
                        null,
                        "Swing Zone Area",
                        "1 Harbourfront Walk | #01-57 VivoCitySingapore 098585",
                        50,
                        LOCATION_SOUTH,
                        gson.toJson(CollectionsKt.arrayListOf(PARTY_TYPE_FORMAL_PARTY,
                                PARTY_TYPE_BBQ_PARTY,
                                PARTY_TYPE_CHRISTMAS_PARTY,
                                PARTY_TYPE_REUNION,
                                PARTY_TYPE_KIDS_PARTY,
                                PARTY_TYPE_BREAK_FAST)),
                        "91834910",
                        "1500"));

        venueList.add(
                new Venue(
                        null,
                        "Restaurant Space",
                        "5 Yong Siak StreetSingapore 168643",
                        48,
                        LOCATION_SOUTH,
                        gson.toJson(CollectionsKt.arrayListOf(PARTY_TYPE_CHRISTMAS_PARTY,
                                PARTY_TYPE_REUNION,
                                PARTY_TYPE_SWEET_21,
                                PARTY_TYPE_SWEET_18,
                                PARTY_TYPE_TEA_PARTY,
                                PARTY_TYPE_FORMAL_PARTY,
                                PARTY_TYPE_BACHELOR_PARTY)),
                        "98246294",
                        "5000"));

        venueList.add(
                new Venue(
                        null,
                        "Upper Place",
                        "",
                        40,
                        LOCATION_SOUTH,
                        gson.toJson(CollectionsKt.arrayListOf(PARTY_TYPE_FORMAL_PARTY,
                                PARTY_TYPE_CHRISTMAS_PARTY,
                                PARTY_TYPE_REUNION,
                                PARTY_TYPE_BREAK_FAST,
                                PARTY_TYPE_BBQ_PARTY,
                                PARTY_TYPE_BACHELOR_PARTY)),
                        "65951380",
                        "2000"));

        venueList.add(
                new Venue(
                        null,
                        "Function Room",
                        "1 Larkhill Road, Sentosa Island, Singapore 099394Singapore 099394",
                        50,
                        LOCATION_SOUTH,
                        gson.toJson(CollectionsKt.arrayListOf(PARTY_TYPE_CHRISTMAS_PARTY,
                                PARTY_TYPE_REUNION, PARTY_TYPE_SWEET_21,
                                PARTY_TYPE_SWEET_18,
                                PARTY_TYPE_BABY_SHOWER,
                                PARTY_TYPE_KIDS_PARTY,
                                PARTY_TYPE_BACHELOR_PARTY)),
                        "68253827",
                        "1000"));

        venueList.add(
                new Venue(
                        null,
                        "BAR SPACE (L•T•A LONG TIME AGO @ BOAT QUAY)",
                        "1 Fusionopolis Way #02-07 ConnexisSingapore 138632",
                        50,
                        LOCATION_WEST,
                        gson.toJson(CollectionsKt.arrayListOf(PARTY_TYPE_CHRISTMAS_PARTY,
                                PARTY_TYPE_REUNION,
                                PARTY_TYPE_BREAK_FAST,
                                PARTY_TYPE_TEA_PARTY,
                                PARTY_TYPE_BACHELOR_PARTY,
                                PARTY_TYPE_BABY_SHOWER)),
                        "98761338",
                        "500"));

        venueList.add(
                new Venue(
                        null,
                        "Outdoor Event Space",
                        "44 Rochester ParkSingapore 139248",
                        40,
                        LOCATION_WEST,
                        gson.toJson(CollectionsKt.arrayListOf(PARTY_TYPE_CHRISTMAS_PARTY,
                                PARTY_TYPE_REUNION,
                                PARTY_TYPE_SWEET_21,
                                PARTY_TYPE_SWEET_18,
                                PARTY_TYPE_TEA_PARTY,
                                PARTY_TYPE_BACHELOR_PARTY)),
                        "81577236",
                        "1000"));

        venueList.add(
                new Venue(
                        null,
                        "Rooftop Private Event Space",
                        "30 Tuas Bay DriveSingapore 637548",
                        50,
                        LOCATION_WEST,
                        gson.toJson(CollectionsKt.arrayListOf(PARTY_TYPE_CHRISTMAS_PARTY,
                                PARTY_TYPE_REUNION,
                                PARTY_TYPE_BREAK_FAST,
                                PARTY_TYPE_FORMAL_PARTY,
                                PARTY_TYPE_BACHELOR_PARTY)),
                        "97120047",
                        "1000"));

        venueList.add(
                new Venue(
                        null,
                        "Rooftop Gallery Space",
                        "3Ikea TampinesSingapore 528559",
                        50,
                        LOCATION_EAST,
                        gson.toJson(CollectionsKt.arrayListOf(PARTY_TYPE_CHRISTMAS_PARTY,
                                PARTY_TYPE_REUNION,
                                PARTY_TYPE_FORMAL_PARTY,
                                PARTY_TYPE_KIDS_PARTY,
                                PARTY_TYPE_BBQ_PARTY,
                                PARTY_TYPE_BACHELOR_PARTY)),
                        "88509813",
                        "1000"));

        venueList.add(
                new Venue(
                        null,
                        "Private Room",
                        "Kinex (Previously OneKM), #02-21, 11 Tanjong Katong Road Singapore 436950",
                        35,
                        LOCATION_EAST,
                        gson.toJson(CollectionsKt.arrayListOf(PARTY_TYPE_CHRISTMAS_PARTY,
                                PARTY_TYPE_REUNION, PARTY_TYPE_SWEET_21,
                                PARTY_TYPE_SWEET_18,
                                PARTY_TYPE_KIDS_PARTY,
                                PARTY_TYPE_BACHELOR_PARTY)),
                        "83281198",
                        "500"));
        venueList.add(
                new Venue(
                        null,
                        "The Seagrill",
                        "Changi Beach Park, 260 Nicoll DriveSingapore 498991",
                        50,
                        LOCATION_EAST,
                        gson.toJson(CollectionsKt.arrayListOf(PARTY_TYPE_CHRISTMAS_PARTY,
                                PARTY_TYPE_REUNION, PARTY_TYPE_SWEET_21,
                                PARTY_TYPE_SWEET_18,
                                PARTY_TYPE_FORMAL_PARTY,
                                PARTY_TYPE_BACHELOR_PARTY)),
                        "92279928",
                        "2000"));
        venueList.add(
                new Venue(
                        null,
                        "Alfresco Dining Area",
                        "5 Changi Business Park Central #01-68/69 Changi City Point Singapore (486038)Singapore 486038",
                        33,
                        LOCATION_EAST,
                        gson.toJson(CollectionsKt.arrayListOf(PARTY_TYPE_CHRISTMAS_PARTY,
                                PARTY_TYPE_REUNION, PARTY_TYPE_SWEET_21,
                                PARTY_TYPE_SWEET_18,
                                PARTY_TYPE_KIDS_PARTY,
                                PARTY_TYPE_BACHELOR_PARTY)),
                        "90014554",
                        "1500"));
        venueList.add(
                new Venue(
                        null,
                        "Event Room",
                        "1 Tampines Walk, #03-03 Our Tampines HubSingapore 520940",
                        50,
                        LOCATION_EAST,
                        gson.toJson(CollectionsKt.arrayListOf(PARTY_TYPE_CHRISTMAS_PARTY,
                                PARTY_TYPE_REUNION, PARTY_TYPE_SWEET_21,
                                PARTY_TYPE_SWEET_18,
                                PARTY_TYPE_FORMAL_PARTY,
                                PARTY_TYPE_BACHELOR_PARTY)),
                        "67059416",
                        "250"));

        venueList.add(
                new Venue(
                        null,
                        "Meeting Room",
                        "217 Syed Alwi RoadSingapore 207776",
                        40,
                        LOCATION_NORTH_EAST,
                        gson.toJson(CollectionsKt.arrayListOf(PARTY_TYPE_CHRISTMAS_PARTY,
                                PARTY_TYPE_REUNION, PARTY_TYPE_SWEET_21,
                                PARTY_TYPE_SWEET_18,
                                PARTY_TYPE_KIDS_PARTY,
                                PARTY_TYPE_BACHELOR_PARTY)),
                        "91464302",
                        "500"));

        venueList.add(
                new Venue(
                        null,
                        "Alfresco Space",
                        "3 Punggol Point Road The Punggol Settlement #01-05Singapore 828694",
                        40,
                        LOCATION_NORTH_EAST,
                        gson.toJson(CollectionsKt.arrayListOf(PARTY_TYPE_CHRISTMAS_PARTY,
                                PARTY_TYPE_REUNION,
                                PARTY_TYPE_SWEET_21,
                                PARTY_TYPE_SWEET_18,
                                PARTY_TYPE_FORMAL_PARTY,
                                PARTY_TYPE_BBQ_PARTY,
                                PARTY_TYPE_BABY_SHOWER,
                                PARTY_TYPE_BACHELOR_PARTY)),
                        "96458881",
                        "1000"));

        venueList.add(
                new Venue(
                        null,
                        "Kingfisher Room",
                        "3 Punggol Point Road The Punggol Settlement #01-05 Singapore 828694",
                        40,
                        LOCATION_NORTH,
                        gson.toJson(CollectionsKt.arrayListOf(PARTY_TYPE_CHRISTMAS_PARTY,
                                PARTY_TYPE_REUNION,
                                PARTY_TYPE_SWEET_21,
                                PARTY_TYPE_SWEET_18,
                                PARTY_TYPE_BABY_SHOWER,
                                PARTY_TYPE_BBQ_PARTY,
                                PARTY_TYPE_KIDS_PARTY,
                                PARTY_TYPE_BACHELOR_PARTY)),
                        "64860872",
                        "500"));
        return venueList;
    }

    //Get all the caterer supported in app
    public static ArrayList<Caterer> getCatererList() {
        ArrayList<Caterer> catererList = new ArrayList<>();
        catererList.add(new Caterer(null,
                "BBQ House Singapore Pte. Ltd foodline",
                "https://www.foodline.sg/catering/BBQ-House-Singapore-Pte.-Ltd/",
                "6100 0029",
                7.0,
                gson.toJson(CollectionsKt.arrayListOf(PARTY_TYPE_BBQ_PARTY,
                        PARTY_TYPE_BACHELOR_PARTY))
        ));

        catererList.add(new Caterer(null,
                "Mmmm! foodline",
                "https://www.foodline.sg/catering/Mmmm!/",
                "6100 0029",
                6.0,
                gson.toJson(CollectionsKt.arrayListOf(PARTY_TYPE_BBQ_PARTY,
                        PARTY_TYPE_BACHELOR_PARTY))
        ));

        catererList.add(new Caterer(null,
                "Catering Culture",
                "https://www.foodline.sg/catering/Catering-Culture/",
                "6100 0029",
                9.0,
                gson.toJson(CollectionsKt.arrayListOf(PARTY_TYPE_BBQ_PARTY,
                        PARTY_TYPE_CHRISTMAS_PARTY))
        ));

        catererList.add(new Caterer(null,
                "Angel's Restaurant foodline",
                "https://www.foodline.sg/catering/Angels-Restaurant/",
                "6100 0029",
                15.0,
                gson.toJson(CollectionsKt.arrayListOf(PARTY_TYPE_BBQ_PARTY,
                        PARTY_TYPE_REUNION,
                        PARTY_TYPE_BACHELOR_PARTY))
        ));
        catererList.add(new Caterer(null,
                "FattyDaddyFattyMummy foodline",
                ": https://www.foodline.sg/catering/FattyDaddyFattyMummy/",
                "6100 0029",
                19.0,
                gson.toJson(CollectionsKt.arrayListOf(PARTY_TYPE_BBQ_PARTY,
                        PARTY_TYPE_CHRISTMAS_PARTY,
                        PARTY_TYPE_BACHELOR_PARTY))
        ));
        catererList.add(new Caterer(null,
                "Occasions Catering foodline",
                "https://www.foodline.sg/catering/Occasions-Catering/",
                "6100 0029",
                28.0,
                gson.toJson(CollectionsKt.arrayListOf(PARTY_TYPE_BBQ_PARTY,
                        PARTY_TYPE_REUNION,
                        PARTY_TYPE_BACHELOR_PARTY))
        ));
        catererList.add(new Caterer(null,
                "Opah Satay foodline",
                "https://www.foodline.sg/catering/Opah-Satay/",
                "6100 0029",
                35.0,
                gson.toJson(CollectionsKt.arrayListOf(PARTY_TYPE_BBQ_PARTY,
                        PARTY_TYPE_REUNION,
                        PARTY_TYPE_BACHELOR_PARTY))
        ));
        catererList.add(new Caterer(null,
                "The Connoisseur Concerto- TCC foodline",
                "https://www.foodline.sg/catering/The-Connoisseur-Concerto--TCC/",
                "6100 0029",
                4.0,
                gson.toJson(CollectionsKt.arrayListOf(PARTY_TYPE_TEA_PARTY, PARTY_TYPE_FORMAL_PARTY))
        ));
        catererList.add(new Caterer(null,
                "Fung Kee",
                "https://www.foodline.sg/catering/Fung-Kee/",
                "6100 0029",
                6.0,
                gson.toJson(CollectionsKt.arrayListOf(PARTY_TYPE_TEA_PARTY,
                        PARTY_TYPE_CHRISTMAS_PARTY,
                        PARTY_TYPE_FORMAL_PARTY, PARTY_TYPE_BREAK_FAST))
        ));
        catererList.add(new Caterer(null,
                "QQ Catering",
                "https://www.foodline.sg/catering/QQ-Catering/",
                "6100 0029",
                8.0,
                gson.toJson(CollectionsKt.arrayListOf(PARTY_TYPE_TEA_PARTY,
                        PARTY_TYPE_CHRISTMAS_PARTY,
                        PARTY_TYPE_FORMAL_PARTY, PARTY_TYPE_BREAK_FAST))
        ));
        catererList.add(new Caterer(null,
                "Papitto Gelato",
                "https://www.foodline.sg/catering/Papitto-Gelato/",
                "6100 0029",
                13.0,
                gson.toJson(CollectionsKt.arrayListOf(PARTY_TYPE_TEA_PARTY,
                        PARTY_TYPE_FORMAL_PARTY,
                        PARTY_TYPE_BREAK_FAST))
        ));
        catererList.add(new Caterer(null,
                "Qi Ji",
                "https://www.foodline.sg/catering/Qi-Ji/",
                "6100 0029",
                6.0,
                gson.toJson(CollectionsKt.arrayListOf(PARTY_TYPE_BACHELOR_PARTY,
                        PARTY_TYPE_CHRISTMAS_PARTY,
                        PARTY_TYPE_BREAK_FAST))
        ));
        catererList.add(new Caterer(null,
                "A-One Signature foodline",
                "https://www.foodline.sg/catering/A-One-Signature/",
                "6100 0029",
                12.0,
                gson.toJson(CollectionsKt.arrayListOf(PARTY_TYPE_BACHELOR_PARTY,
                        PARTY_TYPE_BBQ_PARTY, PARTY_TYPE_BREAK_FAST))
        ));
        catererList.add(new Caterer(null,
                "Curry & Tandoor Pte Ltd",
                "https://www.foodline.sg/catering/Curry-N-Tandoor-Pte-Ltd/",
                "6100 0029",
                15.0,
                gson.toJson(CollectionsKt.arrayListOf(PARTY_TYPE_BACHELOR_PARTY,
                        PARTY_TYPE_CHRISTMAS_PARTY,
                        PARTY_TYPE_BREAK_FAST))
        ));
        catererList.add(new Caterer(null,
                "79 After Dark Catering",
                "https://www.foodline.sg/catering/79-After-Dark-Catering/",
                "6100 0029",
                20.0,
                gson.toJson(CollectionsKt.arrayListOf(PARTY_TYPE_BACHELOR_PARTY, PARTY_TYPE_REUNION))
        ));
        catererList.add(new Caterer(null,
                "Nosh Kitchen foodline",
                "https://www.foodline.sg/catering/Nosh-Kitchen/",
                "6100 0029",
                25.0,
                gson.toJson(CollectionsKt.arrayListOf(PARTY_TYPE_BACHELOR_PARTY, PARTY_TYPE_REUNION))
        ));
        catererList.add(new Caterer(null,
                "Fu Kwee new Caterer Pte Ltd foodline",
                ": https://www.foodline.sg/catering/Fu-Kwee-new Caterer-Pte-Ltd/",
                "6100 0029",
                30.0,
                gson.toJson(CollectionsKt.arrayListOf(PARTY_TYPE_BACHELOR_PARTY, PARTY_TYPE_REUNION))
        ));
        catererList.add(new Caterer(null,
                "Wee Nam Kee Chicken Rice foodline",
                "https://www.foodline.sg/catering/Wee-Nam-Kee-Chicken-Rice/",
                "6100 0029",
                32.0,
                gson.toJson(CollectionsKt.arrayListOf(PARTY_TYPE_BACHELOR_PARTY, PARTY_TYPE_REUNION))
        ));
        catererList.add(new Caterer(null,
                "Grain",
                "https://www.foodline.sg/catering/Grain/",
                "6100 0029",
                35.0,
                gson.toJson(CollectionsKt.arrayListOf(PARTY_TYPE_BACHELOR_PARTY, PARTY_TYPE_REUNION))
        ));
        catererList.add(new Caterer(null,
                "Time For Thai foodline",
                "https://www.foodline.sg/catering/Time-For-Thai/",
                "6100 0029",
                38.0,
                gson.toJson(CollectionsKt.arrayListOf(PARTY_TYPE_BACHELOR_PARTY, PARTY_TYPE_REUNION))
        ));
        catererList.add(new Caterer(null,
                "Mum’s Kitchen",
                "https://www.foodline.sg/catering/Mums-Kitchen/",
                "6100 0029",
                20.0,
                gson.toJson(CollectionsKt.arrayListOf(PARTY_TYPE_BACHELOR_PARTY,
                        PARTY_TYPE_CHRISTMAS_PARTY, PARTY_TYPE_REUNION))
        ));
        catererList.add(new Caterer(null,
                "Big Boys Sizzling Hot Plate Western foodline",
                "https://www.foodline.sg/catering/Big-Boys-Sizzling-Hot-Plate-Western/",
                "6100 0029",
                45.0,
                gson.toJson(CollectionsKt.arrayListOf(PARTY_TYPE_BACHELOR_PARTY, PARTY_TYPE_REUNION))
        ));
        catererList.add(new Caterer(null,
                "Be Frank foodline",
                "https://www.foodline.sg/catering/Be-Frank/",
                "6100 0029",
                6.0,
                gson.toJson(CollectionsKt.arrayListOf(PARTY_TYPE_BABY_SHOWER,
                        PARTY_TYPE_KIDS_PARTY,
                        PARTY_TYPE_SWEET_21,
                        PARTY_TYPE_SWEET_18))
        ));
        catererList.add(new Caterer(null,
                "East West Fusion foodline",
                "https://www.foodline.sg/catering/East-West-Fusion/",
                "6100 0029",
                10.0,
                gson.toJson(CollectionsKt.arrayListOf(PARTY_TYPE_BABY_SHOWER,
                        PARTY_TYPE_SWEET_18,
                        PARTY_TYPE_FORMAL_PARTY,
                        PARTY_TYPE_KIDS_PARTY,
                        PARTY_TYPE_SWEET_21))
        ));
        catererList.add(new Caterer(null,
                "Delizio Catering (Thai Specialties) foodline",
                "https://www.foodline.sg/catering/Delizio-Catering-(Thai-Specialties)/",
                "6100 0029",
                13.0,
                gson.toJson(CollectionsKt.arrayListOf(PARTY_TYPE_BABY_SHOWER,
                        PARTY_TYPE_SWEET_18,
                        PARTY_TYPE_FORMAL_PARTY,
                        PARTY_TYPE_KIDS_PARTY,
                        PARTY_TYPE_SWEET_21))
        ));
        catererList.add(new Caterer(null,
                "WORD. Events and Catering foodline",
                "https://www.foodline.sg/catering/WORD.-Events-and-Catering/",
                "6100 0029",
                15.0,
                gson.toJson(CollectionsKt.arrayListOf(PARTY_TYPE_BABY_SHOWER,
                        PARTY_TYPE_KIDS_PARTY,
                        PARTY_TYPE_SWEET_21,
                        PARTY_TYPE_SWEET_18))
        ));
        catererList.add(new Caterer(null,
                "Sembawang Eating House Seafood Restaurant foodline",
                "https://www.foodline.sg/catering/Sembawang-Eating-House-Seafood-Restaurant/",
                "6100 0029",
                20.0,
                gson.toJson(CollectionsKt.arrayListOf(PARTY_TYPE_BABY_SHOWER,
                        PARTY_TYPE_SWEET_18,
                        PARTY_TYPE_FORMAL_PARTY,
                        PARTY_TYPE_KIDS_PARTY,
                        PARTY_TYPE_SWEET_21))
        ));
        catererList.add(new Caterer(null,
                "Xiang's Catering foodline",
                "https://www.foodline.sg/catering/Xiangs-Catering/",
                "6100 0029",
                25.0,
                gson.toJson(CollectionsKt.arrayListOf(PARTY_TYPE_BABY_SHOWER,
                        PARTY_TYPE_KIDS_PARTY,
                        PARTY_TYPE_SWEET_21,
                        PARTY_TYPE_SWEET_18))
        ));
        catererList.add(new Caterer(null,
                "Good Chance Catering foodline",
                "https://www.foodline.sg/catering/Good-Chance-Catering/",
                "6100 0029",
                30.0,
                gson.toJson(CollectionsKt.arrayListOf(PARTY_TYPE_BABY_SHOWER,
                        PARTY_TYPE_KIDS_PARTY,
                        PARTY_TYPE_SWEET_21,
                        PARTY_TYPE_SWEET_18))
        ));
        catererList.add(new Caterer(null,
                "The Catering Concerto by TCC foodline",
                "https://www.foodline.sg/catering/The-Catering-Concerto-by-TCC/",
                "6100 0029",
                8.0,
                gson.toJson(CollectionsKt.arrayListOf(PARTY_TYPE_BREAK_FAST, PARTY_TYPE_TEA_PARTY))
        ));
        catererList.add(new Caterer(null,
                "BellyGood new Caterer foodline",
                "https://www.foodline.sg/catering/BellyGood-new Caterer/",
                "6100 0029",
                10.0,
                gson.toJson(CollectionsKt.arrayListOf(PARTY_TYPE_BREAK_FAST, PARTY_TYPE_TEA_PARTY))
        ));
        catererList.add(new Caterer(null,
                "Serve Best Pte Ltd foodline",
                "https://www.foodline.sg/catering/Serve-Best-Pte-Ltd/",
                "6100 0029",
                15.0,
                gson.toJson(CollectionsKt.arrayListOf(PARTY_TYPE_BREAK_FAST, PARTY_TYPE_TEA_PARTY))
        ));
        catererList.add(new Caterer(null,
                "Katong Catering foodline",
                "https://www.foodline.sg/catering/Katong-Catering/",
                "6100 0029",
                17.0,
                gson.toJson(CollectionsKt.arrayListOf(PARTY_TYPE_BREAK_FAST, PARTY_TYPE_TEA_PARTY))
        ));
        catererList.add(new Caterer(null,
                "WE Cater foodline",
                "https://www.foodline.sg/catering/WE-Cater/",
                "6100 0029",
                20.0,
                gson.toJson(CollectionsKt.arrayListOf(PARTY_TYPE_BREAK_FAST, PARTY_TYPE_TEA_PARTY))
        ));
        catererList.add(new Caterer(null,
                " East West Noodles (S11) @ Matrix Cafeteria ITE College West foodline",
                "https://www.foodline.sg/catering/East-West-Noodles-(S11)-@-Matrix-Cafeteria-ITE-College-West/",
                "6100 0029",
                22.0,
                gson.toJson(CollectionsKt.arrayListOf(PARTY_TYPE_BREAK_FAST, PARTY_TYPE_TEA_PARTY))
        ));
        catererList.add(new Caterer(null,
                "Rasa Rasa Catering Services Pte Ltd foodline",
                "https://www.foodline.sg/catering/Rasa-Rasa-Catering-Services-Pte-Ltd/",
                "6100 0029",
                20.0,
                gson.toJson(CollectionsKt.arrayListOf(PARTY_TYPE_SWEET_18,
                        PARTY_TYPE_KIDS_PARTY,
                        PARTY_TYPE_SWEET_21,
                        PARTY_TYPE_CHRISTMAS_PARTY))
        ));
        catererList.add(new Caterer(null,
                "Fusion Spoon Catering Services foodline",
                "https://www.foodline.sg/catering/Fusion-Spoon-Catering-Services/",
                "6100 0029",
                25.0,
                gson.toJson(CollectionsKt.arrayListOf(PARTY_TYPE_SWEET_18,
                        PARTY_TYPE_KIDS_PARTY,
                        PARTY_TYPE_SWEET_21,
                        PARTY_TYPE_CHRISTMAS_PARTY))
        ));
        catererList.add(new Caterer(null,
                "WEEAT foodline",
                "https://www.foodline.sg/catering/WEEAT/",
                "6100 0029",
                30.0,
                gson.toJson(CollectionsKt.arrayListOf(PARTY_TYPE_SWEET_18,
                        PARTY_TYPE_KIDS_PARTY,
                        PARTY_TYPE_SWEET_21,
                        PARTY_TYPE_CHRISTMAS_PARTY))
        ));
        catererList.add(new Caterer(null,
                "Mixed Greens foodline",
                " https://www.foodline.sg/catering/Mixed-Greens/",
                "6100 0029",
                10.0,
                gson.toJson(CollectionsKt.arrayListOf(PARTY_TYPE_FORMAL_PARTY,
                        PARTY_TYPE_REUNION,
                        PARTY_TYPE_CHRISTMAS_PARTY))
        ));
        catererList.add(new Caterer(null,
                "THAI-LICIOUS 泰好吃 foodline",
                "https://www.foodline.sg/catering/THAI-LICIOUS--%E6%B3%B0%E5%A5%BD%E5%90%83/",
                "6100 0029",
                30.0,
                gson.toJson(CollectionsKt.arrayListOf(PARTY_TYPE_FORMAL_PARTY,
                        PARTY_TYPE_REUNION,
                        PARTY_TYPE_CHRISTMAS_PARTY))
        ));
        catererList.add(new Caterer(null,
                "Stamford Catering foodline",
                "https://www.foodline.sg/catering/Stamford-Catering/",
                "6100 0029",
                25.0,
                gson.toJson(CollectionsKt.arrayListOf(PARTY_TYPE_CHRISTMAS_PARTY,
                        PARTY_TYPE_REUNION,
                        PARTY_TYPE_BREAK_FAST))
        ));

        return catererList;
    }


    public static UnfinishedDetails convertUnfinished(PartyDetails partyDetails, int uid) {
        return new UnfinishedDetails(partyDetails.getId(), uid, gson.toJson(partyDetails.getPartyDate()), partyDetails.getPartyBudget(), partyDetails.getPartyDestination(), partyDetails.getPartyGuest(), gson.toJson(partyDetails.getPartyType()), gson.toJson(partyDetails.getSelectedDestination()), gson.toJson(partyDetails.getSelectedCaterer()), partyDetails.getExtraNote(), gson.toJson(partyDetails.getGuestNameList()), gson.toJson(partyDetails.getCheckedItemList()), gson.toJson(partyDetails.getLocations()));
    }


    public static SummaryDetails convertSummary(PartyDetails partyDetails, int uid) {
        return new SummaryDetails(partyDetails.getId(), uid, gson.toJson(partyDetails.getPartyDate()), partyDetails.getPartyBudget(), partyDetails.getPartyDestination(), partyDetails.getPartyGuest(), gson.toJson(partyDetails.getPartyType()), gson.toJson(partyDetails.getSelectedDestination()), gson.toJson(partyDetails.getSelectedCaterer()), partyDetails.getExtraNote(), gson.toJson(partyDetails.getGuestNameList()), gson.toJson(partyDetails.getCheckedItemList()), gson.toJson(partyDetails.getLocations()));
    }


    public static HistorySummary convertHistorySummary(PartyDetails partyDetails, int uid) {
        return new HistorySummary(partyDetails.getId(), uid, gson.toJson(partyDetails.getPartyDate()), partyDetails.getPartyBudget(), partyDetails.getPartyDestination(), partyDetails.getPartyGuest(), gson.toJson(partyDetails.getPartyType()), gson.toJson(partyDetails.getSelectedDestination()), gson.toJson(partyDetails.getSelectedCaterer()), partyDetails.getExtraNote(), gson.toJson(partyDetails.getGuestNameList()), gson.toJson(partyDetails.getCheckedItemList()), gson.toJson(partyDetails.getLocations()));
    }


    public static PartyDetails convertPartyFromUnfinished(UnfinishedDetails unfinishedDetails) {
        Type type1 = new TypeToken<ArrayList<String>>() {
        }.getType();
        Type type2 = new TypeToken<ArrayList<CheckedItem>>() {
        }.getType();

        ArrayList<String> selectedPartyType = gson.fromJson(unfinishedDetails.getPartyType(), type1);
        ArrayList<CheckedItem> guestList = gson.fromJson(unfinishedDetails.getGuestNameList(), type2);
        ArrayList<CheckedItem> checkedItemList = gson.fromJson(unfinishedDetails.getCheckedItemList(), type2);
        ArrayList<String> selectedLocation = gson.fromJson(unfinishedDetails.getLocations(), type1);

        return new PartyDetails(
                unfinishedDetails.getId(),
                gson.fromJson(unfinishedDetails.getPartyDate(), Date.class),
                unfinishedDetails.getPartyBudget(),
                unfinishedDetails.getPartyDestination(),
                unfinishedDetails.getPartyGuest(),
                selectedPartyType,
                gson.fromJson(unfinishedDetails.getSelectedCaterers(), Caterer.class),
                gson.fromJson(unfinishedDetails.getSelectedDestination(), Venue.class),
                unfinishedDetails.getExtraNote(),
                guestList,
                checkedItemList,
                selectedLocation
        );
    }


    public static PartyDetails convertPartyFromSummary(SummaryDetails summary) {
        Type type1 = new TypeToken<ArrayList<String>>() {
        }.getType();
        Type type2 = new TypeToken<ArrayList<CheckedItem>>() {
        }.getType();

        ArrayList<String> selectedPartyType = gson.fromJson(summary.getPartyType(), type1);
        ArrayList<CheckedItem> guestList = gson.fromJson(summary.getGuestNameList(), type2);
        ArrayList<CheckedItem> checkedItemList = gson.fromJson(summary.getCheckedItemList(), type2);
        ArrayList<String> selectedLocation = gson.fromJson(summary.getLocations(), type1);

        return new PartyDetails(
                summary.getId(),
                gson.fromJson(summary.getPartyDate(), Date.class),
                summary.getPartyBudget(),
                summary.getPartyDestination(),
                summary.getPartyGuest(),
                selectedPartyType,
                gson.fromJson(summary.getSelectedCaterers(), Caterer.class),
                gson.fromJson(summary.getSelectedDestination(), Venue.class),
                summary.getExtraNote(),
                guestList,
                checkedItemList,
                selectedLocation
        );
    }


    public static PartyDetails convertPartyFromHistorySummary(HistorySummary summary) {
        Type type1 = new TypeToken<ArrayList<String>>() {
        }.getType();
        Type type2 = new TypeToken<ArrayList<CheckedItem>>() {
        }.getType();

        ArrayList<String> selectedPartyType = gson.fromJson(summary.getPartyType(), type1);
        ArrayList<CheckedItem> guestList = gson.fromJson(summary.getGuestNameList(), type2);
        ArrayList<CheckedItem> checkedItemList = gson.fromJson(summary.getCheckedItemList(), type2);
        ArrayList<String> selectedLocation = gson.fromJson(summary.getLocations(), type1);

        return new PartyDetails(
                summary.getId(),
                gson.fromJson(summary.getPartyDate(), Date.class),
                summary.getPartyBudget(),
                summary.getPartyDestination(),
                summary.getPartyGuest(),
                selectedPartyType,
                gson.fromJson(summary.getSelectedCaterers(), Caterer.class),
                gson.fromJson(summary.getSelectedDestination(), Venue.class),
                summary.getExtraNote(),
                guestList,
                checkedItemList,
                selectedLocation
        );
    }

    public static void addEmptyGuest(int totalGuest) {
        if (GUEST_LIST_NAMES.size() < totalGuest) {
            int x = 0;

            for (int var2 = totalGuest - GUEST_LIST_NAMES.size(); x < var2; ++x) {
                GUEST_LIST_NAMES.add(new CheckedItem("", false));
            }
        }

    }

    public static int getRandom() {
        return RangesKt.random(new IntRange(0, 10000), Random.Default);
    }
}