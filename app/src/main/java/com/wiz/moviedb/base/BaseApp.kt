package com.wiz.moviedb

import android.app.Application
import android.util.Log
import com.wiz.moviedb.database.RoomDB
import io.github.inflationx.calligraphy3.CalligraphyConfig
import io.github.inflationx.calligraphy3.CalligraphyInterceptor
import io.github.inflationx.viewpump.ViewPump
import timber.log.Timber

class BaseApp: Application() {

    val TAG = javaClass.simpleName

    val roomDB by lazy {
        RoomDB.getAppDatabase(this)
    }

    override fun onCreate() {
        super.onCreate()

        ViewPump.init(
            ViewPump.builder()
                .addInterceptor(
                    CalligraphyInterceptor(
                        CalligraphyConfig.Builder()
                            .setDefaultFontPath("OpenSans-Regular.ttf")
                            .setFontAttrId(R.attr.fontPath)
                            .build()
                    )
                )
                .build()
        )

        if (BuildConfig.BUILD_TYPE.equals("debug", true)){
            Log.d(TAG, "timber ON")
            Timber.plant(Timber.DebugTree())
        } else {
            Log.d(TAG, "timber OFF")
        }
    }
}