package site.disyfa.moneymanagement.ui

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.CheckBox
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.Spinner
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatButton
import androidx.lifecycle.Observer
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import site.disyfa.moneymanagement.MainActivity
import site.disyfa.moneymanagement.R
import site.disyfa.moneymanagement.localdb.AppRoomDatabase
import site.disyfa.moneymanagement.localdb.CategoryDao
import site.disyfa.moneymanagement.localdb.TransactionDao
import site.disyfa.moneymanagement.model.ApiTransaction
import site.disyfa.moneymanagement.model.Category
import site.disyfa.moneymanagement.model.Transaction
import site.disyfa.moneymanagement.network.ApiClient
import site.disyfa.moneymanagement.network.SingleResponse
import site.disyfa.moneymanagement.util.DBInitializer
import site.disyfa.moneymanagement.util.IconManager
import site.disyfa.moneymanagement.util.IconSpinner
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

class EditActivity : AppCompatActivity() {
    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var mTransactionDao: TransactionDao
    private lateinit var mCategoryDao: CategoryDao
    private lateinit var executorService: ExecutorService
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_edit)
        executorService = Executors.newSingleThreadExecutor()
        val db = AppRoomDatabase.getDatabase(this)
        mTransactionDao = db!!.transactionDao()!!
        mCategoryDao = db!!.categoryDao()!!

        sharedPreferences = getSharedPreferences("UserPrefs", MODE_PRIVATE)
        val iconManager = IconManager(this)
        val categorySpinner = findViewById<Spinner>(R.id.spCategory)
        val cbIncome = findViewById<CheckBox>(R.id.cb_income)
        val btnAddTransaction = findViewById<AppCompatButton>(R.id.btn_add_transaction)
        var oldTransaction: Transaction = Transaction("",sharedPreferences.getString("user_id",null),0,"outcome","","")
        var newTransaction: ApiTransaction = ApiTransaction(sharedPreferences.getString("user_id",null),0,"outcome","","")
        lateinit var categoryList: List<Category>
        val userId: String? = sharedPreferences.getString("user_id",null)
        val transactionId = intent.getStringExtra("TRANSACTION_ID")!!
        mTransactionDao.getTransaction(transactionId).observe(this, Observer { transactionData ->
            mCategoryDao.getCategories(userId).observe(this, Observer { category ->
                categoryList = category
                categorySpinner.adapter = IconSpinner(
                    this,
                    iconManager.categoryIcons, iconManager.colourIcons, "category",category
                )
                categorySpinner.setSelection(category.indexOfFirst { it.name == transactionData.category })
            })
            oldTransaction = transactionData
            fillFields(oldTransaction)

        })

        categorySpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {
            }

            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                categoryList?.let {
                    val selectedCategory = it[position]
                    newTransaction.category = selectedCategory.name?:"sfd"
                    newTransaction.categoryColor = selectedCategory.colour
                    newTransaction.categoryIcon = selectedCategory.icon
                    oldTransaction.category = selectedCategory.name?:"sfd"
                    oldTransaction.categoryColor = selectedCategory.colour
                    oldTransaction.categoryIcon = selectedCategory.icon
                }

            }
        }

        btnAddTransaction.setOnClickListener {
            val progressBar = findViewById<ProgressBar>(R.id.progressBar)
            progressBar.visibility = View.VISIBLE
            btnAddTransaction.visibility = View.GONE
            progressBar.postDelayed({
                progressBar.visibility = View.GONE
            }, 10000)
            if(cbIncome.isChecked){
                newTransaction.type = "income"
                oldTransaction.type = "income"
            }else{
                oldTransaction.type = "outcome"
            }
            newTransaction.amount = findViewById<EditText>(R.id.edt_nominal).text.toString().toInt()
            oldTransaction.amount = findViewById<EditText>(R.id.edt_nominal).text.toString().toInt()
            newTransaction.description = findViewById<EditText>(R.id.edt_description).text.toString()
            oldTransaction.description = findViewById<EditText>(R.id.edt_description).text.toString()
            newTransaction.date = oldTransaction.date
            ApiClient.getInstance().updateTransaction(transactionId,newTransaction).enqueue(object :
                Callback<SingleResponse> {
                override fun onResponse(call: Call<SingleResponse>, response: Response<SingleResponse>) {
                    if (response.isSuccessful) {
                        executorService.execute{ mTransactionDao.update(oldTransaction)}
                        val intent = Intent(this@EditActivity, MainActivity::class.java)
                        startActivity(intent)
                        finish()
                    } else {
                        Log.e("API", "Error: ${response.message()}")
                    }
                }

                override fun onFailure(call: Call<SingleResponse>, t: Throwable) {
                    Log.e("API", "Failure: ${t.message}")
                }
            })

        }



    }

    fun fillFields(transaction: Transaction){
        findViewById<EditText>(R.id.edt_nominal).setText(transaction.amount.toString())
        findViewById<EditText>(R.id.edt_description).setText(transaction.description)
        if (transaction.type=="income"){
            findViewById<CheckBox>(R.id.cb_income).performClick()
        }
    }
}