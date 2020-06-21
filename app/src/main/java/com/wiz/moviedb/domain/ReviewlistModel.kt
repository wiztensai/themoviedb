package com.wiz.moviedb.domain

class ReviewlistModel {
    var results = mutableListOf<ReviewModel>()
    var id = 0
    var page = 0
    var total_results = 0
    var total_pages = 0
}