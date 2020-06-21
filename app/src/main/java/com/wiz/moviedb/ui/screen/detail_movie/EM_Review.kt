package com.wiz.moviedb.ui.screen.detail_movie

import android.content.Context
import android.view.View
import android.widget.TextView
import com.airbnb.epoxy.EpoxyAttribute
import com.airbnb.epoxy.EpoxyModelClass
import com.airbnb.epoxy.EpoxyModelWithHolder
import com.airbnb.epoxy.kotlinsample.helpers.KotlinEpoxyHolder
import com.wiz.moviedb.R
import com.wiz.moviedb.domain.ReviewModel

@EpoxyModelClass(layout = R.layout.item_review)
abstract class EM_Review(var context: Context) : EpoxyModelWithHolder<EM_Review.Holder>() {

    @EpoxyAttribute
    lateinit var reviewModel: ReviewModel

    override fun bind(holder: Holder) {
        super.bind(holder)

        holder.tvAuthor.setText("Author : "+reviewModel.author)
        holder.tvContent.setText(reviewModel.content)
    }

    inner class Holder : KotlinEpoxyHolder() {
        val tvAuthor by bind<TextView>(R.id.tvAuthor)
        val tvContent by bind<TextView>(R.id.tvContent)
        val layoutContainer by bind<View>(R.id.layoutContainer)
    }
}
