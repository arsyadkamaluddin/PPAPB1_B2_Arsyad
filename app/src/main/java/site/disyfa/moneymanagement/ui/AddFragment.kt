package site.disyfa.moneymanagement.ui

import android.content.Context.MODE_PRIVATE
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.CheckBox
import android.widget.EditText
import android.widget.Spinner
import androidx.appcompat.widget.AppCompatButton
import androidx.lifecycle.Observer
import androidx.navigation.findNavController
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
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
import site.disyfa.moneymanagement.util.StringGenerator
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [AddFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class AddFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var mTransactionDao: TransactionDao
    private lateinit var mCategoryDao: CategoryDao
    private lateinit var executorService: ExecutorService

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        executorService = Executors.newSingleThreadExecutor()
        val db = AppRoomDatabase.getDatabase(super.requireContext())
        mTransactionDao = db!!.transactionDao()!!
        mCategoryDao = db!!.categoryDao()!!
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_add, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        sharedPreferences = requireContext().getSharedPreferences("UserPrefs", MODE_PRIVATE)
        val iconManager = IconManager(view.context)
        val iconSpinner = view.findViewById<Spinner>(R.id.spIcon)
        val cbIncome = view.findViewById<CheckBox>(R.id.cb_income)
        val btnAddTransaction = view.findViewById<AppCompatButton>(R.id.btn_add_transaction)
        var transaction: ApiTransaction = ApiTransaction(sharedPreferences.getString("user_id",null),0,"outcome","","")
        val userId: String? = sharedPreferences.getString("user_id",null)
        lateinit var categoryList: List<Category>
        mCategoryDao.getCategories(userId).observe(requireActivity(), Observer { category ->
            categoryList = category
            iconSpinner.adapter = IconSpinner(
                view.context,
                iconManager.categoryIcons, iconManager.colourIcons, "category",category
            )
        })

        btnAddTransaction.setOnClickListener {
            if(cbIncome.isChecked){
                transaction.type = "income"
            }
            transaction.amount = view.findViewById<EditText>(R.id.edt_nominal).text.toString().toInt()
            transaction.description = view.findViewById<EditText>(R.id.edt_description).text.toString()
            ApiClient.getInstance().postTransaction(transaction).enqueue(object :
                Callback<SingleResponse> {
                override fun onResponse(call: Call<SingleResponse>, response: Response<SingleResponse>) {
                    if (response.isSuccessful) {
                        requireActivity().findNavController(R.id.nav_host_fragment).navigate(R.id.homeFragment)
                        DBInitializer(requireActivity().application).syncTransaction()
                    } else {
                        Log.e("API", "Error: ${response.message()}")
                    }
                }

                override fun onFailure(call: Call<SingleResponse>, t: Throwable) {
                    // Tangani kegagalan (misalnya, jaringan error)
                    Log.e("API", "Failure: ${t.message}")
                }
            })

        }


        iconSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
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
                    transaction.category = selectedCategory.name?:"sfd"
                    transaction.categoryColor = selectedCategory.colour
                    transaction.categoryIcon = selectedCategory.icon
                }
            }
        }

    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment AddFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            AddFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}