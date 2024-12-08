package site.disyfa.retrofit.network

import retrofit2.Call
import retrofit2.http.GET
import site.disyfa.retrofit.model.ApiResponse

interface ApiService {
    @GET("/cnn/terbaru")
    fun getCnn(): Call<ApiResponse>
}