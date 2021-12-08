package com.adyen.android.assignment.api.model

import com.google.gson.annotations.SerializedName

data class RelatedPlaces (

    @SerializedName("parent") val parent : Parent,
    @SerializedName("children") val children : List<Children>
)