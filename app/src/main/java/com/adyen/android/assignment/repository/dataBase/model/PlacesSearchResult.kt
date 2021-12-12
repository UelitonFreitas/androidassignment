package com.adyen.android.assignment.repository.dataBase.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.adyen.android.assignment.repository.dataBase.PlacesTypeConverters


@Entity
@TypeConverters(PlacesTypeConverters::class)
class PlacesSearchResult(

    @PrimaryKey
    val query: String,

    val placesIds: List<String>
)