package com.example.courcework

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    private lateinit var savePreferences: SharedPreferences


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        savePreferences = getSharedPreferences("savePreferences", MODE_PRIVATE)

        val login: EditText = findViewById(R.id.textLogin)
        val password: EditText = findViewById(R.id.textPassword)
        val buttonLog: Button = findViewById(R.id.buttonLog)
        val textSwitch: TextView = findViewById(R.id.textSwitch)
        val db = ShopDataBaseHelper(this, null)

        (login as TextView).text = savePreferences.getString("authName", "")
        (password as TextView).text = savePreferences.getString("authPassword", "")
        if (savePreferences.getBoolean("behaviourToAccess", false)){
            val userLoginOrEmail = login.text.toString().trim()
            val userPassword = password.text.toString().trim()
            val isAuth = db.getUser(userLoginOrEmail, userPassword)
            if (isAuth != 0){
                val intent = Intent(this, MenuActivity::class.java)
                startActivity(intent)
            }
        }


        var tempLogin: String? = intent.getStringExtra("login")
        var tempPassword: String? = intent.getStringExtra("password")
        if (tempLogin == null){
            tempLogin = ""
            tempPassword = ""
        }

        if (tempLogin != "") {
            (login as TextView).text = tempLogin
            (password as TextView).text = tempPassword
        }

        textSwitch.setOnClickListener{
            val intent = Intent(this, RegActivity::class.java)
            startActivity(intent)
        }

        buttonLog.setOnClickListener{
            val userLoginOrEmail = login.text.toString().trim()
            val userPassword = password.text.toString().trim()

            if (userLoginOrEmail == "" || userPassword == ""){
                Toast.makeText(this, getString(R.string.warningNotFullData), Toast.LENGTH_SHORT).show()
            }
            else{
                val isAuth = db.getUser(userLoginOrEmail, userPassword)

                if (isAuth != 0){

                    val editSave: SharedPreferences.Editor = savePreferences.edit()
                    editSave.putString("authName",userLoginOrEmail)
                    editSave.putString("authPassword",userPassword)
                    editSave.putInt("authId",isAuth)
                    editSave.putBoolean("behaviourToAccess", true)
                    editSave.apply()

                    val intent = Intent(this, MenuActivity::class.java)
                    startActivity(intent)
                }
                else{
                    Toast.makeText(this, getString(R.string.warningNotHaveThisUser), Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    @Deprecated("Deprecated in Java",
        ReplaceWith("super.onBackPressed()", "androidx.appcompat.app.AppCompatActivity")
    )
    override fun onBackPressed() {
        super.onBackPressed()
        finishAffinity()
    }
}