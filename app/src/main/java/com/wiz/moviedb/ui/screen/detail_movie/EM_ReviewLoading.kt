package com.wiz.moviedb.ui.screen.detail_movie

import android.view.View
import com.airbnb.epoxy.EpoxyHolder
import com.airbnb.epoxy.EpoxyModelClass
import com.airbnb.epoxy.EpoxyModelWithHolder
import com.wiz.moviedb.R

@EpoxyModelClass(layout = R.layout.item_review_loading)
abstract class EM_ReviewLoading : EpoxyModelWithHolder<EM_ReviewLoading.LoadingModelViewHolder>() {

  override fun getSpanSize(totalSpanCount: Int, position: Int, itemCount: Int) = totalSpanCount

  class LoadingModelViewHolder : EpoxyHolder() {
    override fun bindView(itemView: View) {
      // nothing to bind, just display the layout
    }
  }
}