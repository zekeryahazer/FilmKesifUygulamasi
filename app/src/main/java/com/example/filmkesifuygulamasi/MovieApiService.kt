package com.example.filmkesifuygulamasi

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

interface MovieApiService {
    @GET("movie/now_playing")
    suspend fun getNowPlaying(@Query("api_key") key: String, @Query("language") lang: String = "tr-TR"): MovieResponse

    @GET("movie/popular")
    suspend fun getPopular(@Query("api_key") key: String, @Query("language") lang: String = "tr-TR"): MovieResponse

    @GET("movie/top_rated")
    suspend fun getTopRated(@Query("api_key") key: String, @Query("language") lang: String = "tr-TR"): MovieResponse

    @GET("movie/upcoming")
    suspend fun getUpcoming(@Query("api_key") key: String, @Query("language") lang: String = "tr-TR"): MovieResponse

    @GET("search/movie")
    suspend fun searchMovies(
        @Query("api_key") key: String,
        @Query("query") query: String,
        @Query("language") lang: String = "tr-TR"
    ): MovieResponse
}

object RetrofitClient {
    private const val BASE_URL = "https://api.themoviedb.org/3/"

    val instance: MovieApiService by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(MovieApiService::class.java)
    }
}