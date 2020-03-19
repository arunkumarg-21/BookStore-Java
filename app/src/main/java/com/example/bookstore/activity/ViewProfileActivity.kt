package com.example.bookstore.activity

import android.Manifest
import android.app.Activity
import android.content.ContentValues
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.view.Gravity
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.example.bookstore.R
import com.example.bookstore.model.UserList
import com.example.bookstore.util.*
import kotlinx.android.synthetic.main.activity_profile.*
import java.io.ByteArrayOutputStream
import java.io.IOException


class ViewProfileActivity : AppCompatActivity() {

    private val requestCamera: Int = 1
    private val selectFile: Int = 0
    private val permissionCode = 1000
    private var imageUri: Uri? = null
    private lateinit var myDb: DatabaseHelper
    private lateinit var userList: UserList


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)

        initializeView()
        toolbarInitialize()
        profileInitialize()
        buttonClickListener()
        val params = appbar.layoutParams
        if(params.height == 3*80){
            change_user_photo.visibility = View.GONE
        }else{
            change_user_photo.visibility = View.VISIBLE
        }
    }

    private fun toolbarInitialize() {
        setSupportActionBar(toolBar)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.setDisplayShowHomeEnabled(true)
        toolBar.setNavigationOnClickListener {
            val intent = Intent(this@ViewProfileActivity, MainActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(intent)
        }
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
                val i = Intent(this@ViewProfileActivity, EditProfileActivity::class.java)
                i.putExtra("name", edit_name.text.toString())
                i.putExtra("email", edit_email.text.toString())
                i.putExtra("address", edit_address.text.toString())
                i.putExtra("password", userList.userPassword)
                i.putExtra("id", myDb.getUsId(userList.userName))
                startActivity(i)
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun profileInitialize() {

        val sh = SharedPreferenceHelper()
        val name = sh.getSharedName(applicationContext)
        userList = myDb.getUser(name)

        edit_name.setText(userList.userName)
        edit_email.setText(userList.userEmail)
        edit_address.setText(userList.userAddress)
        user_photo.setImageBitmap((bitImage(userList.userImage)))

    }

    private fun bitImage(userImage: ByteArray): Bitmap? {

        return BitmapFactory.decodeByteArray(userImage, 0, userImage.size)
    }


    private fun buttonClickListener() {

        user_photo.setOnClickListener {
            onSelectImage()
        }

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
                                requestPermissions(permission, permissionCode)
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
                        startActivityForResult(Intent.createChooser(intent, "Select Picture"), selectFile)
                    } else if (items[which] == "Cancel") {
                        dialog.dismiss()
                    }
                }.show()
    }

    private fun openCamera() {
        val values = ContentValues()
        values.put(MediaStore.Images.Media.TITLE, "New Picture")
        values.put(MediaStore.Images.Media.DESCRIPTION, "From the Camera")
        imageUri = contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values)

        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri)
        startActivityForResult(intent, requestCamera)
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String?>, grantResults: IntArray) {
        when (requestCode) {
            permissionCode -> if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
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
            if (requestCode == requestCamera) {
                myDb.addImage(drawableToByte(MediaStore.Images.Media.getBitmap(this.contentResolver, imageUri)), userList.userName)
                user_photo.setImageBitmap(MediaStore.Images.Media.getBitmap(this.contentResolver, imageUri))

            } else if (requestCode == selectFile) {
                val selectedImageUri = data?.data
                try {
                    selectedImageUri?.let {
                        if (Build.VERSION.SDK_INT < 28) {
                            val bitmap = MediaStore.Images.Media.getBitmap(
                                    this.contentResolver,
                                    selectedImageUri
                            )
                            myDb.addImage(drawableToByte(bitmap), userList.userName)
                            user_photo.setImageBitmap((bitmap))
                        } else {
                            val source = ImageDecoder.createSource(this.contentResolver, selectedImageUri)
                            val bitmap = ImageDecoder.decodeBitmap(source)
                            myDb.addImage(drawableToByte(bitmap), userList.userName)
                            user_photo.setImageBitmap((bitmap))
                        }

                    }
                } catch (e: IOException) {
                    e.printStackTrace()
                }
            }
        }
    }

    private fun drawableToByte(drawable: Bitmap): ByteArray {
        val stream = ByteArrayOutputStream()
        drawable.compress(Bitmap.CompressFormat.JPEG, 70, stream)
        return stream.toByteArray()
    }
}