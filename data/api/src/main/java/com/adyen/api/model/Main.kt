package com.adyen.api.model

import com.google.gson.annotations.SerializedName

data class Main (

	@SerializedName("latitude") val latitude : Double,
	@SerializedName("longitude") val longitude : Double
)