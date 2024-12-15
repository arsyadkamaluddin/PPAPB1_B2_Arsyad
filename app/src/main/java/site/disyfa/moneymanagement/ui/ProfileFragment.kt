package site.disyfa.moneymanagement.ui

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity.MODE_PRIVATE
import androidx.appcompat.widget.AppCompatButton
import androidx.lifecycle.Observer
import androidx.navigation.findNavController
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import site.disyfa.moneymanagement.R
import site.disyfa.moneymanagement.localdb.AppRoomDatabase
import site.disyfa.moneymanagement.localdb.TransactionDao
import site.disyfa.moneymanagement.localdb.UserDao
import site.disyfa.moneymanagement.model.ApiUser
import site.disyfa.moneymanagement.model.User
import site.disyfa.moneymanagement.network.ApiClient
import site.disyfa.moneymanagement.network.SingleResponse
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [ProfileFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class ProfileFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var mTransactionDao: TransactionDao
    private lateinit var mUserDao: UserDao
    private lateinit var executorService: ExecutorService
        var newUser: User = User("","","","")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        executorService = Executors.newSingleThreadExecutor()
        val db = AppRoomDatabase.getDatabase(requireContext())
        sharedPreferences = requireContext().getSharedPreferences("UserPrefs", MODE_PRIVATE)
        mTransactionDao = db!!.transactionDao()!!
        mUserDao = db!!.userDao()!!
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
        return inflater.inflate(R.layout.fragment_profile, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        var oldUser: User = User("","","","")
        val btnSaveUser: AppCompatButton = view.findViewById(R.id.btn_register)
        mUserDao.getUser(sharedPreferences.getString("user_id",null)!!).observe(this.requireActivity(), Observer { user ->
            oldUser = user
            newUser = user
            fillFields(oldUser,view)
        })

        btnSaveUser.setOnClickListener {
//            val editor = sharedPreferences.edit()
//            editor.putString("user_id", newUser.id)
//            editor.putString("name", newUser.name)
//            editor.putString("email", newUser.email)
//            editor.apply()
            newUser.name = view.findViewById<EditText>(R.id.edt_fullname).text.toString()
            newUser.email = view.findViewById<EditText>(R.id.edt_email).text.toString()
            newUser.password = view.findViewById<EditText>(R.id.edt_password).text.toString()
            ApiClient.getInstance().updateUser(oldUser.id,ApiUser(newUser.name,newUser.email,newUser.password)).enqueue(object :
                Callback<SingleResponse> {
                override fun onResponse(call: Call<SingleResponse>, response: Response<SingleResponse>) {
                    if (response.isSuccessful) {
                        requireActivity().findNavController(R.id.nav_host_fragment).navigate(R.id.logoutFragment)
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
    }
    fun fillFields(oldUser: User, view: View){
        view.findViewById<EditText>(R.id.edt_fullname).setText(oldUser.name)
        view.findViewById<EditText>(R.id.edt_email).setText(oldUser.email)
        view.findViewById<EditText>(R.id.edt_password).setText(oldUser.password)
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment ProfileFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            ProfileFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}