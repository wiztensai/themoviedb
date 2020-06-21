package com.wiz.moviedb.ui.screen.detail_movie

import android.content.Context
import com.airbnb.epoxy.TypedEpoxyController
import com.wiz.moviedb.domain.MovieModel
import com.wiz.moviedb.domain.ReviewModel
import com.wiz.moviedb.util.NetworkState

class EC_DetailMovie(var context: Context, var networkState: NetworkState?, var listener: MCallback): TypedEpoxyController<MutableList<ReviewModel>>() {

    private fun hasExtraRow() = networkState != null && networkState != NetworkState.LOADED && networkState != NetworkState.FAILED

    override fun buildModels(data: MutableList<ReviewModel>?) {
        data?:return

        // REVIEWS
        data.forEach {
            EM_Review_(context)
                .reviewModel(it)
                .id(it.id).onBind { model, view, position ->
                    view.layoutContainer.setOnClickListener {
                        listener.onLayoutClick(position, model.reviewModel, view)
                    }
                }
                .addTo(this)
        }

    }

    interface MCallback {
        fun onLayoutClick(position:Int, model:ReviewModel, holder: EM_Review.Holder)
    }
}