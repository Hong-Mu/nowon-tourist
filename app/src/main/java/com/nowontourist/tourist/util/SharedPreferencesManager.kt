package com.nowontourist.tourist.util

import android.content.Context
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKeys
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SharedPreferencesManager @Inject constructor(
    @ApplicationContext private val context: Context
) {

    companion object {
        private const val FILENAME = "encrypted_shared_prefs"
        const val KEY_AUTH_SKIPPED = "key_auth_skipped"
    }

    private val alias = MasterKeys.getOrCreate(MasterKeys.AES256_GCM_SPEC)
    private val pref = EncryptedSharedPreferences.create(
        FILENAME,
        alias,
        context,
        EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
        EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM,
    )

    fun putString(key: String, value: String) {
        pref.edit().putString(key, value).apply()
    }

    fun putBoolean(key: String, value: Boolean) {
        pref.edit().putBoolean(key, value).apply()
    }

    fun getString(key: String): String? {
        return pref.getString(key, null)
    }

    fun getBoolean(key: String): Boolean {
        return pref.getBoolean(key, false)
    }

    fun remove(key: String) {
        pref.edit().remove(key).apply()
    }

    fun clear() {
        pref.edit().clear().apply()
    }
}