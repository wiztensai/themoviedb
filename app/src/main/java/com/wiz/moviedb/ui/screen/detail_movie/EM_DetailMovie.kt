package com.wiz.moviedb.ui.screen.detail_movie

import android.content.Context
import android.view.View
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import com.airbnb.epoxy.EpoxyAttribute
import com.airbnb.epoxy.EpoxyModelClass
import com.airbnb.epoxy.EpoxyModelWithHolder
import com.airbnb.epoxy.kotlinsample.helpers.KotlinEpoxyHolder
import com.bumptech.glide.Glide
import com.wiz.moviedb.R
import com.wiz.moviedb.domain.MovieModel
import com.wiz.moviedb.util.CST

@EpoxyModelClass(layout = R.layout.item_detail_movie)
abstract class EM_DetailMovie(var context: Context) : EpoxyModelWithHolder<EM_DetailMovie.Holder>() {

    @EpoxyAttribute
    lateinit var movieModel: MovieModel

    override fun bind(holder: Holder) {
        super.bind(holder)

        Glide.with(context).load(CST.BASE_URL_IMG + movieModel.poster_path).into(holder.ivPoster)
        holder.tvTitle.setText(movieModel.title)
        holder.tvReleaseDate.setText(movieModel.release_date)
        holder.tvSynopsis.setText(movieModel.overview)
    }

    inner class Holder : KotlinEpoxyHolder() {
        val ivPoster by bind<ImageView>(R.id.ivPoster)
        val tvTitle by bind<TextView>(R.id.tvTitle)
        val tvReleaseDate by bind<TextView>(R.id.tvReleaseDate)
        val tvSynopsis by bind<TextView>(R.id.tvSynopsis)
        val layoutContainer by bind<View>(R.id.layoutContainer)
        val btnFav by bind<ImageButton>(R.id.btnFav)
    }
}
