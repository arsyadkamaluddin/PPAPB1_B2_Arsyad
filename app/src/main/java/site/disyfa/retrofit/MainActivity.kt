package site.disyfa.retrofit

import android.os.Bundle
import android.util.Log
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import site.disyfa.retrofit.databinding.ActivityMainBinding
import site.disyfa.retrofit.model.ApiResponse
import site.disyfa.retrofit.model.News
import site.disyfa.retrofit.network.ApiClient

class MainActivity : AppCompatActivity() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: NewsAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        enableEdgeToEdge()
//        binding = ActivityMainBinding.inflate(layoutInflater)
//        setContentView(binding.root)
//
//
//        with(binding){
////            rvNews.apply {
////                getNews { newsList ->
////                    this.adapter = NewsAdapter(newsList)
////                }
////                layoutManager = LinearLayoutManager(this@MainActivity)
////            }
//
//        }
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        recyclerView = findViewById(R.id.rv_news)
        recyclerView.layoutManager = LinearLayoutManager(this)

        getNews()


    }
//    callback: (List<News>)->Unit
    private fun getNews(){
        val client = ApiClient.getInstance()
        val responseApi = client.getCnn()
        val newsList = mutableListOf<News>()

        responseApi.enqueue(object: Callback<ApiResponse>{
            override fun onResponse(call: Call<ApiResponse>, response: Response<ApiResponse>) {
                if (response.isSuccessful){
                    for(news in response.body()!!.data.posts)
                        newsList.add(news)
                }
                response.body()?.let {
                    adapter = NewsAdapter(newsList)
                    recyclerView.adapter = adapter
                }
//                callback(newsList)
            }

            override fun onFailure(p0: Call<ApiResponse>, p1: Throwable) {

            }

        })

    }
//private fun fetchNews() {
//    RetrofitInstance.api.getNews().enqueue(object : Callback<List<News>> {
//        override fun onResponse(call: Call<List<News>>, response: Response<List<News>>) {
//            if (response.isSuccessful) {
//                response.body()?.let {
//                    adapter = NewsAdapter(it)
//                    recyclerView.adapter = adapter
//                }
//            }
//        }
//
//        override fun onFailure(call: Call<List<News>>, t: Throwable) {
//            t.printStackTrace()
//        }
//    })
//}
}