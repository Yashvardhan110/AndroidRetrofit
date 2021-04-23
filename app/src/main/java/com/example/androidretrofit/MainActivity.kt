package com.example.androidretrofit

import android.app.DownloadManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.net.Uri
import android.opengl.Visibility
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.ProgressBar
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File

class MainActivity : AppCompatActivity() {
    lateinit var rView: RecyclerView
    lateinit var pBar: ProgressBar

    var DOWNLOAD_ID:Long = 0

    val receiver = DownloadCompleteReceiver()
    var file: File? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        pBar = findViewById(R.id.progressBar)
        rView = findViewById(R.id.rView)
        rView.layoutManager= LinearLayoutManager(this)

        //call
        val key = resources.getString(R.string.apiKey)
        val request = TmdbInterface.getInstance().getMovies(key)

        //Async -> Creates a thread itself -> We don't need to create a thread
        request.enqueue(PopularMoviesCallback())

        //Register Receiver
        registerReceiver(receiver, IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE))

    }

    override fun onDestroy() {
        super.onDestroy()
        unregisterReceiver(receiver)
    }

    inner class PopularMoviesCallback: Callback<PopularMovies> {
        override fun onResponse(call: Call<PopularMovies>, response: Response<PopularMovies>) {
            if (response.isSuccessful){
                val movies = response.body()
                Log.d("MainActivity","List: $movies")
                pBar.visibility = View.INVISIBLE
                movies?.results?.let{
                    rView.adapter = MovieAdapter(movies?.results)
                }

            }
        }

        override fun onFailure(call: Call<PopularMovies>, t: Throwable) {
            Toast.makeText(this@MainActivity,
                "Error Loading Movies: ${t.message}", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menu?.add("Download TnC")
        menu?.add("Show")
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        when(item.title){
            "Download TnC" -> {
                startDownload("https://research.nhm.org/pdfs/10840/10840-001.pdf")

            }
            "Show"->{
                val pdfIntent = Intent(Intent.ACTION_VIEW)
                pdfIntent.setDataAndType(Uri.fromFile(file),"application/pdf")
                pdfIntent.flags = Intent.FLAG_ACTIVITY_NO_HISTORY
                startActivity(pdfIntent)
            }

        }

        return super.onOptionsItemSelected(item)
    }

    private fun startDownload(fileUrl:String){
        val dManager = getSystemService(DOWNLOAD_SERVICE) as DownloadManager
        val fileName = fileUrl.substring(fileUrl.lastIndexOf('/')+1)

        file = File((Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)),fileName)

        val request = DownloadManager.Request(Uri.parse(fileUrl))

        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE)
        request.setAllowedOverMetered(true)
        request.setAllowedOverRoaming(true)
        request.setTitle(fileName)
        request.setDescription("Downloading...")
        request.setDestinationUri(Uri.fromFile(file))
//        request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS,fileName)

        DOWNLOAD_ID = dManager.enqueue(request)
    }

    inner class DownloadCompleteReceiver: BroadcastReceiver(){

        override fun onReceive(context: Context?, intent: Intent?) {
            val id = intent?.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, 0)
            if(id == DOWNLOAD_ID)
            {
                Toast.makeText(this@MainActivity,"Download Complete",
                Toast.LENGTH_LONG).show()


            }
        }

    }
}