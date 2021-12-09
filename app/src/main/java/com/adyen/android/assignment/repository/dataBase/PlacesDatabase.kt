package com.adyen.android.assignment.repository.dataBase;

import androidx.room.Database
import androidx.room.RoomDatabase
import com.adyen.android.assignment.repository.dataBase.model.Place

@Database(
    entities = [
        Place::class
    ],
    version = 1,
    exportSchema = false
)
abstract class PlacesDatabase : RoomDatabase() {
    abstract fun placeDao(): PlaceDao
}
