package com.manjee.basic.data.local.database

import androidx.room.TypeConverter

object AppTypeConverters {
    @TypeConverter
    @JvmStatic
    fun stringToList(value: String?): List<String>? {
        return value?.split(",")?.map { it.trim() }
    }

    @TypeConverter
    @JvmStatic
    fun listToString(list: List<String>?): String? {
        return list?.joinToString(", ")
    }
}
