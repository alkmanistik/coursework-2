package com.example.courcework

import android.content.Intent
import android.graphics.BitmapFactory
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import com.example.courcework.databinding.ActivityMenuBinding
import com.example.courcework.databinding.ActivityProfileChangeBinding

class ProfileChangeActivity : AppCompatActivity() {

    private lateinit var binding: ActivityProfileChangeBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProfileChangeBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.buttonBack.setOnClickListener{
            onBackPressed()
        }

        val id = intent.getIntExtra("id", 1)
        val db = ShopDataBaseHelper(this, null)
        val userData = db.getUserData(id)
        var tempSex = userData.sex as String
        (binding.nameProfile as TextView).text = userData.nickname as String
        (binding.telephoneProfile as TextView).text = userData.phone as String
        (binding.addressProfile as TextView).text = userData.address as String
        if (!userData.image.contentEquals(byteArrayOf())){
            binding.imageProfile.setImageBitmap(BitmapFactory.decodeByteArray(userData.image, 0, userData.image.size))
        }

        fun changeRadioButton(id: String) {
            when (id) {
                "Мужчина" -> binding.radioButton2.isChecked = true
                "Женщина" -> binding.radioButton3.isChecked = true
                else -> binding.radioButton.isChecked = true
            }
        }

        changeRadioButton(tempSex)

        binding.radioGroup.setOnCheckedChangeListener{
                _, checkedId ->
            tempSex = if (checkedId == R.id.radioButton2) {
                "Мужчина"
            } else if (checkedId == R.id.radioButton3) {
                "Женщина"
            } else{
                ""
            }
        }

        binding.buttonChangePhoto.setOnClickListener {

        }

        binding.buttonSave.setOnClickListener{
//            if ((userData[4] == binding.nameProfile.text) and (userData[5] == binding.telephoneProfile.text)
//                and (userData[7] == binding.addressProfile.text) and ()){
//
//            }
            val nm = if ((binding.nameProfile as TextView).text != "") "'${binding.nameProfile.text.toString().trim()}'" else "NULL"
            val ph = if ((binding.telephoneProfile as TextView).text != "") "'${binding.telephoneProfile.text.toString().trim()}'" else "NULL"
            val ad = if ((binding.addressProfile as TextView).text != "") "'${binding.addressProfile.text.toString().trim()}'" else "NULL"
            val sx = if (tempSex != "") "'${tempSex}'" else "NULL"
            val query = "UPDATE ${tables[3]} SET nickname = $nm, phone = $ph, address = $ad, sex = $sx WHERE id = $id"

            db.execSQL(query)

            Toast.makeText(this,getString(R.string.saveConfirm),Toast.LENGTH_SHORT).show()
//
            val intent = Intent(this, MenuActivity::class.java)
            intent.putExtra("page", "Profile")
            startActivity(intent)

        }

    }
}