package com.example.courcework

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.BitmapFactory
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.LinearLayout
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.courcework.databinding.FragmentBasketBinding
import com.example.courcework.databinding.FragmentProfileBinding
import java.sql.Blob

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [Profile.newInstance] factory method to
 * create an instance of this fragment.
 */
class Profile() : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        val view = binding.root
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val activity = activity
        val context = requireContext()

        val savePreferences = activity?.getSharedPreferences("savePreferences", Context.MODE_PRIVATE)
        val authName = savePreferences?.getString("authName","")
        val authPassword = savePreferences?.getString("authPassword","")
        val authId = savePreferences?.getInt("authId", 0)
        val db = ShopDataBaseHelper(context, null)
        val userData = db.getUserData(authName!!,authPassword!!)

        binding.buttonHideInformation.setOnClickListener{
            val dialog = HideInformation(userData.phone, userData.address)
            dialog.show(parentFragmentManager, "none")
        }

        binding.changeProfile.setOnClickListener{
            val intent = Intent(activity,ProfileChangeActivity::class.java)
            intent.putExtra("id", userData.id as Int)
            startActivity(intent)
        }

        binding.buttonDeleteAccount.setOnClickListener{
            db.deleteUser(authId!!)
            val intent = Intent(activity,MainActivity::class.java)
            val editSave: SharedPreferences.Editor = savePreferences.edit()
            editSave.putBoolean("behaviourToAccess", false)
            editSave.putInt("authId", 0)
            editSave.apply()
            startActivity(intent)
        }

        binding.buttonExit.setOnClickListener{
            val intent = Intent(activity,MainActivity::class.java)
            val editSave: SharedPreferences.Editor = savePreferences.edit()
            editSave.putBoolean("behaviourToAccess", false)
            editSave.putInt("authId", 0)
            editSave.apply()
            startActivity(intent)
        }

        if ((userData.nickname as String) != "")
            binding.textNickname.text = (userData.nickname as String)

        if ((userData.email as String) != "")
            binding.textEmail.text = (userData.email as String)

        if ((userData.sex as String) != "")
            binding.textSex.text = (userData.sex as String)

        if (!userData.image.contentEquals(byteArrayOf())){
            binding.imageView.setImageBitmap(BitmapFactory.decodeByteArray(userData.image, 0, userData.image.size))
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment Profile.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            Profile().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}