package com.example.androidretrofit

import android.graphics.BitmapFactory
import android.net.Uri
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.*
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MovieAdapter(val movieList: List<Movie>): RecyclerView.Adapter<MovieAdapter.ViewHolder>() {

    class ViewHolder(view: View): RecyclerView.ViewHolder(view){
        val posterIV = view.findViewById<ImageView>(R.id.imageView)
        val titleT = view.findViewById<TextView>(R.id.titleT)
        val overviewT = view.findViewById<TextView>(R.id.overviewT)
        val votingT = view.findViewById<TextView>(R.id.votingT)



    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater= LayoutInflater.from(parent.context)
        val v= inflater.inflate(R.layout.item_movie_layout, parent, false)
        return ViewHolder(v)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val movie= movieList[position]
        holder.titleT.text= movie.title
        holder.overviewT.text=movie.overview
        holder.votingT.text= "Rating: ${movie.vote_average}"

        val imageUrl="https://image.tmdb.org/t/p/w500${movie.poster_path}"

        Glide.with(holder.itemView.context).load(Uri.parse(imageUrl)).into(holder.posterIV)

//        val req = TmdbInterface.getInstance().getImage(imageUrl)
//        req.enqueue(object: Callback<ResponseBody>{
//           override fun onResponse(call: Call<ResponseBody>,response: Response<ResponseBody>) {
//               if (response.isSuccessful) {
//                   val bytes = response.body()?.bytes()
//                   val bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes?.size ?: 0)
//                   holder.posterIV.setImageBitmap(bitmap)
//               }
//           }
//
//               override fun onFailure(call: Call<ResponseBody>, t: Throwable)
//               {
//                   Log.d("MovieAdapter","Failed to download : ${t.message}")
//               }
//           })

    }

    override fun getItemCount() = movieList.size

}