package com.wiz.moviedb.repository

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.wiz.moviedb.R
import com.wiz.moviedb.domain.MovielistModel
import com.wiz.moviedb.domain.ReviewlistModel
import com.wiz.moviedb.network.MovieService
import com.wiz.moviedb.util.ApiUtil
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.async
import kotlinx.coroutines.delay
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import timber.log.Timber

class R_Movie(private var context: Context) {

    private var apiKey = ""

    init {
        apiKey = context.resources.getString(R.string.key_moviedb)
    }

    suspend fun getPopular(page: Int):MovielistModel {
        val apiService = ApiUtil.retrofit.create(MovieService::class.java)
        return apiService.getPopularMovies(page, apiKey)
    }

    suspend fun getTopRated(page: Int):MovielistModel {
        val apiService = ApiUtil.retrofit.create(MovieService::class.java)
        return apiService.getTopRatedMovies(page, apiKey)
    }

    suspend fun getNowPlaying(page: Int):MovielistModel {
        val apiService = ApiUtil.retrofit.create(MovieService::class.java)
        return apiService.getNowPlayingMovies(page, apiKey)
    }

    suspend fun getReviews(idMovie: Int, page: Int):ReviewlistModel {
        val apiService = ApiUtil.retrofit.create(MovieService::class.java)
        return apiService.getReviews(idMovie, page, apiKey)
    }
}