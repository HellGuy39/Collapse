package com.hellguy39.data.type_converters

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.hellguy39.domain.models.Track

class PlaylistTypeConverter {
    @TypeConverter
    fun listToJson(value: List<Track>) = Gson().toJson(value)

    @TypeConverter
    fun jsonToList(value: String) = Gson().fromJson(value, Array<Track>::class.java).toList()
}