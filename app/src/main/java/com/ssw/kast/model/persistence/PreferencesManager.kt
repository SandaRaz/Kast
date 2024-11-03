package com.ssw.kast.model.persistence

import android.content.Context
import android.content.SharedPreferences
import com.google.gson.Gson
import com.ssw.kast.model.component.ThemeModel

class PreferencesManager(val context: Context) {
    private val sharedPreferences: SharedPreferences =
        context.getSharedPreferences("KastPrefs", Context.MODE_PRIVATE)
    private val gson = Gson()

    fun saveBooleanData(key: String, value: Boolean) {
        val editor = sharedPreferences.edit()
        editor.putBoolean(key, value)
        editor.apply()
    }

    fun saveStringData(key: String, value: String) {
        val editor = sharedPreferences.edit()
        editor.putString(key, value)
        editor.apply()
    }

    fun saveThemeData(themeModel: ThemeModel) {
        val jsonTheme = gson.toJson(themeModel)
        val editor = sharedPreferences.edit()
        editor.putString("currentThemeName", themeModel.name)
        editor.putString("currentTheme", jsonTheme)
        editor.apply()
    }

    fun getBooleanData(key: String, defaultValue: Boolean): Boolean {
        return sharedPreferences.getBoolean(key, defaultValue)
    }

    fun getStringData(key: String, defaultValue: String): String {
        return sharedPreferences.getString(key, defaultValue) ?: defaultValue
    }

    fun getThemeData(): ThemeModel? {
        val jsonTheme = sharedPreferences.getString("currentTheme", null) ?: return null
        return gson.fromJson(jsonTheme, ThemeModel::class.java)
    }
}