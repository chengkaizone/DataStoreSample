package com.tony.datastoresample

import android.content.Context
import android.util.Log
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import java.io.IOException

enum class UiMode {
    LIGHT, DARK
}

class PrefsManager(context: Context) {
    private val Context.dataStore by preferencesDataStore("setting_pref")
    private val dataStore = context.dataStore

    val uiModeFlow: Flow<UiMode> = dataStore.data
            .catch {
                Log.e(TAG, "catch called!")
                if (it is IOException) {
                    it.printStackTrace()
                    emit(emptyPreferences())
                } else {
                    throw it
                }
            }.map { preference ->
                val isDarkMode = preference[IS_DARK_MODE] ?: false
                    Log.e(TAG, "isDarkMode::$isDarkMode")
                when (isDarkMode) {
                    true -> UiMode.DARK
                    false -> UiMode.LIGHT
                }
            }

    suspend fun setUiMode(uiMode: UiMode) {
        Log.e(TAG, "setUiMode:: $uiMode")
        dataStore.edit {
            it[IS_DARK_MODE] = when (uiMode) {
                UiMode.DARK -> false
                UiMode.LIGHT -> true
            }
        }
    }

    companion object {
        const val TAG = "PrefsManager"
        val IS_DARK_MODE = booleanPreferencesKey("dark_mode")
    }
}