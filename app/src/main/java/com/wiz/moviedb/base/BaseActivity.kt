package com.wiz.moviedb

import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.os.StrictMode
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.wiz.moviedb.database.RoomDB
import io.github.inflationx.viewpump.ViewPumpContextWrapper
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import kotlin.coroutines.CoroutineContext


abstract class BaseActivity : AppCompatActivity() {

    private var TAG: String = javaClass.simpleName
    abstract fun getTag(): String
    lateinit var roomDB: RoomDB

    var compositeDisposable: CompositeDisposable = CompositeDisposable()

    /*val prefs by lazy {
        MPrefs(this)
    }*/

    override fun onCreate(savedInstanceState: Bundle?) {
        try {
            super.onCreate(savedInstanceState)

            if (Build.VERSION.SDK_INT > 9) {
                val policy =
                    StrictMode.ThreadPolicy.Builder().permitAll().build()
                StrictMode.setThreadPolicy(policy)
            }

            if (!getTag().isNullOrEmpty()) {
                TAG = getTag()
            }

            roomDB = (application as BaseApp).roomDB

            Log.i(TAG, "onCreate()")
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun Disposable.autoDispose() {
        compositeDisposable.add(this)
    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        Log.i(TAG, "onNewIntent()")
    }

    override fun onStart() {
        super.onStart()
        Log.i(TAG, "onStart()")
    }

    override fun onResume() {
        super.onResume()
        Log.i(TAG, "onResume()")
    }

    override fun onPause() {
        super.onPause()
        Log.i(TAG, "onPause()")
    }

    override fun onStop() {
        super.onStop()
        Log.i(TAG, "onStop()")
    }

    override fun onRestart() {
        super.onRestart()
        Log.i(TAG, "onRestart()")
    }


    override fun onDestroy() {
        Log.i(TAG, "onDestroy()")
        compositeDisposable.clear()
        super.onDestroy()
    }

    override fun onBackPressed() {
        Log.i(TAG, "onBackPressed()")
        super.onBackPressed()
    }

    override fun attachBaseContext(newBase: Context) {
        super.attachBaseContext(ViewPumpContextWrapper.wrap(newBase))
    }
}