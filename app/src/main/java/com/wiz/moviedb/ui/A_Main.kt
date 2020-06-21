package com.wiz.moviedb.ui

import android.os.Bundle
import com.wiz.moviedb.BaseActivity
import com.wiz.moviedb.R
import com.wiz.moviedb.ui.screen.home.F_Home
import com.wiz.moviedb.util.navReplaceTo

class A_Main : BaseActivity() {
    override fun getTag(): String {
        return javaClass.simpleName
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        navReplaceTo(F_Home())
    }
}