package com.hellguy39.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.hellguy39.data.dao.FavouritesDao
import com.hellguy39.data.models.TrackEntity

@Database(
    entities = [TrackEntity::class],
    version = 1,
    exportSchema = false
)
abstract class FavouritesDatabase : RoomDatabase() {
    abstract fun dao(): FavouritesDao
}