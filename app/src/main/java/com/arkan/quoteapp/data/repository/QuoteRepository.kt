package com.arkan.quoteapp.data.repository

import com.arkan.quoteapp.data.datasource.QuoteDataSource
import com.arkan.quoteapp.data.mapper.toQuotes
import com.arkan.quoteapp.data.model.Quote
import com.arkan.quoteapp.utils.ResultWrapper
import com.arkan.quoteapp.utils.proceedFlow
import kotlinx.coroutines.flow.Flow

interface QuoteRepository {
    fun getRandomQuotes() : Flow<ResultWrapper<List<Quote>>>
}

class QuoteRepositoryImpl(private val dataSource: QuoteDataSource) : QuoteRepository {
    override fun getRandomQuotes(): Flow<ResultWrapper<List<Quote>>> {
        return proceedFlow { dataSource.getRandomQuotes().toQuotes() }
    }
}