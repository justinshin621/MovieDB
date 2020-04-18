package com.example.hw3

import androidx.annotation.WorkerThread
import androidx.lifecycle.LiveData

class MovieItemRepository(private val movieDao: MovieItemDao){

    val allMovies: LiveData<List<MovieItem>> = movieDao.getAllMovies()
    val titleMovies : LiveData<List<MovieItem>> = movieDao.getTitleMovies()
    val ratingMovies : LiveData<List<MovieItem>> = movieDao.getRatingMovies()
    val likedMovies : LiveData<List<MovieItem>> = movieDao.getLikedMovies("True")

    @WorkerThread
    fun setLiked(liked : String, title: String){
        movieDao.setLike(liked, title)
    }


    @WorkerThread
    fun insert(movie: MovieItem){
        movieDao.insertMovie(movie)
    }

    @WorkerThread
    fun deleteAll(){
        movieDao.DeleteAll()
    }


}