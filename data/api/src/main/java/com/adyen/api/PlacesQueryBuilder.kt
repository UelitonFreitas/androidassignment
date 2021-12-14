package com.adyen.api

import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.set

abstract class PlacesQueryBuilder {
    fun build(): Map<String, String> {
        val queryParams = hashMapOf<String, String>()
        queryParams["v"] = dateFormat.format(Date())
        putQueryParams(queryParams)
        return queryParams
    }

    abstract fun putQueryParams(queryParams: MutableMap<String, String>)

    companion object {
        private val dateFormat = SimpleDateFormat("yyyyMMdd", Locale.ROOT)
    }
}
