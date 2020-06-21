package com.wiz.moviedb.database

import com.wiz.moviedb.domain.MovieModel
import kotlinx.coroutines.*

object FavUtils {
    fun favorite(db: RoomDB, d: MovieModel) {
        GlobalScope.launch {
            db.movieDao().favorite(d)
        }
    }

    fun unFavorite(db: RoomDB, d: MovieModel) {
        GlobalScope.launch {
            db.movieDao().unFavorite(d.room_id)
        }
    }

    fun getFavs(db: RoomDB, callback: (bookmarks: MutableList<MovieModel>) -> Unit) {
        GlobalScope.launch {
            val stations = db.movieDao().getFavorites()
            withContext(Dispatchers.Main) {
                callback(stations)
            }
        }
    }

    fun isFav(db: RoomDB, roomId:Int, callback: (isExist:Boolean, bookmark: MovieModel?) -> Unit) {
        GlobalScope.launch {
            db.movieDao().searchById(roomId).let {
                withContext(Dispatchers.Main) {
                    if (it == null) {
                        callback(false, null)
                    } else {
                        callback(true, it)
                    }
                }
            }
        }
    }
}