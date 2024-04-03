package com.arkan.quoteapp.data.source.network.services

import com.arkan.quoteapp.BuildConfig
import com.arkan.quoteapp.data.source.network.model.QuoteResponse
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import java.util.concurrent.TimeUnit

interface QuoteApiServices {
    @GET("quotes")
    suspend fun getRandomQuote() : List<QuoteResponse>

    companion object {
        @JvmStatic
        operator fun invoke() : QuoteApiServices {
            val okHttpClient = OkHttpClient.Builder()
                .connectTimeout(120, TimeUnit.SECONDS)
                .readTimeout(120, TimeUnit.SECONDS)
                .build()
            val retrofit = Retrofit.Builder()
                .baseUrl(BuildConfig.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(okHttpClient)
                .build()
            return retrofit.create(QuoteApiServices::class.java)
        }
    }
}