package com.example.bookstore.activity

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.widget.Toolbar
import com.example.bookstore.R
import com.example.bookstore.model.UserList
import com.example.bookstore.util.DatabaseHelper

class EditProfile : AppCompatActivity() {

    private lateinit var saveButton: Button
    private lateinit var editName: TextView
    private lateinit var editEmail: TextView
    private lateinit var editAddress: TextView
    private lateinit var editPassword: TextView
    private lateinit var myDb: DatabaseHelper
    private lateinit var toolbar: Toolbar


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_profile)

        myDb = DatabaseHelper(this)
        toolbar = findViewById(R.id.toolBar)
        editName = findViewById(R.id.edit_name)
        editEmail = findViewById(R.id.edit_email)
        editAddress = findViewById(R.id.edit_address)
        editPassword = findViewById(R.id.edit_password)
        saveButton = findViewById(R.id.save)

        toolbarInitialize()
        inputListener()

        val i: Intent = intent
        val name:String? = i.getStringExtra("name")
        val email:String? = i.getStringExtra("email")
        val address:String? = i.getStringExtra("address")
        val password:String? = i.getStringExtra("password")
        val id:Int = i.getIntExtra("id",0)

        editName.text = name
        editEmail.text = email
        editAddress.text = address
        editPassword.text = password
        saveButton.setOnClickListener(View.OnClickListener {
            if (editName.text.toString().isNotEmpty() && editEmail.text.toString().isNotEmpty()) {
                val result: Boolean = myDb.updateUser(id,editName.text.toString(), editEmail.text.toString(), editAddress.text.toString(),editPassword.text.toString())
                if (result) {
                    val sp = getSharedPreferences("LoginActivity", Context.MODE_PRIVATE)
                    val editor: SharedPreferences.Editor = sp.edit()
                    editor.putString("id",editName.text.toString())
                    editor.apply()
                    Toast.makeText(this@EditProfile, "updated successful", Toast.LENGTH_SHORT).show()
                    //var userList: UserList = myDb.getAllUser()
                }
            }
        })

    }

    private fun toolbarInitialize() {
        setSupportActionBar(toolbar)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.setDisplayShowHomeEnabled(true)

        toolbar.setNavigationOnClickListener{finish()}

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
}
