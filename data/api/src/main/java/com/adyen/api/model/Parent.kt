package com.adyen.api.model

import com.google.gson.annotations.SerializedName

data class Parent (

	@SerializedName("fsq_id") val fsq_id : String,
	@SerializedName("name") val name : String
)