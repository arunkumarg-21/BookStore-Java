package com.example.bookstore.activity

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher
import android.util.Patterns
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.bookstore.R
import com.example.bookstore.util.DatabaseHelper
import com.example.bookstore.util.SharedPreferenceHelper
import kotlinx.android.synthetic.main.activity_edit_profile.*

class EditProfile : AppCompatActivity() {


    private lateinit var myDb: DatabaseHelper


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_profile)

        myDb = DatabaseHelper(this)

        toolbarInitialize()
        inputListener()

        val i: Intent = intent
        val name:String? = i.getStringExtra("name")
        val email:String? = i.getStringExtra("email")
        val address:String? = i.getStringExtra("address")
        val password:String? = i.getStringExtra("password")
        val id:Int = i.getIntExtra("id",0)

        edit_name.setText(name)
        edit_email.setText(email)
        edit_address.setText(address)
        edit_password.setText(password)
        save.setOnClickListener {
            if (edit_name.text.toString().isNotEmpty() && edit_email.text.toString().isNotEmpty()) {
                if (validEmail()) {
                    val result: Boolean = myDb.updateUser(id, edit_name.text.toString(), edit_email.text.toString(), edit_address.text.toString(), edit_password.text.toString())
                    if (result) {
                        val sp = SharedPreferenceHelper()
                        sp.initialize(applicationContext,edit_name.text.toString(),edit_password.text.toString())
                        Toast.makeText(this@EditProfile, "updated successful", Toast.LENGTH_SHORT).show()
                        val intent = Intent(this@EditProfile, ProfileActivity::class.java)
                        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
                        startActivity(intent)
                    }
                }
            }
        }
    }

    private fun toolbarInitialize() {
        setSupportActionBar(toolBar)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.setDisplayShowHomeEnabled(true)

        toolBar.setNavigationOnClickListener{finish()}

    }

    private fun inputListener() {
        edit_name.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {}
            override fun afterTextChanged(s: Editable) {
                if (edit_name.text.toString().isEmpty()) {
                    edit_name.error = "Cannot Be Null"
                } else {
                    edit_name.error = null

                }
            }
        })
        edit_email.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {

            }
            override fun afterTextChanged(s: Editable) {
                if (edit_email.text.toString().isEmpty()) {
                    edit_email.error = "Cannot Be Null"
                } else {
                    if (!TextUtils.isEmpty(edit_email.text.toString()) && Patterns.EMAIL_ADDRESS.matcher(edit_email.text.toString()).matches()) {
                        edit_email.error = null
                    } else {
                        edit_email.error = "Invalid Email"
                    }
                }

            }
        })
    }

    private fun validEmail(): Boolean {
        val target: CharSequence = edit_email.text.toString()
        return !TextUtils.isEmpty(target) && Patterns.EMAIL_ADDRESS.matcher(target).matches()
    }

}
