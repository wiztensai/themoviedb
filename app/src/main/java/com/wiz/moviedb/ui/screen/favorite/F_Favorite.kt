package com.wiz.moviedb.ui.screen.favorite

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import com.wiz.moviedb.BaseFragment
import com.wiz.moviedb.R
import com.wiz.moviedb.databinding.FFavoriteBinding
import com.wiz.moviedb.domain.MovieModel
import com.wiz.moviedb.ui.screen.detail_movie.F_DetailMovie
import com.wiz.moviedb.ui.screen.home.EM_Home
import com.wiz.moviedb.util.getErrorHandler
import com.wiz.moviedb.util.navAddTo
import com.wiz.moviedb.util.popBackStack
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch

class F_Favorite : BaseFragment() {

    override fun getTAG(): String {
        return javaClass.simpleName
    }

    lateinit var bind: FFavoriteBinding
    lateinit var eCHome: EC_Favorite
    private var toggleFavBroadcastReceiver: MusicServiceBroadcastReceiver? = null

    val coroutineScope = CoroutineScope(
        Dispatchers.Main + SupervisorJob() + getErrorHandler()
    )

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        bind = FFavoriteBinding.bind(LayoutInflater.from(context).inflate(R.layout.f_favorite, null))
        return bind.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        checkFavData()
        onClick()

        eCHome = EC_Favorite(
            context!!,
            null, object : EC_Favorite.MCallback {
                override fun onLayoutClick(position: Int, model: MovieModel, holder: EM_Home.Holder) {
                    navAddTo(F_DetailMovie(model))
                }
            })

        eCHome.isDebugLoggingEnabled = true
        bind.recyclerView.setItemSpacingDp(8)
        bind.recyclerView.setController(eCHome)
    }

    private fun onClick() {
        bind.btnBack.setOnClickListener {
            popBackStack()
        }
    }

    fun checkFavData() {
        coroutineScope.launch {
            val d = roomDB.movieDao().getFavorites()
            if (d.isEmpty()) {
                bind.tvInfo.visibility = View.VISIBLE
                bind.recyclerView.visibility = View.INVISIBLE
            } else {
                bind.tvInfo.visibility = View.GONE
                bind.recyclerView.visibility = View.VISIBLE

                eCHome.setData(d)
            }
        }
    }

    override fun onResume() {
        super.onResume()
        initMusicServiceBroadcastReceiver()
    }

    override fun onPause() {
        super.onPause()
        toggleFavBroadcastReceiver?.let {
            activity?.unregisterReceiver(toggleFavBroadcastReceiver)
        }
    }

    private fun initMusicServiceBroadcastReceiver() {
        val intentFilter = IntentFilter()
        intentFilter.addAction(getString(R.string.broadcast_toggle_fav))
        toggleFavBroadcastReceiver = MusicServiceBroadcastReceiver()
        activity?.registerReceiver(toggleFavBroadcastReceiver, intentFilter)
    }

    inner class MusicServiceBroadcastReceiver : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            checkFavData()
        }
    }
}