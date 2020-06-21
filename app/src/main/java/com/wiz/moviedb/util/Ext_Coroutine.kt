package com.wiz.moviedb.util

import android.util.Log
import com.wiz.moviedb.BaseFragment
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope

fun BaseFragment.getErrorHandler ():CoroutineExceptionHandler {
    val TAG = "CoroutineException"

    val handler = CoroutineExceptionHandler({ coroutineContext, throwable ->
        Log.println(Log.ERROR, TAG, Log.getStackTraceString(throwable))
        throw throwable
    })

    return handler
}