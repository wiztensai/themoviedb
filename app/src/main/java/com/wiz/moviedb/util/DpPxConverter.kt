package com.wiz.moviedb.util

import android.content.Context
import android.util.DisplayMetrics

/**
 * Created by Wiz Nanang on 21/11/18.
 */
class DpPxConverter {
    companion object {
        fun dpToPixel(dp: Int, context: Context): Int {
            val dpCalculation = context.getResources().getDisplayMetrics().density
            return (dpCalculation * dp).toInt()
        }

        fun dpToPixel(dp: Float, context: Context): Int {
            val dpCalculation = context.getResources().getDisplayMetrics().density
            return (dpCalculation * dp).toInt()
        }
    }
}