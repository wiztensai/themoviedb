package com.wiz.moviedb.domain

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "favorite")
class MovieModel {
    @PrimaryKey(autoGenerate = true)
    var room_id = 0

    var poster_path = ""
    var id = ""
    var release_date = ""
    var overview = ""
    var title = ""
}