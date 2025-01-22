package com.kaelesty.madprojects.data.local

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import com.kaelesty.data.remote.auth.AuthorizedResponse
import com.kaelesty.madprojects.domain.repos.auth.AuthRepo
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.serialization.Serializable
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

class PreferencesStorage(
    val dataStore: DataStore<Preferences>
) {

    companion object {
        private val AUTH_KEY = stringPreferencesKey("au_res")
    }

    suspend fun saveAuthResponse(res: AuthorizedResponse) {
        dataStore.edit { preferences ->
            preferences.set(
                key = AUTH_KEY,
                value = Json.encodeToString(res)
            )
        }
    }

    suspend fun dropAuthResponse() {
        dataStore.edit { preferences ->
            preferences.remove(AUTH_KEY)
        }
    }

    fun getAuthResponse(): Flow<AuthorizedResponse?> {
        return dataStore.data
            .map { preferences ->
                preferences[AUTH_KEY]?.let {
                    Json.decodeFromString(it)
                }

            }
    }
}