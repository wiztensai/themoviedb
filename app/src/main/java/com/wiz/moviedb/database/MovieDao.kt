package com.wiz.moviedb.database

import androidx.lifecycle.LiveData
import androidx.room.*
import com.wiz.moviedb.domain.MovieModel

@Dao
interface MovieDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun favorite(d: MovieModel):Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun favoriteAll(d: MutableList<MovieModel>):LongArray

    @Query("SELECT * FROM favorite")
    suspend fun getFavorites(): MutableList<MovieModel>

    @Query("DELETE FROM favorite WHERE id=:movieId")
    suspend fun unFavorite(movieId:Int):Int

    @Query("SELECT * FROM favorite WHERE id =:movieId")
    suspend fun searchById(movieId:Int?): MovieModel?
}