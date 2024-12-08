package site.disyfa.retrofit

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import site.disyfa.retrofit.model.ApiResponse
import site.disyfa.retrofit.model.News
import site.disyfa.retrofit.network.ApiClient

class MainActivity : AppCompatActivity() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: NewsAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        recyclerView = findViewById(R.id.rv_news)
        recyclerView.layoutManager = LinearLayoutManager(this)
        getNews()
    }
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
            }
            override fun onFailure(p0: Call<ApiResponse>, p1: Throwable) {

            }

        })

    }
}