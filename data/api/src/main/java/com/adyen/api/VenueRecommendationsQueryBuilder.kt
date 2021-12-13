package com.adyen.api

class VenueRecommendationsQueryBuilder : PlacesQueryBuilder() {
    private var latitudeLongitude: String? = null
    private var query : String? = null
    private var fields : List<String>? = null

    fun setLatitudeLongitude(
        latitude: Double,
        longitude: Double
    ): VenueRecommendationsQueryBuilder {
        this.latitudeLongitude = "$latitude,$longitude"
        return this
    }

    fun setQuery(query: String): VenueRecommendationsQueryBuilder {
        this.query = query
        return this
    }

    fun setFields(listOf: List<String>): VenueRecommendationsQueryBuilder {
        this.fields = listOf
        return this
    }

    override fun putQueryParams(queryParams: MutableMap<String, String>) {
        latitudeLongitude?.apply { queryParams["ll"] = this }
        query?.apply { queryParams["query"] = this }
    }
}
