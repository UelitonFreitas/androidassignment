package com.adyen.android.assignment

import com.adyen.android.assignment.repository.model.Place


object TestUtil {

    fun createPlace(id: String, name: String) = Place(id, name)

    fun createPlaces(amount: Int) =
        (0 until amount).map { index -> createPlace("ID-$index", "Place-$index") }


    fun createDbPlace(id: String, name: String) = com.adyen.android.assignment.repository.dataBase.model.Place(id, name)

    fun createDbPlaces(amount: Int) =
        (0 until amount).map { index -> createDbPlace("ID-$index", "Place-$index") }

}