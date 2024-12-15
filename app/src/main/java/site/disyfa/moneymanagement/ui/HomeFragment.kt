package site.disyfa.moneymanagement.ui

import android.content.Context.MODE_PRIVATE
import android.content.SharedPreferences
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import site.disyfa.moneymanagement.MainActivity
import site.disyfa.moneymanagement.R
import site.disyfa.moneymanagement.adapter.TransactionAdapter
import site.disyfa.moneymanagement.localdb.AppRoomDatabase
import site.disyfa.moneymanagement.localdb.TransactionDao
import site.disyfa.moneymanagement.util.DBInitializer
import site.disyfa.moneymanagement.util.StringGenerator
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"
class HomeFragment : Fragment() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: TransactionAdapter
    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var mTransactionDao: TransactionDao
    private lateinit var executorService: ExecutorService
    private var param1: String? = null
    private var param2: String? = null
    private var totalBalance:Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        executorService = Executors.newSingleThreadExecutor()
        val db = AppRoomDatabase.getDatabase(super.requireContext())
        mTransactionDao = db!!.transactionDao()!!
        sharedPreferences = requireContext().getSharedPreferences("UserPrefs", MODE_PRIVATE)

        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    private fun getTransactions(total: TextView){
        val userId: String? = sharedPreferences.getString("user_id",null)
        val date: String = SimpleDateFormat("yyyy-MM-dd", Locale("id","ID")).format(Date())
        mTransactionDao.getUserTransaction(userId).observe(this.requireActivity(), Observer { transaction ->
            totalBalance = transaction.filter { it.type=="income" }.sumOf { it.amount } - transaction.filter { it.type=="outcome" }.sumOf { it.amount }
            val todayTransaction = transaction.filter { it.date.startsWith(date) }
            var totalOutcome = todayTransaction.filter { it.type=="outcome" }.sumOf { it.amount }
            adapter = TransactionAdapter(requireContext(),todayTransaction)
            recyclerView.adapter = adapter
            total.setText(StringGenerator.formatToRupiah(totalOutcome))
            (requireActivity() as MainActivity).updateBalance(totalBalance)
        })
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val tvName: TextView = view.findViewById(R.id.tv_name)
        val tvDate: TextView = view.findViewById(R.id.tv_date)
        tvDate.setText(SimpleDateFormat("dd MMMM yyyy", Locale("id","ID")).format(Date()))
        tvName.setText("Halo, "+sharedPreferences.getString("name","User"))
        recyclerView = view.findViewById(R.id.rv_transaction)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        getTransactions(view.findViewById<TextView>(R.id.tv_outcome_nominal))


    }

    override fun onResume() {
        super.onResume()
        updateData()
    }

    fun updateData(){
        val tvName: TextView = requireView().findViewById(R.id.tv_name)
        val tvDate: TextView = requireView().findViewById(R.id.tv_date)
        tvDate.setText(SimpleDateFormat("dd MMMM yyyy", Locale("id","ID")).format(Date()))
        tvName.setText("Halo, "+sharedPreferences.getString("name","User"))
    }

    companion object {
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            HomeFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}