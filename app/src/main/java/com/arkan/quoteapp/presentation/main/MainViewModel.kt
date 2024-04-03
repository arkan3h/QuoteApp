package com.arkan.quoteapp.presentation.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.arkan.quoteapp.data.repository.QuoteRepository
import kotlinx.coroutines.Dispatchers

class MainViewModel(
    private val repository: QuoteRepository
) : ViewModel() {
    fun getQuotes() = repository.getRandomQuotes().asLiveData(Dispatchers.IO)
}