package com.adyen.api.model

import com.google.gson.annotations.SerializedName

data class Context (

	@SerializedName("geo_bounds") val geo_bounds : GeoBounds
)