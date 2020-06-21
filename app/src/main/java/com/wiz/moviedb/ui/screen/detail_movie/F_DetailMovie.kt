package com.wiz.moviedb.ui.screen.detail_movie

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.bumptech.glide.Glide
import com.wiz.moviedb.BaseFragment
import com.wiz.moviedb.R
import com.wiz.moviedb.databinding.FDetailMovieBinding
import com.wiz.moviedb.domain.MovieModel
import com.wiz.moviedb.domain.ReviewModel
import com.wiz.moviedb.ui.screen.more_review.F_MoreReview
import com.wiz.moviedb.util.*
import com.wiz.moviedb.viewmodel.VM_DetailMovie
import kotlinx.coroutines.*

class F_DetailMovie(val movieModel: MovieModel) : BaseFragment() {

    override fun getTAG(): String {
        return javaClass.simpleName
    }

    val coroutineScope = CoroutineScope(
        Dispatchers.Main + SupervisorJob() + getErrorHandler()
    )

    lateinit var bind: FDetailMovieBinding
    lateinit var vm: VM_DetailMovie
    lateinit var eCDetailMovie: EC_DetailMovie

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        bind = FDetailMovieBinding.bind(
            LayoutInflater.from(context).inflate(R.layout.f_detail_movie, null)
        )
        return bind.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        vm = ViewModelProviders.of(this, VM_DetailMovie.VMF_DetailMovie(context!!))
            .get(VM_DetailMovie::class.java)

        vm.getReviews(movieModel.id.toInt())

        Glide.with(bind.ivPoster.context).load(CST.BASE_URL_IMG + movieModel.poster_path)
            .into(bind.ivPoster)
        bind.tvTitle.setText(movieModel.title)
        bind.tvSynopsis.setText(movieModel.overview)
        bind.tvReleaseDate.setText(movieModel.release_date)

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
        checkFav()
    }

    fun checkFav() {
        coroutineScope.launch {
            val isFav = if (roomDB.movieDao().searchById(movieModel.id.toInt()) != null) true else false
            if (isFav) bind.btnFav.background = resources.getDrawable(R.drawable.ic_favorite_24px)
            else bind.btnFav.background = resources.getDrawable(R.drawable.ic_favorite_border_24px)
        }
    }

    private fun onclick() {
        bind.btnFav.setOnClickListener {
            coroutineScope.launch {
                val isFav = if (roomDB.movieDao().searchById(movieModel.id.toInt()) != null) true else false
                if (isFav) {
                    roomDB.movieDao().unFavorite(movieModel.id.toInt())
                    bind.btnFav.background = resources.getDrawable(R.drawable.ic_favorite_border_24px)
                } else {
                    roomDB.movieDao().favorite(movieModel)
                    bind.btnFav.background = resources.getDrawable(R.drawable.ic_favorite_24px)
                }

                sendToggleFavBroadcast()
            }
        }

        bind.btnMoreReviews.setOnClickListener {
            navAddTo(F_MoreReview(movieModel))
        }

        bind.btnBack.setOnClickListener {
            popBackStack()
        }
    }

    private fun onListener() {
    }

    fun onObserve() {
        vm.dataReviews.observe(viewLifecycleOwner, Observer {
            eCDetailMovie.networkState = it.networkState

            eCDetailMovie.setData(it.reviewList.results)

            if (it.reviewList.total_pages > 1) {
                bind.btnMoreReviews.isVisible = true
            }

            if (it.reviewList.page != 0 && it.reviewList.results.isEmpty()) {
                bind.tvInfo.visibility = View.VISIBLE
                bind.rvReviews.visibility = View.INVISIBLE
            } else {
                bind.tvInfo.visibility = View.GONE
                bind.rvReviews.visibility = View.VISIBLE
            }
        })
    }

    fun sendToggleFavBroadcast() {
        val intent = Intent()
        intent.action = getString(R.string.broadcast_toggle_fav)
        activity?.sendBroadcast(intent)
    }
}