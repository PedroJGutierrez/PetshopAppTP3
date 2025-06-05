package com.proyecto.petshopapp.profile

import retrofit2.http.GET

interface QuoteApi {
    @GET("quotes")
    suspend fun getQuotes(): QuoteResponse
}

data class QuoteResponse(
    val quotes: List<Quote>
)