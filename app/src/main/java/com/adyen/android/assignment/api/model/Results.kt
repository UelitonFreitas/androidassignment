package com.adyen.android.assignment.api.model

import com.google.gson.annotations.SerializedName

data class Results(

    @SerializedName("fsq_id") val fsq_id: String,
    @SerializedName("categories") val categories: List<Categories>,
    @SerializedName("chains") val chains: List<String>,
    @SerializedName("distance") val distance: Int,
    @SerializedName("geocodes") val geocodes: Geocodes,
    @SerializedName("location") val location: Location,
    @SerializedName("name") val name: String,
    @SerializedName("related_places") val related_places: RelatedPlaces,
    @SerializedName("timezone") val timezone: String
)