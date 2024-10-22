package com.example.tablayout

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.text.Editable
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity.MODE_PRIVATE
import androidx.appcompat.widget.AppCompatButton

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [RegisterFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class RegisterFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    private lateinit var listener: OnFormListener
    private lateinit var sharedPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_register, container, false)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        sharedPreferences = requireActivity().getSharedPreferences("UserPrefs", Context.MODE_PRIVATE)
        val switchButton = view.findViewById<AppCompatButton>(R.id.btn_register)
        switchButton.setOnClickListener {
            var name = view.findViewById<EditText>(R.id.name)
            var nim = view.findViewById<EditText>(R.id.nim)
            var email = view.findViewById<EditText>(R.id.email)
            var password = view.findViewById<EditText>(R.id.password)
            if(name.text.isNotEmpty() && email.text.isNotEmpty() && password.text.isNotEmpty()){
                sharedPreferences.edit().apply{
                    putString("name",name.text.toString())
                    putString("nim",nim.text.toString())
                    putString("email",email.text.toString())
                    putString("password",password.text.toString())
                    apply()
                }

                name.setText("")
                nim.setText("")
                email.setText("")
                password.setText("")
                listener.onRegister()
            }else{
                Toast.makeText(super.getActivity(), "Isi semua data", Toast.LENGTH_SHORT).show()
            }

        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is OnFormListener) {
            listener = context
        } else {
            throw ClassCastException("$context must implement OnRegisterFragmentListener")
        }
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment RegisterFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            RegisterFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}