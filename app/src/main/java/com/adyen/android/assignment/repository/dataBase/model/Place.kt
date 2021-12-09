package com.adyen.android.assignment.repository.dataBase.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Place(

    @PrimaryKey
    val name: String
)
