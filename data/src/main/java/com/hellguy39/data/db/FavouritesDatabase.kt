package com.hellguy39.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.hellguy39.data.dao.FavouritesDao
import com.hellguy39.data.models.TrackDb

@Database(
    entities = [TrackDb::class],
    version = 1,
    exportSchema = false
)
abstract class FavouritesDatabase : RoomDatabase() {
    abstract fun dao(): FavouritesDao
}