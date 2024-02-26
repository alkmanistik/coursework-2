package com.example.courcework

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import kotlin.math.log

class RegActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_reg)

        val login: EditText = findViewById(R.id.textLogin)
        val email: EditText = findViewById(R.id.textEmail)
        val password: EditText = findViewById(R.id.textPassword)
        val buttonLog: Button = findViewById(R.id.buttonLog)
        val textSwitch: TextView = findViewById(R.id.textSwitch)

        textSwitch.setOnClickListener{
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }

        buttonLog.setOnClickListener{
            val userLogin = login.text.toString().trim()
            val userEmail = email.text.toString().trim()
            val userPassword = password.text.toString().trim()

            if (userLogin == "" || userEmail == "" || userPassword == ""){
                Toast.makeText(this, getString(R.string.warningNotFullData), Toast.LENGTH_SHORT).show()
            }
            else{
                val db = ShopDataBaseHelper(this, null)
                val checkUser = db.getCheckUser(userLogin, userEmail)
                if (checkUser){
                    Toast.makeText(this, getString(R.string.warningAnotherUser), Toast.LENGTH_SHORT).show()
                }
                else{
                    db.addUser(userLogin, userEmail, userPassword)

                    Toast.makeText(this,"Был добавлен пользователь login:${login.text.toString().trim()}",Toast.LENGTH_SHORT).show()

                    val intent = Intent(this, MainActivity::class.java)
                    intent.putExtra("login", userLogin)
                    intent.putExtra("password", userPassword)
                    startActivity(intent)
                }
            }
        }
    }
    @Deprecated("Deprecated in Java",
        ReplaceWith("super.onBackPressed()", "androidx.appcompat.app.AppCompatActivity")
    )
    override fun onBackPressed() {
        super.onBackPressed()
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
    }
}