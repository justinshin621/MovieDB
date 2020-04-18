package com.example.hw3

data class Movies(
    var results: List<MovieItem>,
    val total_pages: Int,
    val page: Int)