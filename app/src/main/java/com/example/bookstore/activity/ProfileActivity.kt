package com.example.bookstore.activity

import android.Manifest
import android.app.Activity
import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.ImageDecoder
import android.graphics.drawable.BitmapDrawable
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.util.Base64
import android.util.Base64.DEFAULT
import android.view.Gravity
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.example.bookstore.R
import com.example.bookstore.model.UserList
import com.example.bookstore.util.DatabaseHelper
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_profile.*
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.IOException
import java.nio.charset.StandardCharsets
import java.nio.file.Files


class ProfileActivity : AppCompatActivity() {

    var REQUEST_CAMERA: Int = 1
    var SELECT_FILE: Int = 0
    var PERMISSION_CODE = 1000
    var imguri: Uri? = null
    lateinit var myDb: DatabaseHelper
    lateinit var userList: UserList


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)

        initializeView()
        toolbarInitialize()
        profileInitialize()
        buttonClickListener()
    }

    private fun toolbarInitialize() {
        setSupportActionBar(toolBar)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.setDisplayShowHomeEnabled(true)

        toolBar.setNavigationOnClickListener { finish() }

    }

    private fun initializeView() {

        myDb = DatabaseHelper(this)

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.profile_edit, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.edit -> {
                val i = Intent(this@ProfileActivity, EditProfile::class.java)
                i.putExtra("name", edit_name.text.toString())
                i.putExtra("email",edit_email.text.toString())
                i.putExtra("address",edit_address.text.toString())
                i.putExtra("password",userList.userPassword)
                i.putExtra("id",myDb.getUsId(userList.userName))
                startActivity(i)
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun profileInitialize() {
        val sh = getSharedPreferences("LoginActivity", Context.MODE_PRIVATE)
        val name = sh.getString("id", null)
        val circleImage = CircleImage()
        userList = myDb.getUser(name)

        edit_name.setText(userList.userName)
        edit_email.setText(userList.userEmail)
        edit_address.setText(userList.userAddress)
        user_photo.setImageBitmap(circleImage.transform((bitImage(userList.userImage))))

    }

    private fun bitImage(userImage: ByteArray): Bitmap? {
        val bitmap = BitmapFactory.decodeByteArray(userImage, 0, userImage.size)

        return bitmap
    }


    private fun buttonClickListener() {

        user_photo.setOnClickListener(View.OnClickListener {
            onSelectImage()
        })

    }


    private fun onSelectImage() {
        val items = arrayOf<CharSequence>("Camera", "Gallery", "Cancel")

        val builder = AlertDialog.Builder(this)
        builder.setTitle("Add Image")
                .setItems(items) { dialog, which ->
                    if (items[which] == "Camera") {
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                            if (checkSelfPermission(Manifest.permission.CAMERA) == PackageManager.PERMISSION_DENIED ||
                                    checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED) {
                                val permission = arrayOf(Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                                requestPermissions(permission, PERMISSION_CODE)
                            } else {
                                openCamera()
                            }
                        } else {
                            openCamera()
                        }
                    } else if (items[which] == "Gallery") {
                        val intent = Intent()
                        intent.type = "image/*"
                        intent.action = Intent.ACTION_GET_CONTENT
                        startActivityForResult(Intent.createChooser(intent, "Select Picture"), SELECT_FILE)
                    } else if (items[which] == "Cancel") {
                        dialog.dismiss()
                    }
                }.show()
    }

    private fun openCamera() {
        val values = ContentValues()
        values.put(MediaStore.Images.Media.TITLE, "New Picture")
        values.put(MediaStore.Images.Media.DESCRIPTION, "From the Camera")
        imguri = contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values)

        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        intent.putExtra(MediaStore.EXTRA_OUTPUT, imguri)
        startActivityForResult(intent, REQUEST_CAMERA)
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String?>, grantResults: IntArray) {
        when (requestCode) {
            PERMISSION_CODE -> if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                openCamera()
            } else {
                val toast = Toast.makeText(this, "Permission Denied", Toast.LENGTH_SHORT)
                toast.setGravity(Gravity.CENTER, 0, 0)
                toast.show()
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == REQUEST_CAMERA) {
                // imageView.setImageURI(imguri)
                //Picasso.with(this).load(imguri).transform(CircleImage()).into(user_photo)
                //myDb.addImage(drawableToByte(user_photo.drawable),userList.userName)

            } else if (requestCode == SELECT_FILE) {
                val selectedImageUri = data?.data
                try {
                    selectedImageUri?.let {
                        if (Build.VERSION.SDK_INT < 28) {
                            val bitmap = MediaStore.Images.Media.getBitmap(
                                    this.contentResolver,
                                    selectedImageUri
                            )
                            //imageView.setImageBitmap(bitmap)
                            Picasso.with(this).load(selectedImageUri).transform(CircleImage()).into(user_photo)
                            //val base64ImageString = encoder(selectedImageUri.path!!)
                            myDb.addImage(drawableToByte(bitmap), userList.userName)
                        } else {
                            val source = ImageDecoder.createSource(this.contentResolver, selectedImageUri)
                            val bitmap = ImageDecoder.decodeBitmap(source)
                            //imageView.setImageBitmap(bitmap)
                            Picasso.with(this).load(selectedImageUri).transform(CircleImage()).into(user_photo)
                            //val base64ImageString = encoder(selectedImageUri.path!!)
                            myDb.addImage(drawableToByte(bitmap), userList.userName)
                        }

                    }
                } catch (e: IOException) {
                    e.printStackTrace()
                }
            }
        }
    }

    /*private fun encoder(filePath: String): String{
        val bytes = File(filePath).readBytes()
        return Base64.encodeToString(bytes,DEFAULT)
    }*/
    private fun drawableToByte(drawable: Bitmap): ByteArray {
        //val bitmap = (drawable as? BitmapDrawable)?.bitmap
        val stream = ByteArrayOutputStream()
        drawable.compress(Bitmap.CompressFormat.JPEG, 70, stream)
        return stream.toByteArray()
        //return Base64.decode(drawable, DEFAULT)
    }
}
