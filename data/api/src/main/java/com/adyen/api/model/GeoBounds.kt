package com.adyen.api.model

import com.google.gson.annotations.SerializedName

data class GeoBounds (

	@SerializedName("circle") val circle : Circle
)