package com.strish.android.testproject.api

import com.strish.android.testproject.JSONResponse
import io.reactivex.Observable
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

interface NewsApiService {


    @GET("everything?domains=nytimes.com&apiKey=$API_KEY")
    fun newArticles(@Query("page") page: Int,
                    @Query("sortBy") sort: String,
                    @Query("from") from: String,
                    @Query("to") to: String): Observable<JSONResponse>

    companion object Factory {
        const val API_KEY = "bdfa8be22c44404989076c9026ab69fa"
        fun create(): NewsApiService {
            val retrofit = Retrofit.Builder()
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .addConverterFactory(GsonConverterFactory.create())
                    .baseUrl("https://newsapi.org/v2/")
                    .build()

            return retrofit.create(NewsApiService::class.java)
        }
    }
}
