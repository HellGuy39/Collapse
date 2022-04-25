package com.hellguy39.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.hellguy39.data.dao.PlaylistsDao
import com.hellguy39.data.models.PlaylistDb
import com.hellguy39.data.type_converters.PlaylistTypeConverter

@Database(
    entities = [PlaylistDb::class],
    version = 1,
    exportSchema = false
)
@TypeConverters(PlaylistTypeConverter::class)
abstract class PlaylistsDatabase : RoomDatabase() {
    abstract fun dao(): PlaylistsDao
}