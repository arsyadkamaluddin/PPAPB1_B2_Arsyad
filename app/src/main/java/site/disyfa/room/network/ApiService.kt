package site.disyfa.room.network

import retrofit2.Call
import retrofit2.http.GET
import site.disyfa.room.model.ApiResponse

interface ApiService {
    @GET("/cnn/terbaru")
    fun getCnn(): Call<ApiResponse>
}