package com.hellguy39.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

//@Dao
//interface SavedServiceStateDao {
//
//    @Query("SELECT * FROM SavedServiceStateDb")
//    suspend fun getSavedState() : List<SavedServiceStateDb>
//
//    @Insert(onConflict = OnConflictStrategy.REPLACE)
//    fun insertSavedState(savedServiceStateDb: SavedServiceStateDb)
//
//}