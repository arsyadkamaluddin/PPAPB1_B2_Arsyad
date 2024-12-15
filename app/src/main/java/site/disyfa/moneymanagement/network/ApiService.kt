package site.disyfa.moneymanagement.network

import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.DELETE
import retrofit2.http.Path
import site.disyfa.moneymanagement.model.*

interface ApiService {

    @GET("users")
    fun getUsers(): Call<List<User>>

    @GET("users/{id}")
    fun getUser(@Path("id") id: String): Call<User>

    @POST("users")
    fun postUser(@Body user: ApiUser): Call<SingleResponse>

    @POST("users/{id}")
    fun updateUser(@Path("id") id: String, @Body user: ApiUser): Call<SingleResponse>

    @DELETE("users/{id}")
    fun deleteUser(@Path("id") id: String): Call<SingleResponse>

    @GET("transactions")
    fun getTransactions(): Call<List<Transaction>>

    @GET("transactions/{id}")
    fun getTransaction(@Path("id") id: String): Call<Transaction>

    @POST("transactions")
    fun postTransaction(@Body transaction: ApiTransaction): Call<SingleResponse>

    @POST("transactions/{id}")
    fun updateTransaction(@Path("id") id: String, @Body transaction: ApiTransaction): Call<SingleResponse>

    @DELETE("transactions/{id}")
    fun deleteTransaction(@Path("id") id: String): Call<SingleResponse>

    @GET("categories")
    fun getCategories(): Call<List<Category>>

    @GET("categories/{id}")
    fun getCategory(@Path("id") id: String): Call<Category>

    @POST("categories")
    fun postCategory(@Body category: ApiCategory): Call<SingleResponse>

    @POST("categories/{id}")
    fun updateCategory(@Path("id") id: String, @Body category: ApiCategory): Call<SingleResponse>

    @DELETE("categories/{id}")
    fun deleteCategory(@Path("id") id: String): Call<SingleResponse>

}