package com.example.courcework

import android.content.Context
import android.icu.text.SimpleDateFormat
import android.widget.Toast


val tables = listOf<String>("Log","Product", "Purchases", "Users", "Wishlist")
val status = listOf<String>("WAIT", "DELIVERY", "DONE", "WISH")
val sortingBy = listOf<String>("ASC", "DESC")
val sort = listOf<String>("","short_name", "price")
val dateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
const val posted_by = "000"
val addresses: Array<String> = arrayOf("example@gmail.com")
const val subject = "Обращение в тех. поддержку: "
const val maxAmount = 99
const val minLength = 8
const val maxLength = 20

val queryShopDB = listOf<String>("CREATE TABLE IF NOT EXISTS Log (id_purchases INTEGER NOT NULL, date NUMERIC NOT NULL, status TEXT NOT NULL);",
    "CREATE TABLE IF NOT EXISTS Product (id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, full_name TEXT NOT NULL, short_name TEXT NOT NULL, category TEXT NOT NULL, description TEXT, price INTEGER NOT NULL, image TEXT);",
    "INSERT INTO Product (id, full_name, short_name, category, description, price, image) VALUES (1, 'Голубой модульный диван подставка', 'Модульный диван', 'Диваны', 'Качественный диван, который обеспечит вам не только получить удовольствие от сна, но и от обычной активности, диван разбирается в столик и содержит модульные хранилища для вещей.', 46000, 'BlueSofaModul');",
    "INSERT INTO Product (id, full_name, short_name, category, description, price, image) VALUES (2, 'Серый диван в стиле LOFT', 'LOFT диван', 'Диваны', 'Удобный разбираемый диван для гостевых комнат, стиль дивана увеличивает удовлетворение от комнатной обстановки, и помогает отдохнуть от ежедневной рутины', 35000, 'GreySofaLoft');",
    "INSERT INTO Product (id, full_name, short_name, category, description, price, image) VALUES (3, 'Оранжевый диван в стиле LOFT', 'LOFT диван', 'Диваны', 'Удобный разбираемый диван для гостевых комнат, стиль дивана увеличивает удовлетворение от комнатной обстановки, и помогает отдохнуть от ежедневной рутины', 30000, 'OrangeSofaLoft');",
    "INSERT INTO Product (id, full_name, short_name, category, description, price, image) VALUES (4, 'Жёлтый стул на деревянных ножках', 'Жёлтый стул', 'Стулья', 'Стильный жёлтый стул для ваших интерьеров из дерева или других материалов, подходит для обеденных зон.', 5300, 'YellowChair');",
    "INSERT INTO Product (id, full_name, short_name, category, description, price, image) VALUES (5, 'Набор стульев в стиле чёрно-белый', 'Набор стульев', 'Стулья', 'Контастные стулья для людей любищие символизм не только в искусстве, но и в интерьере. Удобные обивки и легко стираемое покрытие обеспечиватся хорошим покрытием', 15890, 'CoupleChair');",
    "INSERT INTO Product (id, full_name, short_name, category, description, price, image) VALUES (6, 'Серый дизайнерский стул на деревянных ножках', 'Дизайнерский стул', 'Стулья', 'Дизайнерский стул для ваших зон отдыха, подходит для тех, кто отдыхает идеями. Сделан стул известным дизайнером Джонам Кемеди.', 8000, 'GreyChair');",
    "INSERT INTO Product (id, full_name, short_name, category, description, price, image) VALUES (7, 'Голубой дизайнерский стул на пластиковых ножках', 'Дизайнерский стул', 'Стулья', 'Классический стул для ваших идей и ваших комнат. Цвет стула подходит для большинства интерьеров, а удобные ножки обеспичивают лёгкость самих стульев.', 6700, 'BlueChair');",
    "INSERT INTO Product (id, full_name, short_name, category, description, price, image) VALUES (8, 'Белые подушки с эффектом запоминания из органического материала', 'Белые подушки', 'Аксессуары', 'Lorem ipsum dolor sit amet, consectetuer adipiscing elit, sed diam nonummy nibh euismod tincidunt ut laoreet dolore magna aliquam erat', 4300, 'WhitePillows');",
    "INSERT INTO Product (id, full_name, short_name, category, description, price, image) VALUES (9, 'Перьевые подушки', 'Перьевые подушки', 'Аксессуары', 'Lorem ipsum dolor sit amet, consectetuer adipiscing elit, sed diam nonummy nibh euismod tincidunt ut laoreet dolore magna aliquam erat', 1500, 'BluePillows');",
    "INSERT INTO Product (id, full_name, short_name, category, description, price, image) VALUES (10, 'Дизайнерский бежевый горшок для ваших интерьеров', 'Дизайнерский горшок', 'Аксессуары', 'Lorem ipsum dolor sit amet, consectetuer adipiscing elit, sed diam nonummy nibh euismod tincidunt ut laoreet dolore magna aliquam erat', 970, 'GreyPot');",
    "INSERT INTO Product (id, full_name, short_name, category, description, price, image) VALUES (11, 'Коричневые стандартные горшки для цветов', 'Коричневый горшок', 'Аксессуары', 'Lorem ipsum dolor sit amet, consectetuer adipiscing elit, sed diam nonummy nibh euismod tincidunt ut laoreet dolore magna aliquam erat', 250, 'StandartPot');",
    "INSERT INTO Product (id, full_name, short_name, category, description, price, image) VALUES (12, 'Кофейный дизайнерский столик с одной ножкой', 'Кофейный столик', 'Столы', 'Lorem ipsum dolor sit amet, consectetuer adipiscing elit, sed diam nonummy nibh euismod tincidunt ut laoreet dolore magna aliquam erat', 23000, 'CoffeeTable');",
    "INSERT INTO Product (id, full_name, short_name, category, description, price, image) VALUES (13, 'Кофейный деревянный столик для любых интерьеров', 'Кофейный столик', 'Столы', 'Lorem ipsum dolor sit amet, consectetuer adipiscing elit, sed diam nonummy nibh euismod tincidunt ut laoreet dolore magna aliquam erat', 31000, 'CoffeeTableWood');",
    "INSERT INTO Product (id, full_name, short_name, category, description, price, image) VALUES (14, 'Обеденный деревянный стол с функцией удлинения', 'Деревянный стол', 'Столы', 'Lorem ipsum dolor sit amet, consectetuer adipiscing elit, sed diam nonummy nibh euismod tincidunt ut laoreet dolore magna aliquam erat', 56900, 'StandartTable');",
    "CREATE TABLE IF NOT EXISTS Purchases (id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, id_user INTEGER NOT NULL, id_product INTEGER NOT NULL, amount INTEGER NOT NULL);",
    "CREATE TABLE IF NOT EXISTS Users (id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, login TEXT NOT NULL UNIQUE, email TEXT NOT NULL UNIQUE, password TEXT NOT NULL, nickname TEXT, phone TEXT, sex TEXT, address TEXT, image BLOB);",
    "CREATE TABLE IF NOT EXISTS Wishlist (id_user NOT NULL, id_product NOT NULL);")

