package com.kikyoung.movie.data

import android.content.Context
import androidx.core.content.edit
import com.squareup.moshi.Moshi
import java.lang.reflect.Type

/**
 * TODO Use Room or Realm instead of SharedPreferences
 */
class LocalStorage(context: Context, private val moshi: Moshi) {

    companion object {
        private const val NAME = "localStorage"
    }

    private val sharedPreferences = context.getSharedPreferences(NAME, Context.MODE_PRIVATE)

    fun <T> put(key: String, type: Type, value: T?) {
        sharedPreferences.edit {
            putString(key, moshi.adapter<T>(type).toJson(value))
        }
    }

    fun <T> get(key: String, type: Type, defaultValue: T? = null): T? {
        return try {
            sharedPreferences.getString(key, moshi.adapter<T>(type).toJson(defaultValue))?.let {
                moshi.adapter<T>(type).fromJson(it)
            }
        } catch (e: Exception) {
            null
        }
    }
}