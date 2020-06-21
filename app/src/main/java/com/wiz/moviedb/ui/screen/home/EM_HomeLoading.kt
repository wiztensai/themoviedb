package com.wiz.moviedb.ui.screen.home

import android.view.View
import com.airbnb.epoxy.EpoxyHolder
import com.airbnb.epoxy.EpoxyModelClass
import com.airbnb.epoxy.EpoxyModelWithHolder
import com.wiz.moviedb.R

@EpoxyModelClass(layout = R.layout.item_home_loading)
abstract class EM_HomeLoading : EpoxyModelWithHolder<EM_HomeLoading.LoadingModelViewHolder>() {

  override fun getSpanSize(totalSpanCount: Int, position: Int, itemCount: Int) = totalSpanCount

  class LoadingModelViewHolder : EpoxyHolder() {
    override fun bindView(itemView: View) {
      // nothing to bind, just display the layout
    }
  }
}