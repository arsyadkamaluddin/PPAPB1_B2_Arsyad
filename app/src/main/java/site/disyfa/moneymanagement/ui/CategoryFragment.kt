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
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.Spinner
import androidx.appcompat.widget.AppCompatButton
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import site.disyfa.moneymanagement.R
import site.disyfa.moneymanagement.adapter.CategoryAdapter
import site.disyfa.moneymanagement.localdb.AppRoomDatabase
import site.disyfa.moneymanagement.localdb.CategoryDao
import site.disyfa.moneymanagement.model.ApiCategory
import site.disyfa.moneymanagement.model.Category
import site.disyfa.moneymanagement.network.ApiClient
import site.disyfa.moneymanagement.network.SingleResponse
import site.disyfa.moneymanagement.util.DBInitializer
import site.disyfa.moneymanagement.util.IconManager
import site.disyfa.moneymanagement.util.IconSpinner
import site.disyfa.moneymanagement.util.StringGenerator
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

class CategoryFragment : Fragment() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: CategoryAdapter
    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var mCategoryDao: CategoryDao
    private lateinit var executorService: ExecutorService
    private var param1: String? = null
    private var param2: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        executorService = Executors.newSingleThreadExecutor()
        val db = AppRoomDatabase.getDatabase(super.requireContext())
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
        return inflater.inflate(R.layout.fragment_category, container, false)
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        sharedPreferences = requireContext().getSharedPreferences("UserPrefs", MODE_PRIVATE)
        var category: ApiCategory = ApiCategory(sharedPreferences.getString("user_id",null),null,1,1)
        val iconManager = IconManager(view.context)
        val iconSpinner = view.findViewById<Spinner>(R.id.spIcon)
        val colourSpinner = view.findViewById<Spinner>(R.id.spColour)
        val btnAddCategory = view.findViewById<AppCompatButton>(R.id.btn_add_category)
        btnAddCategory.setOnClickListener{
            val progressBar = view.findViewById<ProgressBar>(R.id.progressBar)
            progressBar.visibility = View.VISIBLE
            btnAddCategory.visibility = View.GONE

            progressBar.postDelayed({
                progressBar.visibility = View.GONE
                btnAddCategory.visibility = View.VISIBLE
            }, 2000)

            category.name = view.findViewById<EditText>(R.id.edt_category_name).text.toString()
            executorService.execute{mCategoryDao.insert(Category(StringGenerator.generateUUIDString(),category.user_id,category.name,category.icon,category.colour))}
            ApiClient.getInstance().postCategory(category).enqueue(object :
                Callback<SingleResponse> {
                override fun onResponse(call: Call<SingleResponse>, response: Response<SingleResponse>) {
                    if (response.isSuccessful) {
                    } else {
                        Log.e("API", "Error: ${response.message()}")
                    }
                }

                override fun onFailure(call: Call<SingleResponse>, t: Throwable) {
                    // Tangani kegagalan (misalnya, jaringan error)
                    Log.e("API", "Failure: ${t.message}")
                }
            })
            iconSpinner.setSelection(0)
            colourSpinner.setSelection(0)
            view.findViewById<EditText>(R.id.edt_category_name).setText("")
        }
        iconSpinner.adapter = IconSpinner(
            view.context,
            iconManager.categoryIcons, null, "icon"
        )

        colourSpinner.adapter = IconSpinner(
            view.context,
            null, iconManager.colourIcons, "colour"
        )
        recyclerView = view.findViewById(R.id.rv_categories)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        val userId: String? = sharedPreferences.getString("user_id",null)
        mCategoryDao.getCategories(userId).observe(this.requireActivity(), Observer { category ->
            adapter = CategoryAdapter(requireContext(),category)
            recyclerView.adapter = adapter
        })

        iconSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {
            }

            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                category.icon = iconManager.categoryIcons[position].id
            }
        }

        colourSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {
            }

            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                category.colour = iconManager.colourIcons[position].id
            }
        }

    }

    override fun onResume() {
        super.onResume()
        val userId: String? = sharedPreferences.getString("user_id",null)
        mCategoryDao.getCategories(userId).observe(this, Observer { category ->
            adapter = CategoryAdapter(requireContext(),category)
            recyclerView.adapter = adapter
        })



    }
    companion object {
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            CategoryFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}