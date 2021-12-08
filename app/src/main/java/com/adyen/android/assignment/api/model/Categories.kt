package com.adyen.android.assignment.api.model

import com.google.gson.annotations.SerializedName

data class Categories (

	@SerializedName("id") val id : Int,
	@SerializedName("name") val name : String,
	@SerializedName("icon") val icon : Icon
)