package com.adyen.api.model

import com.google.gson.annotations.SerializedName

data class Geocodes(

    @SerializedName("main") val main: Main
)