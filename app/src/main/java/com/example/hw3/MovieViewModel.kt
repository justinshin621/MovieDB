package com.example.hw3

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import java.util.*
import kotlin.coroutines.CoroutineContext

class MovieViewModel(application: Application): AndroidViewModel(application){


    private var parentJob = Job()
    private val coroutineContext: CoroutineContext
        get() = parentJob + Dispatchers.Main


    private val scope = CoroutineScope(coroutineContext)

    private var disposable: Disposable? = null

    private val repository: MovieItemRepository = MovieItemRepository(MovieRoomDatabase.getDatabase(application).movieDao())

    var moviesList : MediatorLiveData<List<MovieItem>> = MediatorLiveData<List<MovieItem>>()

    private var  currentOrder  = "Date"
    private var isLiked = "No"

    init {
            moviesList.addSource(repository.allMovies) { result ->
                if (currentOrder == "Date") {
                    result?.let { moviesList.value = it }
                }
            }
            moviesList.addSource(repository.titleMovies) { result ->
                if (currentOrder == "Title") {
                    result?.let { moviesList.value = it }
                }
            }
            moviesList.addSource(repository.ratingMovies) { result ->
                if (currentOrder == "Rating") {
                    result?.let {
                        moviesList.value = it
                    }
                }
            }
            moviesList.addSource(repository.likedMovies) {
                result-> if(currentOrder == "Liked"){
                result?.let {
                    moviesList.value = it
                    }
                }
            }

    }



    fun rearrangeMovies(order: String) = when(order) {
        "Date" -> repository.allMovies.value?.let {moviesList.value = it}
        "Title" -> repository.titleMovies.value?.let{ moviesList.value = it}
        "Rating" -> repository.ratingMovies.value?.let{moviesList.value = it}
        "Liked" -> repository.likedMovies.value?.let{moviesList.value = it}
        else -> repository.allMovies.value?.let {moviesList.value = it}
    }.also {currentOrder = order}

    fun refreshMovies(page: Int){

        //TODO: add your API key from themoviedb.org
        disposable =
            RetrofitService.create("https://api.themoviedb.org/3/").getNowPlaying("42087cb54934e3e3457197ff378fb210",page).subscribeOn(
                Schedulers.io()).observeOn(
                AndroidSchedulers.mainThread()).subscribe(
                {result -> showResult(result)},
                {error -> showError(error)})
    }

    private fun showError(error: Throwable?) {
        Log.e("HERE", "uh oh", error)
    }

    private fun showResult(movies: Movies?) {
        deleteAll()
        movies?.results?.forEach { movie ->
            movie.liked = "false"
            insert(movie)
        }
    }

    fun setLike(liked : String, title: String) = scope.launch(Dispatchers.IO) {
        repository.setLiked(liked, title)
    }

    private fun insert(movie: MovieItem) = scope.launch(Dispatchers.IO) {
        repository.insert(movie)
    }

    private fun deleteAll() = scope.launch (Dispatchers.IO){
        repository.deleteAll()
    }



}


