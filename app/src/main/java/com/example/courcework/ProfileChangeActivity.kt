package com.example.courcework

import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import com.example.courcework.databinding.ActivityMenuBinding
import com.example.courcework.databinding.ActivityProfileChangeBinding
import java.io.ByteArrayOutputStream

class ProfileChangeActivity : AppCompatActivity() {

    private lateinit var binding: ActivityProfileChangeBinding
    private val IMAGE_PICK_CODE = 1000
    private val PERMISSION_CODE = 1001
    private lateinit var imageProfileByteArray: ByteArray

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
        imageProfileByteArray = userData.image

        var tempSex = userData.sex as String
        (binding.nameProfile as TextView).text = userData.nickname as String
        (binding.telephoneProfile as TextView).text = userData.phone as String
        (binding.addressProfile as TextView).text = userData.address as String
        if (!imageProfileByteArray.contentEquals(byteArrayOf())){
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
            // Check for permissions
            if (checkSelfPermission(android.Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED) {
                val permissions = arrayOf(android.Manifest.permission.READ_EXTERNAL_STORAGE)
                requestPermissions(permissions, PERMISSION_CODE)
            } else {
                pickImageFromGallery()
            }
        }

        binding.buttonSave.setOnClickListener{
//            if ((userData[4] == binding.nameProfile.text) and (userData[5] == binding.telephoneProfile.text)
//                and (userData[7] == binding.addressProfile.text) and ()){
//
//            }
            val nm = if ((binding.nameProfile as TextView).text != "") "${binding.nameProfile.text.toString().trim()}" else "NULL"
            val ph = if ((binding.telephoneProfile as TextView).text != "") "${binding.telephoneProfile.text.toString().trim()}" else "NULL"
            val ad = if ((binding.addressProfile as TextView).text != "") "${binding.addressProfile.text.toString().trim()}" else "NULL"
            val sx = if (tempSex != "") "${tempSex}" else "NULL"
//            val query = "UPDATE ${tables[3]} SET nickname = $nm, phone = $ph, address = $ad, sex = $sx, image = $imageProfileByteArray WHERE id = $id"
//            db.execSQL(query)
            db.updateUser(id, nm, ph, ad, sx, imageProfileByteArray)

            Toast.makeText(this,getString(R.string.saveConfirm),Toast.LENGTH_SHORT).show()
//
            val intent = Intent(this, MenuActivity::class.java)
            intent.putExtra("page", "Profile")
            startActivity(intent)

        }

    }
    private fun pickImageFromGallery() {
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        startActivityForResult(intent, IMAGE_PICK_CODE)
    }
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when(requestCode){
            PERMISSION_CODE -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    pickImageFromGallery()
                } else {
                    // Permission from popup denied
                    Toast.makeText(this, "Permission denied", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == RESULT_OK && requestCode == IMAGE_PICK_CODE) {
            val imageUri: Uri? = data?.data
            binding.imageProfile.setImageURI(imageUri)

            // Convert image to BLOB
            val bitmap = MediaStore.Images.Media.getBitmap(this.contentResolver, imageUri)
            val stream = ByteArrayOutputStream()
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream)
            imageProfileByteArray = stream.toByteArray()


        }
    }
}