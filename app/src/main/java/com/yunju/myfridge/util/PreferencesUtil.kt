package com.yunju.myfridge.util

import android.content.Context

class PreferencesUtil {

    companion object{

        private const val NOTIFY_FRIDGE = "NOTIFY_FRIDGE"

        // 알람 설정
        fun setNotify(context: Context, isNotify: Boolean) {
            val pref = context.getSharedPreferences(NOTIFY_FRIDGE, Context.MODE_PRIVATE)
            pref.edit().putBoolean(NOTIFY_FRIDGE, isNotify).apply()
        }
        // 알람 설정
        fun getNotify(context: Context) : Boolean {
            val pref = context.getSharedPreferences(NOTIFY_FRIDGE, Context.MODE_PRIVATE)
            return pref.getBoolean(NOTIFY_FRIDGE, false)
        }
    }
}