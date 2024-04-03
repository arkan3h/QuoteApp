package com.arkan.quoteapp.data.mapper

import com.arkan.quoteapp.data.model.Quote
import com.arkan.quoteapp.data.source.network.model.QuoteResponse

fun QuoteResponse.toQuote() = Quote(
    id = this.id.orEmpty(),
    quote = this.quote.orEmpty(),
    anime = this.anime.orEmpty(),
    character = this.character.orEmpty(),
)

fun Collection<QuoteResponse>.toQuotes() = this.map {
    it.toQuote()
}