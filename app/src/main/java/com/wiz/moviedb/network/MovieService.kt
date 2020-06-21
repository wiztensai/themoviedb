package com.wiz.moviedb.network

import com.wiz.moviedb.domain.MovielistModel
import com.wiz.moviedb.domain.ReviewlistModel
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Path
import retrofit2.http.Query


interface MovieService {
    @GET("popular")
    suspend fun getPopularMovies(
        @Query("page") page : Int,
        @Query("api_key") apiKey : String

    ): MovielistModel

    @GET("top_rated")
    suspend fun getTopRatedMovies(
        @Query("page") page : Int,
        @Query("api_key") apiKey : String

    ): MovielistModel

    @GET("now_playing")
    suspend fun getNowPlayingMovies(
        @Query("page") page : Int,
        @Query("api_key") apiKey : String

    ): MovielistModel

    @GET("{id}/reviews")
    suspend fun getReviews(
        @Path("id") id : Int,
        @Query("page") page : Int,
        @Query("api_key") apiKey : String
    ): ReviewlistModel
}
