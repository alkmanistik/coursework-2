package com.example.courcework

import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast

class ProductPageActivity : AppCompatActivity() {

    private lateinit var savePreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_product_page)

        savePreferences = getSharedPreferences("savePreferences", MODE_PRIVATE)
        val db = ShopDataBaseHelper(this, null)

        val authId = savePreferences.getInt("authId", 0)

        val nameProduct: TextView = findViewById(R.id.textName)
        val nameShortProduct: TextView = findViewById(R.id.textPage)
        val priceProduct: TextView = findViewById(R.id.textPrice)
        val imageProduct: ImageView = findViewById(R.id.imageProduct)
        val categoryProduct: TextView = findViewById(R.id.textTag)
        val descriptionProduct: TextView = findViewById(R.id.textDescription)
        val countProduct: TextView = findViewById(R.id.textCounter)
        val buttonWishlist: ImageButton = findViewById(R.id.buttonWishlist)
        val buttonSub: ImageButton = findViewById(R.id.buttonSub)
        val buttonAdd: ImageButton = findViewById(R.id.buttonAdd)
        val buttonBuy: Button = findViewById(R.id.buttonBuy)
        val buttonBack: ImageButton = findViewById(R.id.buttonBack)
        val arrayStep: Array<Int> = arrayOf(0,0)

        val idProduct = intent.getIntExtra("productId", 0)
        nameProduct.text = intent.getStringExtra("productFull")
        nameShortProduct.text = intent.getStringExtra("productShort")
        priceProduct.text = intent.getStringExtra("productPrice")
        categoryProduct.text = intent.getStringExtra("productCategory")
        descriptionProduct.text = intent.getStringExtra("productDescription")
        imageProduct.setImageResource(intent.getIntExtra("productImage",1))

        var inWishlist: Boolean = db.getWish(authId, idProduct)

        if (inWishlist)
            buttonWishlist.setImageResource(R.drawable.baseline_favorite_24)
        else
            buttonWishlist.setImageResource(R.drawable.baseline_favorite_border_24)

        fun updateBuyBottom(){
            val count = countProduct.text.toString().toInt()
            val begin = 1
            if (count != begin){
                val price = priceProduct.text.toString().dropLast(2).toInt()
                buttonBuy.text = (price * count).toString() + " ₽"
            }
            else{
                buttonBuy.text = getString(R.string.buyThis)
            }
        }

        buttonSub.setOnClickListener{
            arrayStep[1] = 0
            val temp = countProduct.text.toString().toInt()
            if (arrayStep[0] < 9) {
                countProduct.text = limitation(1, maxAmount, temp - 1).toString()
                arrayStep[0]++
            }
            else{
                countProduct.text = limitation(1, maxAmount, temp - 10).toString()
            }
            updateBuyBottom()
        }
        buttonAdd.setOnClickListener{
            arrayStep[0] = 0
            val temp = countProduct.text.toString().toInt()
            if (arrayStep[1] < 9) {
                countProduct.text = limitation(1, maxAmount, temp + 1).toString()
                arrayStep[1]++
            }
            else{
                countProduct.text = limitation(1, maxAmount, temp + 10).toString()
            }
            updateBuyBottom()
        }
        buttonBuy.setOnClickListener{
            val findId = db.checkProductInBasket(authId, idProduct)
            if (findId == 0){
                db.productToBasket(authId, idProduct, countProduct.text.toString().toInt())
                Toast.makeText(this, "В корзину добавлен товар в размере: ${countProduct.text.toString().toInt()}", Toast.LENGTH_SHORT).show()
            }
            else{
                db.changeAmountInBasket(findId ,countProduct.text.toString().toInt())
                Toast.makeText(this, "Изменено количество товара на ${countProduct.text.toString().toInt()}", Toast.LENGTH_SHORT).show()
            }
        }
        buttonWishlist.setOnClickListener{
            if (inWishlist){
                buttonWishlist.setImageResource(R.drawable.baseline_favorite_border_24)
                db.deleteFromWishlist(authId, idProduct)
                inWishlist = !inWishlist
                Toast.makeText(this, getString(R.string.deleteFromWishlist), Toast.LENGTH_SHORT).show()
            }
            else{
                buttonWishlist.setImageResource(R.drawable.baseline_favorite_24)
                db.addToWishlist(authId, idProduct)
                inWishlist = !inWishlist
                Toast.makeText(this, getString(R.string.addToWishlist), Toast.LENGTH_SHORT).show()
            }
        }
        buttonBack.setOnClickListener {
            onBackPressed()
        }
    }
}