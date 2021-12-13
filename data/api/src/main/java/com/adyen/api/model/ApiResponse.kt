package com.adyen.api.model

data class ApiResponse(

    val results: List<Results>,
    val context: Context
)