fun limitation(min:Int, max:Int, value:Int): Int{
    return if(value < min)
        min
    else if(value > max)
        max
    else
        value
}

fun checkLog(context:Context, text:String): Boolean{
    if (text.length < minLength){
        Toast.makeText(context, context.getString(R.string.warningMinLength,context.getString(R.string.log), minLength), Toast.LENGTH_LONG).show()
        return false
    }
    if (text.length > maxLength){
        Toast.makeText(context, context.getString(R.string.warningMaxLength,context.getString(R.string.log), maxLength), Toast.LENGTH_LONG).show()
        return false
    }
    return true
}
fun checkEmail(context:Context, text:String): Boolean{
    return true
}
fun checkPas(context:Context, text:String): Boolean{
    if (text.length < minLength){
        Toast.makeText(context, context.getString(R.string.warningMinLength,context.getString(R.string.pas), minLength), Toast.LENGTH_LONG).show()
        return false
    }
    if (text.length > maxLength){
        Toast.makeText(context, context.getString(R.string.warningMaxLength,context.getString(R.string.pas), maxLength), Toast.LENGTH_LONG).show()
        return false
    }
    return true
}

enum class ProductCategory(private val displayName: String) {
    SOFA("Диваны"),
    CHAIR("Стулья"),
    TABLE("Столы"),
    ACCESSORIES("Аксессуары");

    override fun toString(): String {
        return displayName
    }
}