package site.disyfa.room

import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import site.disyfa.room.database.Berita
import site.disyfa.room.database.BeritaDao
import site.disyfa.room.database.BeritaRoomDatabase
import site.disyfa.room.model.ApiResponse
import site.disyfa.room.model.News
import site.disyfa.room.network.ApiClient
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

class MainActivity : AppCompatActivity(), IBeritaFavourite {
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: NewsAdapter
    private lateinit var mBeritaDao: BeritaDao
    private lateinit var executorService: ExecutorService
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        executorService = Executors.newSingleThreadExecutor()
        val db = BeritaRoomDatabase.getDatabase(this)
        mBeritaDao = db!!.beritaDao()!!
        setContentView(R.layout.activity_main)
        recyclerView = findViewById(R.id.rv_news)
        val favButton: Button = findViewById(R.id.view_fav)
        recyclerView.layoutManager = LinearLayoutManager(this)
        getNews()

        favButton.setOnClickListener{
            if (favButton.text=="Favorit"){
                getFavourite()
                favButton.setText("Semua")
            }else{
                getNews()
                favButton.setText("Favorit")
            }
        }
    }
    private fun getNews(){
        val client = ApiClient.getInstance()
        val responseApi = client.getCnn()
        val newsList = mutableListOf<News>()
        responseApi.enqueue(object: Callback<ApiResponse> {
            override fun onResponse(call: Call<ApiResponse>, response: Response<ApiResponse>) {
                if (response.isSuccessful){
                    for(news in response.body()!!.data.posts)
                        newsList.add(news)
                }
                response.body()?.let {
                    adapter = NewsAdapter(newsList,this@MainActivity)
                    recyclerView.adapter = adapter
                }
            }
            override fun onFailure(p0: Call<ApiResponse>, p1: Throwable) {

            }

        })
    }
    private fun getFavourite(){
        mBeritaDao.allBerita.observe(this) { berita ->
            val adapter = BeritaAdapter(berita,this@MainActivity)
            recyclerView.adapter = adapter
        }
    }

    override fun onInsertBerita(berita: News) {
        executorService.execute { mBeritaDao.insert(Berita(
            berita.link, berita.title, berita.pubDate, berita.description, berita.thumbnail
        )) }
        getNews()
    }

    override fun onDeleteBerita(berita: News) {
        executorService.execute { mBeritaDao.delete(Berita(
            berita.link, berita.title, berita.pubDate, berita.description, berita.thumbnail
        )) }
    }
    override fun getBerita(link: String, callback: (Boolean) -> Unit) {
        mBeritaDao.getBerita(link).observe(this, Observer { beritaList ->
            if (beritaList != null && beritaList.isNotEmpty()) {
                callback(true) // Berita ditemukan
            } else {
                callback(false) // Berita tidak ditemukan
            }
        })
    }
}