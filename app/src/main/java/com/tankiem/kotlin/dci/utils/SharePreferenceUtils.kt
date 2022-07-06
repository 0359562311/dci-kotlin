package com.tankiem.kotlin.dci.utils

import android.content.Context
import android.content.SharedPreferences

object SharePreferenceUtils {
    private lateinit var instance: SharedPreferences
    private lateinit var instanceEditor: SharedPreferences.Editor
    fun initialize(context: Context) {
        instance = context.getSharedPreferences("com.example.fakeslink", 0)
        instanceEditor = instance.edit()
    }
    fun addString(key: String, value: String) {
        instanceEditor.putString(key, value)
        instanceEditor.apply()
    }
    fun addInt(key: String, value: Int) {
        instanceEditor.putInt(key, value)
        instanceEditor.apply()
    }
    fun getString(key: String): String? {
        return instance.getString(key, null)
    }
    fun removeKey(key: String) {
        instanceEditor.remove(key)
        instanceEditor.apply()
    }
}