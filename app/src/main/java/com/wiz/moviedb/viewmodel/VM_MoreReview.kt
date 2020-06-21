package com.wiz.moviedb.viewmodel

import android.content.Context
import androidx.lifecycle.*
import com.wiz.moviedb.domain.MovielistModel
import com.wiz.moviedb.domain.ReviewlistModel
import com.wiz.moviedb.repository.R_Movie
import com.wiz.moviedb.util.NetworkState
import kotlinx.coroutines.launch
import timber.log.Timber

class VM_MoreReview(private val context: Context): ViewModel() {

    private var repo:R_Movie

    var dataReviews = MutableLiveData<DataReviews>()

    init {
        repo = R_Movie(context)
    }

    fun getReviews(idMovie:Int, page:Int = 1) {
        viewModelScope.launch {
            // get latest value livedata
            val temp = dataReviews.value?.reviewList?:let {
                ReviewlistModel()
            }

            // trigger view loading di recyclerview
            dataReviews.value = DataReviews(temp, NetworkState.LOADING)

            try {
                val res = repo.getReviews(idMovie, page)

                // res data manipulation and keep others res value
                val resResult = res.results

                // reset res result for latest value livedata
                res.results = temp.results

                // append res result to current value livedata
                res.results.addAll(resResult)

                dataReviews.value = DataReviews(res, NetworkState.LOADED)
            } catch (t: Throwable) {
                Timber.e(t, "ERROR")
                dataReviews.value = DataReviews(temp, NetworkState.FAILED)
            }
        }
    }

    fun resetDataReviews() {
        val empty = ReviewlistModel()
        dataReviews.value = DataReviews(empty, NetworkState.LOADED)
    }

    class VMF_MoreReview(private val context: Context): ViewModelProvider.NewInstanceFactory() {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T = VM_MoreReview(context) as T
    }
}