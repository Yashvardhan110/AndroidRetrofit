package com.example.androidretrofit

data class Movie (val id: Int,
                  val overview: String,
                  val poster_path: String,
                  val release_date:String,
                  val title: String,
                  val vote_average: Double,
                  val vote_count:Int)

data class PopularMovies(val results: List<Movie>)

data class Details(val id: Int,
                   val overview: String,
                   val poster_path: String,
                   val homepage:String,
                   val title: String,
                   val vote_average: Double,
                   val vote_count:Int,
                   val status:String,
                   val name:String)