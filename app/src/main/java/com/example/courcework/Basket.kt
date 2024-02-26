package com.example.courcework

import android.app.Activity
import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.courcework.databinding.FragmentBasketBinding

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [Basket.newInstance] factory method to
 * create an instance of this fragment.
 */
class Basket : Fragment() {
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

    private var _binding: FragmentBasketBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentBasketBinding.inflate(inflater, container, false)
        val view = binding.root
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initBasket()

        val activity = activity
        val context = requireContext()
        val savePreferences = activity?.getSharedPreferences("savePreferences", Context.MODE_PRIVATE)
        val authId = savePreferences?.getInt("authId", 0)!!
        val db = ShopDataBaseHelper(context, null)

        binding.buttonPay.setOnClickListener{
            db.insertNewLog(authId, status[1])
            initBasket()
        }

    }

    private fun initBasket(){

        val activity = activity
        val context = requireContext()
        val savePreferences = activity?.getSharedPreferences("savePreferences", Context.MODE_PRIVATE)
        val authId = savePreferences?.getInt("authId", 0)!!
        val db = ShopDataBaseHelper(context, null)
        val amountAndPrice = db.getAmountAndPrice(authId)

        binding.amountBasket.text = "x${amountAndPrice[0]}"
        binding.priceBasket.text = "${amountAndPrice[1]} ₽"

        val productBasket = db.getProductWithStatus(authId, status[0])

        if (productBasket.isNotEmpty()){
            binding.textEmptyBasket.visibility = View.GONE
            binding.listBasket.visibility = View.VISIBLE
            binding.bottomBasketPanel.visibility = View.VISIBLE
            binding.buttonPay.isEnabled = true
            binding.listBasket.layoutManager = LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false)
            binding.listBasket.adapter = ProductBasketAdapter(productBasket, context, savePreferences, activity, parentFragmentManager)
        }
        else{
            binding.textEmptyBasket.visibility = View.VISIBLE
            binding.listBasket.visibility = View.GONE
            binding.bottomBasketPanel.visibility = View.GONE
            binding.buttonPay.isEnabled = false
        }

        val productDelivery = db.getProductWithStatus(authId, status[1])

        if (productDelivery.isNotEmpty()) {
            binding.delivery.visibility = View.VISIBLE
            binding.cardDelivery.visibility = View.VISIBLE
            binding.listDelivery.layoutManager =
                LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false)
            binding.listDelivery.adapter = ProductBasketAdapter(productDelivery, context, savePreferences, activity, parentFragmentManager)
        }
        else{
            binding.delivery.visibility = View.GONE
            binding.cardDelivery.visibility = View.GONE
        }

        val productWishlist = db.getUserWishlist(authId)

        if (productWishlist.isNotEmpty()){
            binding.textEmptyWishlist.visibility = View.GONE
            binding.listWishlist.visibility = View.VISIBLE
            binding.listWishlist.layoutManager = LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false)
            binding.listWishlist.adapter = ProductBasketAdapter(productWishlist, context, savePreferences, activity, parentFragmentManager)
        }
        else{
            binding.textEmptyWishlist.visibility = View.VISIBLE
            binding.listWishlist.visibility = View.GONE
        }

        val productPurchases = db.getProductWithStatus(authId, status[2])

        if (productPurchases.isNotEmpty()){
            binding.textEmptyPurchases.visibility = View.GONE
            binding.listPurchases.visibility = View.VISIBLE
            binding.listPurchases.layoutManager = LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false)
            binding.listPurchases.adapter = ProductBasketAdapter(productPurchases, context, savePreferences, activity, parentFragmentManager)
        }
        else{
            binding.textEmptyPurchases.visibility = View.VISIBLE
            binding.listPurchases.visibility = View.GONE
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    override fun onResume() {
        super.onResume()

        // Проверяем условие, при котором нужно обновить активность
        initBasket()
    }
    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment Basket.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            Basket().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}