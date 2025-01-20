package com.kaelesty.madprojects.data.local

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.core.Preferences
import kotlinx.atomicfu.locks.SynchronizedObject
import kotlinx.atomicfu.locks.synchronized
import okio.Path.Companion.toPath


object AppPreferences {
    private lateinit var dataStore: DataStore<Preferences>
    private val lock = SynchronizedObject()

    const val DATASTORE_FILE = "preferences.preferences_pb"

    fun getDataStore(producePath: () -> String): DataStore<Preferences> {
        return synchronized(lock) {
            if (::dataStore.isInitialized) {
                dataStore
            } else {
                PreferenceDataStoreFactory.createWithPath(
                    produceFile = { producePath().toPath() }
                ).also { dataStore = it }
            }
        }
    }
}

expect fun createDataStore(context: Any? = null): DataStore<Preferences>