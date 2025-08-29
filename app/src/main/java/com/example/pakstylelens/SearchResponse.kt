package com.example.pakstylelens

// SearchResponse.kt
import java.io.Serializable

data class SearchResponse(
    val results: List<SearchResult>
) : Serializable
