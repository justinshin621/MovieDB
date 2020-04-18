package com.example.hw3

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Layout
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import androidx.navigation.navArgs
import com.example.hw3.DateConverter
import com.bumptech.glide.request.RequestOptions
import kotlinx.android.synthetic.main.fragment_detail.*
import java.util.*


class list_view : Fragment() {

    var navController: NavController? = null
    private lateinit var model: MovieViewModel
    var date : DateConverter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(

        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        return inflater.inflate(R.layout.fragment_list_view, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val spinner = view.findViewById<Spinner>(R.id.spinner)
        var sortType = ""
        val checkBox : CheckBox = view.findViewById(R.id.checkBox)

        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener,
            AdapterView.OnItemClickListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {
            }

            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                val selectedSort = parent?.getItemAtPosition(position).toString()
                sortType = selectedSort
            }

            override fun onItemClick(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {

            }
        }
        model = ViewModelProviders.of(this).get(MovieViewModel::class.java)

        var recyclerView = view.findViewById<RecyclerView>(R.id.movie_list)
        var adapter = MovieListAdapter()
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(this.context)
        recyclerView.setNestedScrollingEnabled(false)

        model.moviesList.observe(
        this,
        Observer { movies ->
        movies?.let {
        adapter.setMovies(it)
                }
            }
        )

        navController = Navigation.findNavController(view)

        (view.findViewById<Button>(R.id.refresh)).setOnClickListener {
            model.rearrangeMovies(sortType)
            model.refreshMovies(1)
        }

       checkBox.setOnCheckedChangeListener{_, _ ->
            if(checkBox.isChecked){
                model.rearrangeMovies("Liked")
                Log.d("Checked the Liked", "hope this works")

            }
            else {
                model.rearrangeMovies(sortType)
                Log.d("Dischecked Liked", "No")
            }

       }


    }


//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    inner class MovieListAdapter():
        RecyclerView.Adapter<MovieListAdapter.MovieViewHolder>(){

        private var movies = emptyList<MovieItem>()

        internal fun setMovies(movies: List<MovieItem>) {
            this.movies = movies
            notifyDataSetChanged()
        }

        override fun getItemCount(): Int {

            return movies.size
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieViewHolder {


            val v = LayoutInflater.from(parent.context)
                .inflate(R.layout.card_view, parent, false)
            return MovieViewHolder(v)

        }

        override fun onBindViewHolder(holder: MovieViewHolder, position: Int) {

            Glide.with(this@list_view).load(resources.getString(R.string.picture_base_url)+movies[position].poster_path).apply( RequestOptions().override(128, 128)).into(holder.view.findViewById(R.id.poster))

            holder.view.findViewById<TextView>(R.id.title).text=movies[position].title

            holder.view.findViewById<TextView>(R.id.rating).text=movies[position].vote_average.toString()

            holder.itemView.setOnClickListener(){

                var title : String = movies[position].title
                var posterPath : String = resources.getString(R.string.picture_base_url)+movies[position].poster_path
                var date : String = (movies[position].release_date).toString()
                var overview : String = movies[position].overview

                var stuff : ArrayList<String> = mutableListOf<String>(title,posterPath,date,overview) as ArrayList<String>

                var bundle : Bundle
                bundle = bundleOf("details" to stuff)

                Log.d("navigated value", position.toString() )
                view?.findNavController()?.navigate(R.id.action_list_view_to_detail, bundle)

            }

        }

        inner class MovieViewHolder(val view: View): RecyclerView.ViewHolder(view), View.OnClickListener{
            override fun onClick(view: View?){

                if (view != null) {


                }


            }


        }
    }

}




