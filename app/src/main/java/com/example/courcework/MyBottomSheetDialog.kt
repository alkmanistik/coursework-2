package com.example.courcework

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.courcework.databinding.FragmentDefaultProductSettingBinding
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class MyBottomSheetDialog : BottomSheetDialogFragment() {
    private var _binding: FragmentDefaultProductSettingBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = FragmentDefaultProductSettingBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val activity = activity
        val context = requireContext()
        val savePreferences = activity?.getSharedPreferences("savePreferences", Context.MODE_PRIVATE)
        val db = ShopDataBaseHelper(context, null)

        val authId = savePreferences?.getInt("authId",0)
        val productNowId = savePreferences?.getInt("productNowId",0)
        val purchasesNowId = savePreferences?.getInt("purchasesNowId",0)
        val statusNow = savePreferences?.getString("statusNow","")

        when(statusNow){
            status[0] -> basketSetting(authId!!,productNowId!!,purchasesNowId!!,db)
            status[1] -> deliverySetting(authId!!,productNowId!!,purchasesNowId!!,db)
            status[2] -> doneSetting(authId!!,productNowId!!,purchasesNowId!!,db)
            else -> wishlistSetting(authId!!,productNowId!!,purchasesNowId!!,db)
        }
    }
    private fun basketSetting(authId: Int, productNowId: Int, purchasesNowId: Int, db: ShopDataBaseHelper){
        binding.buttonChange.visibility = View.VISIBLE
        binding.buttonChange.setOnClickListener{
            val productList = db.getProductWithId(productNowId)
            val imageId = context?.resources?.getIdentifier((productList[0].image).lowercase(), "drawable", context?.packageName)
            val priceString = "${productList[0].price} ₽"
            val intent = Intent(context, ProductPageActivity::class.java)
            intent.putExtra("productId", productList[0].idProduct)
            intent.putExtra("productFull", productList[0].nameFull)
            intent.putExtra( "productShort", productList[0].nameShort)
            intent.putExtra("productCategory", productList[0].category)
            intent.putExtra("productDescription", productList[0].description)
            intent.putExtra("productPrice", priceString)
            intent.putExtra("productImage", imageId)
            dismiss()
            context?.startActivity(intent)
        }
        binding.buttonDelete.setOnClickListener{
            db.deletePurchases(purchasesNowId)
            dismiss()
        }
    }
    private fun deliverySetting(authId: Int, productNowId: Int, purchasesNowId: Int, db: ShopDataBaseHelper){
        binding.buttonDelete.visibility = View.GONE
        binding.textType.visibility = View.VISIBLE
        binding.textType.text = getString(R.string.statusDelivery)
        binding.textDate.visibility = View.VISIBLE
        binding.textDate.text = db.getDatePurchases(purchasesNowId)
        binding.buttonDone.visibility = View.VISIBLE
        binding.buttonDone.setOnClickListener{
            db.insertNewLog(authId, status[2], purchasesNowId)
            dismiss()
        }
    }
    private fun wishlistSetting(authId: Int, productNowId: Int, purchasesNowId: Int, db: ShopDataBaseHelper){
        binding.buttonDelete.setOnClickListener{
            db.deleteFromWishlist(authId,productNowId)
            dismiss()
        }
    }
    private fun doneSetting(authId: Int, productNowId: Int, purchasesNowId: Int, db: ShopDataBaseHelper){
        binding.textType.visibility = View.VISIBLE
        binding.textDate.visibility = View.VISIBLE
        binding.textDate.text = db.getDatePurchases(purchasesNowId)
        binding.buttonDelete.setOnClickListener{
            db.deletePurchases(purchasesNowId)
            dismiss()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun dismiss() {
        super.dismiss()
        val intent = Intent(activity, TempActivity::class.java)
        startActivity(intent)
    }

}