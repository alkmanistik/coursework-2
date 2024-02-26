package com.example.courcework

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.RecyclerView
import com.example.courcework.databinding.ActivityMenuBinding
import com.example.courcework.databinding.FragmentBasketBinding
import com.example.courcework.databinding.FragmentDefaultProductSettingBinding
import com.google.android.material.bottomsheet.BottomSheetDialog

class ProductBasketAdapter (
    private var products: List<ProductBasket>,
    var context: Context,
    var savePreferences: SharedPreferences?,
    var activity: Activity,
    var manager: FragmentManager
): RecyclerView.Adapter<ProductBasketAdapter.MyViewHolder>(){
    class MyViewHolder(view: View): RecyclerView.ViewHolder(view){
        val card: CardView = view.findViewById(R.id.cardProduct)
        val image: ImageView = view.findViewById(R.id.productImage)
        val name: TextView = view.findViewById(R.id.productName)
        val price: TextView = view.findViewById(R.id.productPrice)
        val button: ImageButton = view.findViewById(R.id.productButton)
        val amount: TextView = view.findViewById(R.id.amountProduct)
    }

    private lateinit var binding: FragmentDefaultProductSettingBinding

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.product_in_basket,parent, false)
        return MyViewHolder(view)
    }

    override fun getItemCount(): Int {
        return products.count()
    }



    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val imageId = context.resources.getIdentifier((products[position].image).lowercase(), "drawable", context.packageName)
        holder.image.setImageResource(imageId)
        holder.name.text = products[position].nameShort
        val priceString = "${products[position].price} ₽"
        holder.price.text = priceString
        if (products[position].amount != 0)
            holder.amount.text = "x${products[position].amount}"

        holder.card.setOnClickListener{
            val intent = Intent(context, ProductPageActivity::class.java)
            intent.putExtra("productId", products[position].idProduct)
            intent.putExtra("productFull", products[position].nameFull)
            intent.putExtra( "productShort", products[position].nameShort)
            intent.putExtra("productCategory", products[position].category)
            intent.putExtra("productDescription", products[position].description)
            intent.putExtra("productPrice", priceString)
            intent.putExtra("productImage", imageId)
            context.startActivity(intent)
        }

        holder.button.setOnClickListener {
            val editSave: SharedPreferences.Editor = savePreferences!!.edit()
            editSave.putInt("productNowId",products[position].idProduct)
            editSave.putInt("purchasesNowId",products[position].purchasesId)
            editSave.putString("statusNow",products[position].status)
            editSave.apply()


            val view: View = activity.layoutInflater.inflate(R.layout.fragment_default_product_setting, null)
            val dialog = MyBottomSheetDialog()
            dialog.show(manager, "none")
        }

    }
}