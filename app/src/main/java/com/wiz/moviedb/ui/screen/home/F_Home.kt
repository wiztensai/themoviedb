package com.wiz.moviedb.ui.screen.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.wiz.moviedb.BaseFragment
import com.wiz.moviedb.R
import com.wiz.moviedb.databinding.FHomeBinding
import com.wiz.moviedb.domain.MovieModel
import com.wiz.moviedb.ui.screen.detail_movie.F_DetailMovie
import com.wiz.moviedb.ui.screen.favorite.F_Favorite
import com.wiz.moviedb.util.DpPxConverter
import com.wiz.moviedb.util.EndlessRecyclerViewScrollListener
import com.wiz.moviedb.util.navAddTo
import com.wiz.moviedb.viewmodel.VM_Main

class F_Home : BaseFragment() {

    override fun getTAG(): String {
        return javaClass.simpleName
    }

    lateinit var bind: FHomeBinding
    lateinit var vm: VM_Main
    lateinit var eCHome: EC_Home
    lateinit var scrollListener : EndlessRecyclerViewScrollListener
    lateinit var bottomSheetBehavior: BottomSheetBehavior<LinearLayout>

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        bind = FHomeBinding.bind(LayoutInflater.from(context).inflate(R.layout.f_home, null))
        return bind.root
    }
    
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        vm = ViewModelProviders.of(this, VM_Main.VMF_Main(context!!)).get(VM_Main::class.java)

        vm.getMovielist()

        initBottomSheet()
        onObserve()
        onClick()
        onListener()

        eCHome = EC_Home(
            context!!,
            null, object : EC_Home.MCallback {
                override fun onLayoutClick(
                    position: Int,
                    model: MovieModel,
                    holder: EM_Home.Holder
                ) {
                    navAddTo(F_DetailMovie(model))
                }
            })

        eCHome.isDebugLoggingEnabled = true
        bind.recyclerView.setItemSpacingDp(8)
        bind.recyclerView.setController(eCHome)
    }

    private fun initBottomSheet() {
        bottomSheetBehavior = BottomSheetBehavior.from(bind.bottomMenuCategory)
        bottomSheetBehavior.isHideable = false
        bottomSheetBehavior.peekHeight = DpPxConverter.dpToPixel(44, bind.bottomMenuCategory.context)
        bottomSheetBehavior.isFitToContents = true
    }

    private fun onListener() {
        scrollListener = object : EndlessRecyclerViewScrollListener(bind.recyclerView.layoutManager as LinearLayoutManager) {
            override fun onLoadMore(page: Int, totalItemsCount: Int, view: RecyclerView?) {
//                if (isEndData == false) {
//                    vm.getMovielist(page)
//                }
                vm.getMovielist(page)
            }
        }
        bind.recyclerView.addOnScrollListener(scrollListener)
    }

    private fun onClick() {
        bind.btnFav.setOnClickListener {
            navAddTo(F_Favorite())
        }

        bind.btnVisibilityBottomSheet.setOnClickListener {
            toggleVisibilityBottomSheet()
        }

        bind.btnCatNowPlaying.setOnClickListener {
            if (VM_Main.CAT_NOW_PLAYING == vm.getCategoryMode()) return@setOnClickListener
            toggleVisibilityBottomSheet()
            scrollListener.resetState()
            vm.getMovielist(category = VM_Main.CAT_NOW_PLAYING)
        }

        bind.btnCatPopular.setOnClickListener {
            if (VM_Main.CAT_POPULAR == vm.getCategoryMode()) return@setOnClickListener
            toggleVisibilityBottomSheet()
            scrollListener.resetState()
            vm.getMovielist(category = VM_Main.CAT_POPULAR)
        }

        bind.btnCatTopRated.setOnClickListener {
            if (VM_Main.CAT_TOP_RATED == vm.getCategoryMode()) return@setOnClickListener
            toggleVisibilityBottomSheet()
            scrollListener.resetState()
            vm.getMovielist(category = VM_Main.CAT_TOP_RATED)
        }
    }

    private fun toggleVisibilityBottomSheet() {
        if (bottomSheetBehavior.getState() == BottomSheetBehavior.STATE_COLLAPSED) {
            bind.btnVisibilityBottomSheet.setText("Close Category")
            bottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED)
        } else {
            bind.btnVisibilityBottomSheet.setText("Open Category")
            bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED)
        }
    }

    fun onObserve() {
        vm.dataMovies.observe(viewLifecycleOwner, Observer {
            eCHome.networkState = it.networkState

            eCHome.setData(it.movieList.results)
        })
    }
}