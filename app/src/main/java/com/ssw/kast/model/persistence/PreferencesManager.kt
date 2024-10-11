package com.ssw.kast.model.persistence

import android.content.Context
import android.content.SharedPreferences

class PreferencesManager(val context: Context) {
    private val sharedPreferences: SharedPreferences =
        context.getSharedPreferences("KastPrefs", Context.MODE_PRIVATE)

    fun saveStringData(key: String, value: String) {
        val editor = sharedPreferences.edit()
        editor.putString(key, value)
        editor.apply()
    }

    fun saveBooleanData(key: String, value: Boolean) {
        val editor = sharedPreferences.edit()
        editor.putBoolean(key, value)
        editor.apply()
    }

    fun getStringData(key: String, defaultValue: String): String {
        return sharedPreferences.getString(key, defaultValue) ?: defaultValue
    }

    fun getBooleanData(key: String, defaultValue: Boolean): Boolean {
        return sharedPreferences.getBoolean(key, defaultValue)
    }
}