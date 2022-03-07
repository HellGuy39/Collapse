package com.hellguy39.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.hellguy39.data.models.TrackDatabase

@Database(entities = [TrackDatabase::class], version = 1)
abstract class TracksDatabase: RoomDatabase() {
    abstract fun tracksDao(): TracksDao
}