package com.wiz.moviedb.util

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.wiz.moviedb.BaseActivity
import com.wiz.moviedb.BaseFragment
import com.wiz.moviedb.R
import com.wiz.moviedb.ui.A_Main

// REPLACE
fun A_Main.navReplaceTo(fragment: Fragment, tag:String = "") {
    // karena transaction replace ini pakai addToBackStack dan ini sering dipakai,
    // pakai addToBackStack agar mentrigger onBackStackListener! saat sudah mentriggernya,
    // ini enak sekali untuk memisahkan class navigation dari class lainnya (clean code)
    // oki, setiap replace, maka baiknya clear backStack
    clearBackStack()

    supportFragmentManager.beginTransaction().replace(R.id.navFragment, fragment, tag).addToBackStack(tag).commit()
}

fun BaseFragment.navReplaceTo(fragment: Fragment, tag:String = "") {
    (activity as A_Main).navReplaceTo(fragment, tag)
}

// ADD
fun A_Main.navAddTo(fragment: Fragment, tag:String = "") {
    supportFragmentManager.beginTransaction()
        .setCustomAnimations(
            R.anim.default_enter_anim,
            R.anim.default_exit_anim,
            R.anim.default_pop_enter,
            R.anim.default_pop_exit)
        .add(R.id.navFragment, fragment, tag).addToBackStack(tag).commit()
}

fun BaseFragment.navAddTo(fragment: Fragment, tag:String = "") {
    (activity as A_Main).navAddTo(fragment, tag)
}

// POPBACKSTACK
fun BaseFragment.popBackStack(){
    (activity as A_Main).onBackPressed()
}

/**
 * CLEAR BACKSTACK
 * Remove all entries from the backStack of this fragmentManager
 */
fun BaseActivity.clearBackStack() {
    if (supportFragmentManager.backStackEntryCount > 0) {
        val entry = supportFragmentManager.getBackStackEntryAt(0)
        supportFragmentManager.popBackStack(entry.id, FragmentManager.POP_BACK_STACK_INCLUSIVE)
    }
}

fun BaseFragment.clearBackStack() {
    (activity as A_Main).clearBackStack()
}