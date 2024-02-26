package com.example.courcework

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView

class ProductAdapter (
    private var products: List<Product>,
    var context: Context,
    var savePreferences: SharedPreferences?,
    var activity: Activity,
    var db: ShopDataBaseHelper,
): RecyclerView.Adapter<ProductAdapter.MyViewHolder>(){
    class MyViewHolder(view: View): RecyclerView.ViewHolder(view){
        val card: CardView = view.findViewById(R.id.cardProduct)
        val image: ImageView = view.findViewById(R.id.productImage)
        val name: TextView = view.findViewById(R.id.productName)
        val price: TextView = view.findViewById(R.id.productPrice)
        val button: ImageButton = view.findViewById(R.id.productButton)
        val buttonWishlist: ImageButton = view.findViewById(R.id.buttonWishlist)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.product_in_catalog,parent, false)
        return MyViewHolder(view)
    }

    override fun getItemCount(): Int {
        return products.count()
    }

    fun setFilteredList(mList: List<Product>){
        this.products = mList
        notifyDataSetChanged()
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val imageId = context.resources.getIdentifier((products[position].image).lowercase(), "drawable", context.packageName)
        holder.image.setImageResource(imageId)
        holder.name.text = products[position].nameShort
        val priceString = "${products[position].price} ₽"
        holder.price.text = priceString
        val authId = savePreferences!!.getInt("authId", 0)

        holder.buttonWishlist.setOnClickListener{
            val inWishlist = db.getWish(authId, products[position].idProduct)
            if (!inWishlist){
                db.addToWishlist(authId, products[position].idProduct)
                Toast.makeText(context, context.getString(R.string.addToWishlist), Toast.LENGTH_SHORT).show()
            }
            else
                Toast.makeText(context, context.getString(R.string.nowInWishlist), Toast.LENGTH_SHORT).show()
        }

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
            val findId = db.checkProductInBasket(authId, products[position].idProduct)
            if (findId == 0){
                db.productToBasket(authId, products[position].idProduct, 1)
                Toast.makeText(context, "В корзину добавлен товар в размере: 1", Toast.LENGTH_SHORT).show()
            }
            else{
                val lastAmount = db.getAmountPurchases(findId)
                if (lastAmount < maxAmount){
                    val newAmount = lastAmount + 1
                    db.changeAmountInBasket(findId ,newAmount)
                    Toast.makeText(context, "Изменено количество товара на $newAmount", Toast.LENGTH_SHORT).show()
                }
                else
                    Toast.makeText(context, "Максимальное количество товара в корзине", Toast.LENGTH_SHORT).show()
            }
        }
    }
}