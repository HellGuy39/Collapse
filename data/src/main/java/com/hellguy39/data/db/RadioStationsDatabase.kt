package com.hellguy39.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.hellguy39.data.dao.RadioStationsDao
import com.hellguy39.data.models.RadioStationEntity

@Database(
    entities = [RadioStationEntity::class],
    exportSchema = false,
    version = 1
)
abstract class RadioStationsDatabase : RoomDatabase() {
    abstract fun dao(): RadioStationsDao
}