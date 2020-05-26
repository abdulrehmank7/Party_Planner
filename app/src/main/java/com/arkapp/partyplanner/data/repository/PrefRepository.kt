package com.arkapp.partyplanner.data.repository

import android.content.Context
import android.content.SharedPreferences
import com.arkapp.partyplanner.data.models.PartyDetails
import com.arkapp.partyplanner.data.models.UserLogin
import com.arkapp.partyplanner.data.preferences.PREFERENCE_NAME
import com.arkapp.partyplanner.data.preferences.PREF_LOGGED_IN
import com.arkapp.partyplanner.data.preferences.PREF_PARTY_DETAILS
import com.arkapp.partyplanner.data.preferences.PREF_USER_LOGIN
import com.google.gson.Gson


class PrefRepository(val context: Context) {

    private val pref: SharedPreferences = context.getSharedPreferences(
        PREFERENCE_NAME,
        Context.MODE_PRIVATE
    )
    private val editor = pref.edit()
    private val gson = Gson()

    private fun String.put(long: Long) {
        editor.putLong(this, long)
        editor.commit()
    }

    private fun String.put(int: Int) {
        editor.putInt(this, int)
        editor.commit()
    }

    private fun String.put(string: String) {
        editor.putString(this, string)
        editor.commit()
    }

    private fun String.put(boolean: Boolean) {
        editor.putBoolean(this, boolean)
        editor.commit()
    }


    private fun String.getLong(): Long {
        return pref.getLong(this, 0)
    }

    private fun String.getInt(): Int {
        return pref.getInt(this, 0)
    }

    private fun String.getString(): String {
        return pref.getString(this, "")!!
    }

    private fun String.getBoolean(): Boolean {
        return pref.getBoolean(this, false)
    }

    /*********************************************/

    fun clearData() {
        editor.clear()
        editor.commit()
    }

    fun setLoggedIn(isLoggedIn: Boolean) {
        PREF_LOGGED_IN.put(isLoggedIn)
    }

    fun setLoggedIn() = PREF_LOGGED_IN.getBoolean()

    fun setCurrentPartyDetails(partyDetails: PartyDetails) {
        PREF_PARTY_DETAILS.put(gson.toJson(partyDetails))
    }

    fun getCurrentPartyDetails(): PartyDetails {
        PREF_PARTY_DETAILS.getString().also {
            return if (it.isEmpty())
                PartyDetails(null,
                             null,
                             null,
                             null,
                             ArrayList(),
                             null,
                             null,
                             null,
                             null,
                             null,
                             ArrayList())
            else
                gson.fromJson(it, PartyDetails::class.java)
        }
    }

    fun setCurrentUser(userLogin: UserLogin) {
        PREF_USER_LOGIN.put(gson.toJson(userLogin))
    }

    fun getCurrentUser(): UserLogin? {
        PREF_USER_LOGIN.getString().also {
            return if (it.isEmpty())
                null
            else
                gson.fromJson(it, UserLogin::class.java)
        }
    }

}