package com.wiz.moviedb.ui.screen.home.F_Home

import android.util.Log
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import androidx.test.core.app.ApplicationProvider.getApplicationContext
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.IdlingRegistry
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.rule.ActivityTestRule
import com.linkedin.android.testbutler.TestButler
import com.schibsted.spain.barista.assertion.BaristaAssertions
import com.schibsted.spain.barista.assertion.BaristaAssertions.assertThatBackButtonClosesTheApp
import com.schibsted.spain.barista.assertion.BaristaListAssertions.assertListNotEmpty
import com.schibsted.spain.barista.assertion.BaristaRecyclerViewAssertions.assertRecyclerViewItemCount
import com.schibsted.spain.barista.assertion.BaristaVisibilityAssertions.assertDisplayed
import com.schibsted.spain.barista.interaction.BaristaClickInteractions
import com.schibsted.spain.barista.interaction.BaristaClickInteractions.clickBack
import com.schibsted.spain.barista.interaction.BaristaClickInteractions.clickOn
import com.schibsted.spain.barista.interaction.BaristaListInteractions.clickListItem
import com.schibsted.spain.barista.interaction.BaristaListInteractions.scrollListToPosition
import com.schibsted.spain.barista.interaction.BaristaSleepInteractions.sleep
import com.schibsted.spain.barista.internal.failurehandler.BaristaException
import com.schibsted.spain.barista.rule.cleardata.ClearDatabaseRule
import com.schibsted.spain.barista.rule.cleardata.ClearFilesRule
import com.schibsted.spain.barista.rule.cleardata.ClearPreferencesRule
import com.wiz.moviedb.R
import com.wiz.moviedb.ui.A_Main
import com.wiz.moviedb.util.EspressoIdlingResource
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith


@RunWith(AndroidJUnit4::class)
class A_MainTest{

    private val TAG = "A_HomeTest"
    private var rvHomeResId = 0

    @get:Rule var activityRule: ActivityTestRule<A_Main> = ActivityTestRule(A_Main::class.java)
    @get:Rule var clearPreferencesRule = ClearPreferencesRule()
    @get:Rule var clearFilesRule = ClearFilesRule() // Delete all files in getFilesDir() and getCacheDir()
    @get:Rule var clearDatabaseRule = ClearDatabaseRule() // Delete all tables from all the app's SQLite Databases

    @Before
    @Throws(Exception::class)
    fun setup() {
        rvHomeResId = R.id.recyclerView
        TestButler.verifyAnimationsDisabled(getApplicationContext())
    }

    /**
     * cek jika di request pertama, mendapatkan result size 20 (result maks 1 page di moviedb)
     * lalu scroll ke item terakhir
     * lalu cek di request kedua, total result 40
     *
     * lalu click category now playing
     * cek apakah endless scrollnya work?
     */
    @Test
    fun test_endless_home_rv_category() {
        IdlingRegistry.getInstance().register(EspressoIdlingResource.idlingresource)

        assertRecyclerViewItemCount(rvHomeResId, 20)
        scrollListToPosition(rvHomeResId, getRVcount(rvHomeResId)-1)
        assertRecyclerViewItemCount(rvHomeResId, 40)

        clickOn(R.id.btn_visibility_bottom_sheet)
        sleep(300) // bugfix: animasi bottom sheet belum bisa dimatikan, jadi pakai sleep
        assertDisplayed(R.id.btnCatTopRated)
        clickOn(R.id.btnCatTopRated)

        assertRecyclerViewItemCount(rvHomeResId, 20)
        scrollListToPosition(rvHomeResId, getRVcount(rvHomeResId)-1)
        assertRecyclerViewItemCount(rvHomeResId, 40)

        IdlingRegistry.getInstance().unregister(EspressoIdlingResource.idlingresource)
    }

    /**
     * cek apakah review di detail movie muncul?
     * jika tidak apakah muncul info bahwa tidak ada review?
     */
    @Test
    fun check_review_in_movie_detail() {
        IdlingRegistry.getInstance().register(EspressoIdlingResource.idlingresource)

        assertListNotEmpty(rvHomeResId)
        clickListItem(rvHomeResId, 0)
        try {
            assertListNotEmpty(R.id.rvReviews) // cek list tidak kosong

            Log.d(TAG, "rv size: ${getRVcount(R.id.rvReviews)}")
        } catch (e: BaristaException) { // jika list kosong, sengaja dicatch karena memunculkan info rv kosong
            assertDisplayed(R.id.tvInfo) // muncul info error

            Log.d(TAG, "rv size: ${getRVcount(R.id.rvReviews)}")
        }

        IdlingRegistry.getInstance().unregister(EspressoIdlingResource.idlingresource)
    }

    /**
     * click list 1 di category popular. simpan variabel tersebut
     * ke movie detail, lalu popbackstack
     * lalu klik btn favorite. click list 1 di fav untuk ke movie detailnya
     * cek apakah judul dan tanggalnya sama dengan variabel yang disimpan?
     */
    @Test
    fun check_favorite() {
        IdlingRegistry.getInstance().register(EspressoIdlingResource.idlingresource)

        clickListItem(rvHomeResId, 0)
        clickOn(R.id.btnFavDetail)
        clickOn(R.id.btnBack) // back ke home

        val recyclerView = activityRule.getActivity().findViewById(rvHomeResId) as RecyclerView
        val movieTitle = recyclerView.layoutManager?.findViewByPosition(0)?.findViewById<TextView>(R.id.tvTitle)?.text?.toString()
        if (movieTitle == null) throw BaristaException("Movie title null", IllegalStateException())
        clickOn(R.id.btnFavHome)

        val rvFavorite = activityRule.getActivity().findViewById(R.id.rvFavorite) as RecyclerView
        val favTitle = rvFavorite.findViewHolderForAdapterPosition(0)?.itemView?.findViewById<TextView>(R.id.tvTitle)
        favTitle?.let {
            assertDisplayed(it.id, movieTitle)
        }

        IdlingRegistry.getInstance().unregister(EspressoIdlingResource.idlingresource)
    }

    @Test
    fun check_exit_app() {
        clickBack()
        assertThatBackButtonClosesTheApp()
    }

    private fun getRVcount(resId:Int): Int {
        val recyclerView = activityRule.getActivity().findViewById(resId) as RecyclerView
        return recyclerView.adapter!!.itemCount
    }
}