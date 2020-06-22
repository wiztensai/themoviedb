package com.wiz.moviedb.ui.screen.more_review

import android.content.Context
import com.airbnb.epoxy.TypedEpoxyController
import com.wiz.moviedb.domain.MovieModel
import com.wiz.moviedb.ui.screen.detail_movie.EM_ReviewLoading_
import com.wiz.moviedb.ui.screen.home.EM_Home
import com.wiz.moviedb.ui.screen.home.EM_Home_
import com.wiz.moviedb.util.NetworkState

class EC_MoreReview(var context: Context, var networkState: NetworkState?, var listener: MCallback): TypedEpoxyController<MutableList<MovieModel>>() {

    private fun hasExtraRow() = networkState != null && networkState != NetworkState.LOADED && networkState != NetworkState.FAILED

    override fun buildModels(data: MutableList<MovieModel>?) {
        data?:return

        // CONTENT
        data.forEach {
            EM_Home_(context)
                .movieModel(it)
                .id("morereview"+it.id).onBind { model, view, position ->
                    view.layoutContainer.setOnClickListener {
                        listener.onLayoutClick(position, model.movieModel, view)
                    }
                }
                .addTo(this)
        }

        // MORE CONTENT LOADING
        EM_ReviewLoading_()
            .id("MCL")
            .addIf(hasExtraRow() && data.isNotEmpty(), this)
    }

    interface MCallback {
        fun onLayoutClick(position:Int, model:MovieModel, holder: EM_Home.Holder)
    }
}