package com.wiz.moviedb.ui.screen.more_review

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.core.view.isVisible
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.wiz.moviedb.BaseFragment
import com.wiz.moviedb.R
import com.wiz.moviedb.databinding.FDetailMovieBinding
import com.wiz.moviedb.databinding.FHomeBinding
import com.wiz.moviedb.databinding.FMoreReviewBinding
import com.wiz.moviedb.domain.MovieModel
import com.wiz.moviedb.domain.ReviewModel
import com.wiz.moviedb.ui.screen.detail_movie.EC_DetailMovie
import com.wiz.moviedb.ui.screen.detail_movie.EM_Review
import com.wiz.moviedb.ui.screen.favorite.F_Favorite
import com.wiz.moviedb.ui.screen.home.EM_Home
import com.wiz.moviedb.util.*
import com.wiz.moviedb.viewmodel.VM_DetailMovie
import com.wiz.moviedb.viewmodel.VM_Main
import com.wiz.moviedb.viewmodel.VM_MoreReview
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch

class F_MoreReview(val movieModel: MovieModel) : BaseFragment() {
    override fun getTAG(): String {
        return javaClass.simpleName
    }

    val coroutineScope = CoroutineScope(
        Dispatchers.Main + SupervisorJob() + getErrorHandler()
    )

    lateinit var bind: FMoreReviewBinding
    lateinit var vm: VM_MoreReview
    lateinit var eCDetailMovie: EC_DetailMovie
    lateinit var scrollListener : EndlessRecyclerViewScrollListener

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        setRetainInstance(true)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        bind = FMoreReviewBinding.bind(
            LayoutInflater.from(context).inflate(R.layout.f_more_review, null)
        )
        return bind.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        vm = ViewModelProviders.of(this, VM_MoreReview.VMF_MoreReview(context!!))
            .get(VM_MoreReview::class.java)

        vm.getReviews(movieModel.id.toInt())

        eCDetailMovie = EC_DetailMovie(
            context!!, null, object : EC_DetailMovie.MCallback {
                override fun onLayoutClick(position: Int, model: ReviewModel, holder: EM_Review.Holder) {

                }
            })

        eCDetailMovie.isDebugLoggingEnabled = true
        bind.rvReviews.setItemSpacingDp(8)
        bind.rvReviews.setController(eCDetailMovie)

        onObserve()
        onclick()
        onListener()
    }

    private fun onclick() {
        bind.btnBack.setOnClickListener {
            popBackStack()
        }
    }

    private fun onListener() {
        scrollListener = object : EndlessRecyclerViewScrollListener(bind.rvReviews.layoutManager as LinearLayoutManager) {
            override fun onLoadMore(page: Int, totalItemsCount: Int, view: RecyclerView?) {
                vm.getReviews(page = page, idMovie = movieModel.id.toInt())
            }
        }
        bind.rvReviews.addOnScrollListener(scrollListener)
    }

    fun onObserve() {
        bind.shimmerInitMoreReview.isVisible = true

        vm.dataReviews.observe(viewLifecycleOwner, Observer {
            eCDetailMovie.networkState = it.networkState

            if (it.reviewList.results.isEmpty() && it.networkState != NetworkState.FAILED) {
                bind.shimmerInitMoreReview.isVisible = true
            } else {
                bind.shimmerInitMoreReview.isVisible = false
            }

            eCDetailMovie.setData(it.reviewList.results)

            if (it.networkState != NetworkState.LOADING) {
                if (!EspressoIdlingResource.idlingresource.isIdleNow) {
                    EspressoIdlingResource.decrement()
                }
            }
        })
    }
}