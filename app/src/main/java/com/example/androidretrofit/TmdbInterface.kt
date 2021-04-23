package com.example.androidretrofit

import okhttp3.ResponseBody
import retrofit2.*
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query
import retrofit2.http.Url

interface TmdbInterface {

    @GET("3/movie/popular")
    fun getMovies(@Query("api_key") key: String): Call<PopularMovies>

    @GET
    fun getImage(@Url url:String): Call<ResponseBody>

    @GET("3/movie/{movie_id}")
    fun getDetails(@Path("movie_id") id:Int, @Query("api_key")key:String): Call<MovieDetails>

    companion object{
        val BASE_URL ="https://api.themoviedb.org/"

        fun getInstance() : TmdbInterface
        {
           val builder = Retrofit.Builder()
            builder.addConverterFactory(GsonConverterFactory.create())
            builder.baseUrl(BASE_URL)

            val retrofit = builder.build() //create object of retrofit
            return retrofit.create(TmdbInterface::class.java)
        }
    }
}