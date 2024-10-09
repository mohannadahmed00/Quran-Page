package com.giraffe.quranpage.local.preferences

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.byteArrayPreferencesKey
import androidx.datastore.preferences.core.doublePreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.floatPreferencesKey
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.longPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map


class DataStorePreferences(private val context: Context) {
    private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(
        name = "quran_page_data_store"
    )

    suspend fun save(key: String, value: Any) {
        when (value) {
            is Int -> context.dataStore.edit { preferences ->
                preferences[intPreferencesKey(key)] = value
            }

            is Double -> context.dataStore.edit { preferences ->
                preferences[doublePreferencesKey(
                    key
                )] = value
            }

            is String -> context.dataStore.edit { preferences ->
                preferences[stringPreferencesKey(
                    key
                )] = value
            }

            is Boolean -> context.dataStore.edit { preferences ->
                preferences[booleanPreferencesKey(
                    key
                )] = value
            }

            is Float -> context.dataStore.edit { preferences ->
                preferences[floatPreferencesKey(key)] = value
            }

            is Long -> context.dataStore.edit { preferences ->
                preferences[longPreferencesKey(key)] = value
            }

            is ByteArray -> context.dataStore.edit { preferences ->
                preferences[byteArrayPreferencesKey(
                    key
                )] = value
            }
        }
    }

    suspend fun readInt(key: String) = context.dataStore.data.map { preferences ->
        preferences[intPreferencesKey(key)] ?: 0
    }.first()

    suspend fun readDouble(key: String) = context.dataStore.data.map { preferences ->
        preferences[doublePreferencesKey(key)] ?: "not found in preferences !!"
    }.first()

    suspend fun readString(key: String) = context.dataStore.data.map { preferences ->
        preferences[stringPreferencesKey(key)]
    }.first()

    suspend fun readBoolean(key: String) = context.dataStore.data.map { preferences ->
        preferences[booleanPreferencesKey(key)] ?: "not found in preferences !!"
    }.first()

    suspend fun readFloat(key: String) = context.dataStore.data.map { preferences ->
        preferences[floatPreferencesKey(key)] ?: "not found in preferences !!"
    }.first()

    suspend fun readLong(key: String) = context.dataStore.data.map { preferences ->
        preferences[longPreferencesKey(key)] ?: "not found in preferences !!"
    }.first()

    suspend fun readByteArray(key: String) = context.dataStore.data.map { preferences ->
        preferences[byteArrayPreferencesKey(key)] ?: "not found in preferences !!"
    }.first()

}