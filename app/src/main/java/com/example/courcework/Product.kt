package com.example.courcework

class Product(
    val idProduct: Int,
    val nameFull: String,
    val nameShort: String,
    val category: String,
    val description: String,
    val price: Int,
    val image: String,
) {
}

class ProductBasket(
    val idProduct: Int,
    val nameFull: String,
    val nameShort: String,
    val category: String,
    val description: String,
    val price: Int,
    val image: String,
    val amount: Int,
    val status: String,
    val purchasesId: Int = 0,
){
}