package com.wiz.moviedb.ui

import android.os.Build
import android.os.Bundle
import android.widget.Toast
import com.wiz.moviedb.BaseActivity
import com.wiz.moviedb.R
import com.wiz.moviedb.ui.screen.home.F_Home
import com.wiz.moviedb.util.navReplaceTo

class A_Main : BaseActivity() {
    override fun getTag(): String {
        return javaClass.simpleName
    }

    private var backpressedTime: Long = 0
    private val PERIOD: Long = 2000

    val toastExit by lazy {
        Toast.makeText(this, "Press once again to exit", Toast.LENGTH_SHORT)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        navReplaceTo(F_Home())
    }

    override fun onBackPressed() {
        // minimal backStackEntryCount == 1. karena transaction Replace pun di-addToBackstack
        if (supportFragmentManager.backStackEntryCount <= 1) {
            if (backpressedTime + PERIOD > System.currentTimeMillis()) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    // ini lebih bagus karena setelah diclose, app-nya tidak dihide
                    // kalo finishAffinity, setelah diclose, appnya masih ada dibackground/ cuma dihide aja
                    finishAndRemoveTask()
                } else {
                    finishAffinity()
                }
            }
            else toastExit.show()

            backpressedTime = System.currentTimeMillis()
        } else {
            super.onBackPressed()
        }
    }
}