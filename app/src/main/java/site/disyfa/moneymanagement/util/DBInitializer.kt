package site.disyfa.moneymanagement.util

import android.app.Application
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import site.disyfa.moneymanagement.localdb.AppRoomDatabase
import site.disyfa.moneymanagement.localdb.CategoryDao
import site.disyfa.moneymanagement.localdb.TransactionDao
import site.disyfa.moneymanagement.localdb.UserDao
import site.disyfa.moneymanagement.model.*
import site.disyfa.moneymanagement.network.ApiClient
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

class DBInitializer(application: Application) {

    private val mTransactionDao: TransactionDao
    private val mCategoryDao: CategoryDao
    private val mUserDao: UserDao
    private val executorService: ExecutorService
    private val client = ApiClient.getInstance()

    init {
        // Memastikan menggunakan aplikasi context untuk database
        val db = AppRoomDatabase.getDatabase(application)
        mTransactionDao = db!!.transactionDao()!!
        mCategoryDao = db!!.categoryDao()!!
        mUserDao = db!!.userDao()!!

        // ExecutorService untuk memastikan database beroperasi di thread terpisah
        executorService = Executors.newSingleThreadExecutor()
    }

    // Fungsi untuk menghapus semua data di Room
    fun deleteAllRoomData() {
        executorService.execute {
            mTransactionDao.deleteAllTransactions()
            mUserDao.deleteAllUsers()
            mCategoryDao.deleteAllCategories()
        }
    }

    // Fungsi untuk mengambil data dari API dan memasukkan ke dalam Room
    fun getAllDatabaseData() {
        // Ambil data pengguna
        val userResponse = client.getUsers()
        userResponse.enqueue(object : Callback<List<User>> {
            override fun onResponse(p0: Call<List<User>>, p1: Response<List<User>>) {
                p1.body()?.let { users ->
                    executorService.execute {
                        for (user in users) {
                            mUserDao.insert(user)
                        }
                    }
                }
            }

            override fun onFailure(p0: Call<List<User>>, p1: Throwable) {
                // Penanganan gagal
            }
        })

        // Ambil data transaksi
        val transactionResponse = client.getTransactions()
        transactionResponse.enqueue(object : Callback<List<Transaction>> {
            override fun onResponse(p0: Call<List<Transaction>>, p1: Response<List<Transaction>>) {
                p1.body()?.let { transactions ->
                    executorService.execute {
                        for (transaction in transactions) {
                            mTransactionDao.insert(transaction)
                        }
                    }
                }
            }

            override fun onFailure(p0: Call<List<Transaction>>, p1: Throwable) {
                // Penanganan gagal
            }
        })

        // Ambil data kategori
        val categoryResponse = client.getCategories()
        categoryResponse.enqueue(object : Callback<List<Category>> {
            override fun onResponse(p0: Call<List<Category>>, p1: Response<List<Category>>) {
                p1.body()?.let { categories ->
                    executorService.execute {
                        for (category in categories) {
                            mCategoryDao.insert(category)
                        }
                    }
                }
            }

            override fun onFailure(p0: Call<List<Category>>, p1: Throwable) {
                // Penanganan gagal
            }
        })
    }

    // Fungsi untuk mensinkronkan semua data
    fun syncDatabase() {
        deleteAllRoomData() // Hapus semua data lama
        getAllDatabaseData() // Ambil dan simpan data terbaru
    }
    fun syncTransaction(){
        executorService.execute {
            mTransactionDao.deleteAllTransactions()
        }
        val transactionResponse = client.getTransactions()
        transactionResponse.enqueue(object : Callback<List<Transaction>> {
            override fun onResponse(p0: Call<List<Transaction>>, p1: Response<List<Transaction>>) {
                p1.body()?.let { transactions ->
                    executorService.execute {
                        for (transaction in transactions) {
                            mTransactionDao.insert(transaction)
                        }
                    }
                }
            }

            override fun onFailure(p0: Call<List<Transaction>>, p1: Throwable) {
                // Penanganan gagal
            }
        })
    }
    fun syncUser(){
        executorService.execute {
            mUserDao.deleteAllUsers()
        }
        val userResponse = client.getUsers()
        userResponse.enqueue(object : Callback<List<User>> {
            override fun onResponse(p0: Call<List<User>>, p1: Response<List<User>>) {
                p1.body()?.let { users ->
                    executorService.execute {
                        for (user in users) {
                            mUserDao.insert(user)
                        }
                    }
                }
            }

            override fun onFailure(p0: Call<List<User>>, p1: Throwable) {
                // Penanganan gagal
            }
        })
    }
    fun syncCategory(){
        executorService.execute {
            mCategoryDao.deleteAllCategories()
        }
        val categoryResponse = client.getCategories()
        categoryResponse.enqueue(object : Callback<List<Category>> {
            override fun onResponse(p0: Call<List<Category>>, p1: Response<List<Category>>) {
                p1.body()?.let { categories ->
                    executorService.execute {
                        for (category in categories) {
                            mCategoryDao.insert(category)
                        }
                    }
                }
            }

            override fun onFailure(p0: Call<List<Category>>, p1: Throwable) {
                // Penanganan gagal
            }
        })
    }
}
