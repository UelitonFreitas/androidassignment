package com.adyen.android.assignment.repository.dataBase

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.adyen.android.assignment.repository.dataBase.model.Place
import kotlinx.coroutines.flow.Flow

@Dao
interface PlaceDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertPlaces(breeds: List<Place>)

    @Query("SELECT * FROM place")
    fun getPlaces(): Flow<List<Place>>
}