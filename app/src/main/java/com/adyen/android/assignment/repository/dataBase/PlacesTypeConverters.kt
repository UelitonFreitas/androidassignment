package com.adyen.android.assignment.repository.dataBase

import androidx.room.TypeConverter

object PlacesTypeConverters {

    @TypeConverter
    @JvmStatic
    fun stringToStringList(data: String?): List<String>? {
        return data?.split(",")
    }

    @TypeConverter
    @JvmStatic
    fun stringListToString(data: List<String>?): String? {
        return data?.joinToString(",")
    }
}