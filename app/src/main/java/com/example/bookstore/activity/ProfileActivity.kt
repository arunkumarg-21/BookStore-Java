package com.example.bookstore.activity

import android.Manifest
import android.app.Activity
import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.graphics.drawable.BitmapDrawable
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.Gravity
import android.view.View
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.example.bookstore.R
import com.example.bookstore.model.UserList
import com.example.bookstore.util.DatabaseHelper
import com.squareup.picasso.Picasso
import java.io.IOException



class ProfileActivity : AppCompatActivity() {

    private lateinit var imageView: ImageView
    var REQUEST_CAMERA: Int = 1
    var SELECT_FILE: Int = 0
    var PERMISSION_CODE = 1000
    var imguri: Uri? = null
    lateinit var linearLayout: LinearLayout
    lateinit var editButton: Button
    lateinit var saveButton: Button
    lateinit var textName: TextView
    lateinit var textEmail: TextView
    lateinit var textAddress: TextView
    lateinit var editName: TextView
    lateinit var editEmail: TextView
    lateinit var editAddress: TextView
    lateinit var myDb: DatabaseHelper
    lateinit var toolbar: Toolbar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)

        initializeView()
        toolbarInitialize()
        profileInitialize()
        inputListener()
        buttonClickListener()

    }

    private fun toolbarInitialize() {
        setSupportActionBar(toolbar)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.setDisplayShowHomeEnabled(true)

        toolbar.setNavigationOnClickListener{finish()}

    }

    private fun initializeView() {
        imageView = findViewById(R.id.user_photo)
        linearLayout = findViewById(R.id.ll2)
        editButton = findViewById(R.id.edit)
        saveButton = findViewById(R.id.save)
        textName = findViewById(R.id.text_name)
        textEmail = findViewById(R.id.text_email)
        textAddress = findViewById(R.id.text_address)
        editName = findViewById(R.id.edit_name)
        editEmail = findViewById(R.id.edit_email)
        editAddress = findViewById(R.id.edit_address)
        toolbar = findViewById(R.id.toolBar)
        myDb = DatabaseHelper(this)

        linearLayout.visibility = LinearLayout.GONE
    }

    private fun profileInitialize() {
        val sh = getSharedPreferences("LoginActivity", Context.MODE_PRIVATE)
        val name = sh.getString("id", null)

        val userList: UserList = myDb.getUser(name)

        textName.text = userList.userName
        textEmail.text = userList.userEmail
        textAddress.text = userList.userAddress
    }

    private fun inputListener() {
        editName.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {}
            override fun afterTextChanged(s: Editable) {
                if (editName.text.toString().isEmpty()) {
                    editName.error = "Cannot Be Null"
                } else {
                    editName.error = null

                }
            }
        })
        editEmail.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {}
            override fun afterTextChanged(s: Editable) {
                if (editEmail.text.toString().isEmpty()) {
                    editEmail.error = "Cannot Be Null"
                } else {
                    editEmail.error = null
                }
            }
        })
    }

    private fun buttonClickListener() {

        imageView.setOnClickListener(View.OnClickListener {
            onSelectImage()
        })

        editButton.setOnClickListener(View.OnClickListener {
            linearLayout.visibility = LinearLayout.VISIBLE
        })


        saveButton.setOnClickListener(View.OnClickListener {
            if (editName.text.toString().isNotEmpty() && editEmail.text.toString().isNotEmpty()) {
                val result: Boolean = myDb.updateUser(textName.text.toString(),editName.text.toString(), editEmail.text.toString(), editAddress.text.toString())
                if (result) {
                    val sp = getSharedPreferences("LoginActivity", Context.MODE_PRIVATE)
                    val editor: SharedPreferences.Editor = sp.edit()
                    editor.putString("id", editName.text.toString())
                    editor.apply()
                    Toast.makeText(this@ProfileActivity, "updated successful", Toast.LENGTH_SHORT).show()
                    var userList: UserList = myDb.getAllUser()
                }
            }
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
                Picasso.with(this).load(imguri).transform(CircleImage()).into(imageView)

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
                            Picasso.with(this).load(selectedImageUri).transform(CircleImage()).into(imageView)
                        } else {
                            val source = ImageDecoder.createSource(this.contentResolver, selectedImageUri)
                            val bitmap = ImageDecoder.decodeBitmap(source)
                            //imageView.setImageBitmap(bitmap)
                            Picasso.with(this).load(selectedImageUri).transform(CircleImage()).into(imageView)
                        }
                        /* val bitmap = MediaStore.Images.Media.getBitmap(contentResolver, selectedImageUri)
                         imageView.setImageBitmap(bitmap)*/
                    }
                } catch (e: IOException) {
                    e.printStackTrace()
                }
            }
        }
    }
}
