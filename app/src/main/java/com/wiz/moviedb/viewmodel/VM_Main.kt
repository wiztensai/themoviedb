package com.wiz.moviedb.viewmodel

import android.content.Context
import androidx.lifecycle.*
import com.wiz.moviedb.domain.MovielistModel
import com.wiz.moviedb.repository.R_Movie
import com.wiz.moviedb.util.NetworkState
import kotlinx.coroutines.launch
import timber.log.Timber
import wazma.punjabi.helper.MPrefs

data class DataMovies(var movieList : MovielistModel, var networkState: NetworkState)

class VM_Main(private val context: Context): ViewModel() {

    private var repo:R_Movie
    private var prefs: MPrefs
    var dataMovies = MutableLiveData<DataMovies>()

    init {
        repo = R_Movie(context)
        prefs = MPrefs(context)
    }

    companion object {
        const val CATEGORY_MODE = "CATEGORY_MODE"
        const val CAT_POPULAR = 1
        const val CAT_NOW_PLAYING = 2
        const val CAT_TOP_RATED = 3
    }

    fun getMovielist(page:Int = 1, category:Int = getCategoryMode()) {
        if (category != getCategoryMode()) resetMovielist() // happen when user switch category
        if (page == 1) prefs.setData(CATEGORY_MODE, category)

        viewModelScope.launch {
            val temp = dataMovies.value?.movieList?:let {
                MovielistModel()
            }

            dataMovies.value = DataMovies(temp, NetworkState.LOADING)

            try {
                var res:MovielistModel

                when(category) {
                    CAT_POPULAR -> res = repo.getPopular(page)
                    CAT_NOW_PLAYING -> res = repo.getNowPlaying(page)
                    CAT_TOP_RATED -> res = repo.getTopRated(page)
                    else -> res = repo.getPopular(page)
                }

                // res data manipulation and keep others res value
                val resResult = res.results

                // reset res result for latest value livedata
                res.results = temp.results

                // append res result to current value livedata
                res.results.addAll(resResult)

                dataMovies.value = DataMovies(res, NetworkState.LOADED)
            } catch (t: Throwable) {
                Timber.e(t, "ERROR")
                dataMovies.value = DataMovies(temp, NetworkState.FAILED)
            }
        }
    }

    fun resetMovielist() {
        val empty = MovielistModel()
        dataMovies.value = DataMovies(empty, NetworkState.LOADED)
    }

    fun getCategoryMode():Int {
        return prefs.getPrefs().getInt(CATEGORY_MODE, CAT_POPULAR)
    }

    class VMF_Main(private val context: Context): ViewModelProvider.NewInstanceFactory() {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T = VM_Main(context) as T
    }
}