package com.adyen.android.assignment.api.model

import com.google.gson.annotations.SerializedName

data class Children (
    @SerializedName("fsq_id") val fsq_id : String,
    @SerializedName("name") val name : String
)
