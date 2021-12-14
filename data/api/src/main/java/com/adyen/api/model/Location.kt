package com.adyen.api.model

import com.google.gson.annotations.SerializedName

data class Location (

	@SerializedName("address") val address : String,
	@SerializedName("country") val country : String,
	@SerializedName("cross_street") val cross_street : String,
	@SerializedName("locality") val locality : String,
	@SerializedName("neighborhood") val neighborhood : List<String>,
	@SerializedName("postcode") val postcode : String,
	@SerializedName("region") val region : String
)