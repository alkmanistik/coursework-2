package com.example.courcework

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteDatabase.CursorFactory
import android.database.sqlite.SQLiteOpenHelper
import java.util.Date

class ShopDataBaseHelper(val context: Context, val factory: CursorFactory?):
    SQLiteOpenHelper(context, "shopdb", factory, 1) {
    override fun onCreate(db: SQLiteDatabase?) {
        for (query in queryShopDB) {
            db!!.execSQL(query)
        }
    }

    override fun onUpgrade(db: SQLiteDatabase?, p1: Int, p2: Int) {
        for (table in tables){
            db!!.execSQL("DROP TABLE IF EXISTS $table")
        }
        onCreate(db)
    }

    fun execSQL(query: String){
        val db = this.writableDatabase
        db.execSQL(query)
    }

    fun deleteUser(idUser: Int){
        val db = this.writableDatabase
        db.execSQL("DELETE FROM Wishlist WHERE id_user = $idUser;")
        db.execSQL("DELETE FROM Users WHERE id = $idUser;")
    }

    fun addUser(login: String, email: String, password: String){
        val values = ContentValues()
        values.put("login", login)
        values.put("email", email)
        values.put("password", password)

        val db = this.writableDatabase
        db.insert(tables[3], null, values)
        db.close()

    }

    fun getUser(login: String, password: String): Int{
        val db = this.readableDatabase
        val result = db.rawQuery("SELECT * FROM ${tables[3]} WHERE login = '$login' AND password = '$password' or email = '$login' AND password = '$password'", null)
        var temp = 0
        if (result.moveToFirst()){
            temp = result.getInt(result.getColumnIndexOrThrow("id"))
        }
        db.close()
        result.close()
        return temp
    }

    fun getCheckUser(login: String, email: String):Boolean{
        val db = this.readableDatabase
        val result = db.rawQuery("SELECT * FROM ${tables[3]} WHERE login = '$login' or email = '$email'", null)
        val temp = result.moveToFirst()
        db.close()
        result.close()
        return temp
    }

    private fun listUser(result: Cursor): User {
        if (result.moveToFirst()) {
            do {
                val userId = result.getInt(result.getColumnIndexOrThrow("id"))
                val userLogin = result.getString(result.getColumnIndexOrThrow("login"))
                val userEmail = result.getString(result.getColumnIndexOrThrow("email"))
                val userPassword = result.getString(result.getColumnIndexOrThrow("password"))
                val userNickname = if (result.getString(result.getColumnIndexOrThrow("nickname")) != null) result.getString(result.getColumnIndexOrThrow("nickname")) else ""
                val userPhone = if (result.getString(result.getColumnIndexOrThrow("phone")) != null) result.getString(result.getColumnIndexOrThrow("phone")) else ""
                val userSex = if (result.getString(result.getColumnIndexOrThrow("sex")) != null) result.getString(result.getColumnIndexOrThrow("sex")) else ""
                val userAddress = if (result.getString(result.getColumnIndexOrThrow("address")) != null) result.getString(result.getColumnIndexOrThrow("address")) else ""
                val userImage = if (result.getBlob(result.getColumnIndexOrThrow("image")) != null) result.getBlob(result.getColumnIndexOrThrow("image")) else byteArrayOf()

                return User(
                    userId, userLogin, userEmail,
                    userPassword, userNickname, userPhone,
                    userSex, userAddress, userImage
                )
            } while (result.moveToNext())
        }
        return User()
    }

    fun getUserData(login: String, password: String): User {
        val db = this.readableDatabase
        val result = db.rawQuery(
            "SELECT * FROM ${tables[3]} WHERE login = '$login' AND password = '$password' or email = '$login' AND password = '$password'",
            null
        )
        return listUser(result)
    }

    fun getUserData(id: Int): User {
        val db = this.readableDatabase
        val result = db.rawQuery(
            "SELECT * FROM ${tables[3]} WHERE id = $id",
            null
        )
        return listUser(result)
    }

    fun getProductWithId(id: Int):ArrayList<Product>{
        val db = this.readableDatabase
        val result = db.rawQuery("SELECT * FROM ${com.example.courcework.tables[1]} WHERE id = ${id}", null)

        return listProduct(result)
    }

    fun deletePurchases(idPurchases: Int){
        execSQL("DELETE FROM Purchases WHERE id = $idPurchases; DELETE FROM Log WHERE id_purchases = $idPurchases;")
    }

    private fun listProduct(result: Cursor):ArrayList<Product>{
        val productData: ArrayList<Product> = arrayListOf()
        if (result.moveToFirst()){
            do {
                val productId = result.getInt(result.getColumnIndexOrThrow("id"))
                val productFull = result.getString(result.getColumnIndexOrThrow("full_name"))
                val productShort = result.getString(result.getColumnIndexOrThrow("short_name"))
                val productCategory = result.getString(result.getColumnIndexOrThrow("category"))
                val productDescription = result.getString(result.getColumnIndexOrThrow("description"))
                val productPrice = result.getInt(result.getColumnIndexOrThrow("price"))
                val productImage = result.getString(result.getColumnIndexOrThrow("image"))

                productData.add(Product(productId, productFull, productShort,
                    productCategory, productDescription,productPrice, productImage))

            } while (result.moveToNext())
        }
        return productData
    }

    fun getProduct(category: String = "", sort: String = "", sortingBy: String = "ASC"):ArrayList<Product>{
        val db = this.readableDatabase
        var newCategory = ""
        var newSort = ""
        if (category != ""){
            newCategory = " WHERE category = '$category'"
        }
        if (sort != ""){
            newSort = " ORDER BY $sort $sortingBy"
        }
        val query = "SELECT * FROM ${com.example.courcework.tables[1]}" + newCategory + newSort
        val result = db.rawQuery(query, null)
        return listProduct(result)
    }

    fun getUserWishlist(id: Int):ArrayList<ProductBasket>{
        val db = this.readableDatabase
        val result = db.rawQuery("SELECT Product.*, 0 AS purchases_id FROM Product INNER JOIN (SELECT * FROM Wishlist WHERE Wishlist.id_user = $id) as UserWishlist ON UserWishlist.id_product = Product.id",null)
        return listProductBasket(result, 0,"WISH")
    }

    fun addToWishlist(idUser: Int, idProduct: Int){
        val query = "INSERT INTO ${tables[4]} (id_user, id_product) VALUES ($idUser, $idProduct);"
        execSQL(query)
    }

    fun deleteFromWishlist(idUser: Int, idProduct: Int){
        val query = "DELETE FROM ${tables[4]} WHERE id_user = $idUser AND id_product = $idProduct"
        execSQL(query)
    }

    fun getWish(idUser: Int, idProduct: Int): Boolean{
        val db = this.readableDatabase
        val result = db.rawQuery("SELECT * FROM ${tables[4]} WHERE id_user = $idUser AND id_product = $idProduct", null)
        val temp: Boolean = result.moveToFirst()
        db.close()
        result.close()
        return temp
    }

    fun getProductWithStatus(id: Int, status: String):ArrayList<ProductBasket>{
        val db = this.readableDatabase
        val result = db.rawQuery("SELECT Product.*, Purchases.amount, Purchases.id as purchases_id FROM Product,Purchases, (SELECT id_purchases, MAX(date) AS date, status FROM Log GROUP BY id_purchases) as LastLog WHERE Purchases.id_user = $id AND Product.id = Purchases.id_product AND LastLog.status = '$status' AND Purchases.id = LastLog.id_purchases",null)
        return listProductBasket(result, status)
    }

    private fun listProductBasket(result: Cursor, status: String):ArrayList<ProductBasket>{
        val productData: ArrayList<ProductBasket> = arrayListOf()
        if (result.moveToFirst()){
            do {
                val productId = result.getInt(result.getColumnIndexOrThrow("id"))
                val productFull = result.getString(result.getColumnIndexOrThrow("full_name"))
                val productShort = result.getString(result.getColumnIndexOrThrow("short_name"))
                val productCategory = result.getString(result.getColumnIndexOrThrow("category"))
                val productDescription = result.getString(result.getColumnIndexOrThrow("description"))
                val productPrice = result.getInt(result.getColumnIndexOrThrow("price"))
                val productImage = result.getString(result.getColumnIndexOrThrow("image"))
                val productAmount = result.getInt(result.getColumnIndexOrThrow("amount"))
                val purchasesId = result.getInt(result.getColumnIndexOrThrow("purchases_id"))

                productData.add(ProductBasket(productId, productFull, productShort,
                    productCategory, productDescription,productPrice, productImage, productAmount, status, purchasesId))

            } while (result.moveToNext())
        }
        return productData
    }

    private fun listProductBasket(result: Cursor, amount: Int, status: String):ArrayList<ProductBasket>{
        val productData: ArrayList<ProductBasket> = arrayListOf()
        if (result.moveToFirst()){
            do {
                val productId = result.getInt(result.getColumnIndexOrThrow("id"))
                val productFull = result.getString(result.getColumnIndexOrThrow("full_name"))
                val productShort = result.getString(result.getColumnIndexOrThrow("short_name"))
                val productCategory = result.getString(result.getColumnIndexOrThrow("category"))
                val productDescription = result.getString(result.getColumnIndexOrThrow("description"))
                val productPrice = result.getInt(result.getColumnIndexOrThrow("price"))
                val productImage = result.getString(result.getColumnIndexOrThrow("image"))

                productData.add(ProductBasket(productId, productFull, productShort,
                    productCategory, productDescription,productPrice, productImage, amount, status))

            } while (result.moveToNext())
        }
        return productData
    }

    fun productToBasket(idUser: Int, idProduct: Int, amount: Int){
        val db = this.writableDatabase
        var idPurchases = 1
        val date = Date()
        val result = db.rawQuery("SELECT id FROM Purchases ORDER BY id DESC LIMIT 1", null)
        if (result.moveToFirst()){
            idPurchases += result.getInt(result.getColumnIndexOrThrow("id"))
        }
        db.execSQL("INSERT INTO Purchases (id ,id_user, id_product, amount) VALUES ($idPurchases, $idUser, $idProduct, $amount)")
        db.execSQL("INSERT INTO Log (id_purchases, date, status) VALUES ($idPurchases, '${dateFormat.format(date)}', '${status[0]}')")
    }

    fun checkProductInBasket(idUser: Int, idProduct: Int): Int{
        val db = this.readableDatabase
        val result = db.rawQuery("SELECT Purchases.id FROM Purchases, (SELECT id_purchases, MAX(date) AS date, status FROM Log GROUP BY id_purchases) as LastLog WHERE Purchases.id_user = $idUser AND Purchases.id_product = $idProduct AND LastLog.status = '${status[0]}' AND Purchases.id = LastLog.id_purchases",null)
        return if (result.moveToFirst()){
            result.getInt(result.getColumnIndexOrThrow("id"))
        }
        else 0
    }
    fun changeAmountInBasket(idPurchases: Int, amount: Int){
        execSQL("UPDATE Purchases SET amount = $amount WHERE id = $idPurchases")
    }

    fun getAmountAndPrice(idUser:Int):ArrayList<Int>{
        val db = this.readableDatabase
        val result = db.rawQuery("SELECT SUM(Purchases.amount) AS amount, SUM(Purchases.amount * Product.price) AS sum FROM Product,Purchases, (SELECT id_purchases, MAX(date) AS date, status FROM Log GROUP BY id_purchases) as LastLog WHERE Purchases.id_user = $idUser AND Product.id = Purchases.id_product AND LastLog.status = '${status[0]}' AND Purchases.id = LastLog.id_purchases GROUP BY Purchases.id_user",null)
        return if (result.moveToFirst()){
            val amount = result.getInt(result.getColumnIndexOrThrow("amount"))
            val sum = result.getInt(result.getColumnIndexOrThrow("sum"))
            arrayListOf<Int>(amount, sum)
        } else{
            arrayListOf<Int>(0,0)
        }
    }

    fun insertNewLog(idUser: Int, statusNow: String, idPurchases: Int = 0){
        val db = this.writableDatabase
        if (statusNow == status[1]){
            val result = db.rawQuery("SELECT Purchases.id AS id FROM Product,Purchases, (SELECT id_purchases, MAX(date) AS date, status FROM Log GROUP BY id_purchases) as LastLog WHERE Purchases.id_user = $idUser AND Product.id = Purchases.id_product AND LastLog.status = '${status[0]}' AND Purchases.id = LastLog.id_purchases", null)
            val date = Date()
            if (result.moveToFirst()){
                do {
                    db.execSQL("INSERT INTO Log (id_purchases, date, status) VALUES (${result.getInt(result.getColumnIndexOrThrow("id"))}, '${dateFormat.format(date)}', '${statusNow}')")
                } while (result.moveToNext())
            }
        }
        else if (statusNow == status[2]){
            val date = Date()
            db.execSQL("INSERT INTO Log (id_purchases, date, status) VALUES ($idPurchases, '${dateFormat.format(date)}', '${statusNow}')")
        }

    }

    fun getDatePurchases(idPurchases: Int): String{
        val db = this.readableDatabase
        var dateTime: String = ""
        val result = db.rawQuery("SELECT date FROM Log WHERE id_purchases = ${idPurchases} ORDER BY date DESC LIMIT 1;", null)
        if (result.moveToFirst()){
            dateTime = result.getString(result.getColumnIndexOrThrow("date"))
        }
        return dateTime
    }

    fun getAmountPurchases(idPurchases: Int): Int{
        val db = this.readableDatabase
        var amount: Int = 0
        val result = db.rawQuery("SELECT amount FROM Purchases WHERE id = $idPurchases", null)
        if (result.moveToFirst()){
            amount = result.getInt(result.getColumnIndexOrThrow("amount"))
        }
        return amount
    }
}
