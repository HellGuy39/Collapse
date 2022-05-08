package com.hellguy39.data.db

import androidx.room.Database
import androidx.room.RoomDatabase

//@Database(
//
//)
abstract class EqualizerPresetsDatabase: RoomDatabase() {
    abstract fun dao()
}