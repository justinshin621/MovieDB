package com.example.hw3

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import org.w3c.dom.Text
import java.util.*
import java.util.ResourceBundle.getBundle


class detail : Fragment() {


    private lateinit var model: MovieViewModel
    private lateinit var poster: ImageView
    private lateinit var title: TextView
    private lateinit var date: TextView
    private lateinit var overview: TextView
    private lateinit var like: RadioButton
    private lateinit var dislike: RadioButton
    private lateinit var radio: RadioGroup

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        return inflater.inflate(R.layout.fragment_detail, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        var items: ArrayList<String> = arguments?.getStringArrayList("details")!!

        poster = view.findViewById(R.id.detailPoster)
        title = view.findViewById(R.id.detailTitle)
        date = view.findViewById(R.id.detailDate)
        overview = view.findViewById(R.id.detailOverview)
        model = ViewModelProviders.of(this).get(MovieViewModel::class.java)


        like = view.findViewById(R.id.likeButton)
        dislike = view.findViewById(R.id.dislikeButton)
        radio = view.findViewById(R.id.radioGroup)

        radio.setOnCheckedChangeListener(
            RadioGroup.OnCheckedChangeListener { _, checkedId ->
                val radio: RadioButton = view.findViewById(checkedId)
                if (radio.text.toString() == "Like"){                                       // RADIO BUTTON LIKED
                    model.setLike("True", items[0])
                    Log.d("Added movie", items[0])
                }
                else if (radio.text.toString() == "Dislike"){                               // RADIO BUTTON DISLIKED
                    model.setLike("False", items[0])
                    Log.d("Removed movie", items[0])
                }
                Toast.makeText(this.context," ${radio.text} : " + items[0],
                    Toast.LENGTH_SHORT).show()
            })




            val movieTitle = view.findViewById<TextView>(R.id.detailTitle)
            val date: TextView = view.findViewById(R.id.detailDate)
            val overview: TextView = view.findViewById(R.id.detailOverview)


            movieTitle.text = items[0]
            Glide.with(this@detail).load(items[1]).apply(RequestOptions().override(128, 128))
                .into(poster)
            date.text = items[2]
            overview.text = items[3]


        }

}

