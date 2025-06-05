package com.proyecto.petshopapp.profile

import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import retrofit2.converter.gson.GsonConverterFactory
import com.proyecto.petshopapp.profile.Quote
import kotlinx.coroutines.launch
import retrofit2.Retrofit
import androidx.compose.runtime.State

class FAQViewModel : ViewModel() {

    private val _quotes = mutableStateOf<List<Quote>>(emptyList())
    val quotes: State<List<Quote>> = _quotes

    private val retrofit = Retrofit.Builder()
        .baseUrl("https://dummyjson.com/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    private val quoteApi = retrofit.create(QuoteApi::class.java)

    init {
        viewModelScope.launch {
            try {
                _quotes.value = quoteApi.getQuotes().quotes.take(5) // Solo 5 FAQ
            } catch (e: Exception) {
                Log.e("FAQ", "Error loading quotes", e)
            }
        }
    }
}