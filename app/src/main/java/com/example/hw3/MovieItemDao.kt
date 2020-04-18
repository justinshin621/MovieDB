package com.example.hw3

import androidx.lifecycle.LiveData
import androidx.room.*
import io.reactivex.Flowable
import org.intellij.lang.annotations.Flow
import retrofit2.http.QueryName

@Dao
interface MovieItemDao{

    @Query("SELECT * FROM movie_table order BY release_date DESC")
    fun getAllMovies(): LiveData<List<MovieItem>>

    @Query("SELECT * FROM movie_table order BY title ASC")
    fun getTitleMovies(): LiveData<List<MovieItem>>

    @Query("SELECT * FROM movie_table order BY vote_average DESC")
    fun getRatingMovies(): LiveData<List<MovieItem>>

    @Query ("SELECT * FROM movie_table WHERE liked = :liked")
    fun getLikedMovies(liked :String): LiveData<List<MovieItem>>


    @Query ("UPDATE movie_table SET liked = :liked WHERE title = :title")
    fun setLike(liked: String, title: String)

    @Query ("DELETE FROM movie_table")
    fun DeleteAll()

    @Insert (onConflict = OnConflictStrategy.REPLACE)
    fun insertMovie(movie: MovieItem)

    @Query("SELECT * FROM movie_table order BY release_date ASC")
    fun getAllMoviesAsc(): LiveData<List<MovieItem>>


}