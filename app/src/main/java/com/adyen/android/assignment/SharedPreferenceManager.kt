package com.adyen.android.assignment

import android.content.Context
import com.adyen.android.assignment.data.SHARED_PREFERENCE_FILE_NAME
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SharedPreferenceManager @Inject constructor(@ApplicationContext context: Context) {

    private var sharedPreference =
        context.getSharedPreferences(SHARED_PREFERENCE_FILE_NAME, Context.MODE_PRIVATE)


    fun saveItem(key: String, value: String?) {
        val editor = sharedPreference.edit()

        editor.putString(key, value)

        editor.apply()
    }

    fun getStringItem(key: String): String {

        val value = sharedPreference.getString(key, "")

        return value ?: ""
    }

}