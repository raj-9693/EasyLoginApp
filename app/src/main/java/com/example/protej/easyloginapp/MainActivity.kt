package com.example.protej.easyloginapp

import android.R.attr.password
import android.R.attr.text
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.widget.ProgressBar
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.protej.easyloginapp.databinding.ActivityMainBinding
import com.example.protej.easyloginapp.databinding.SignUpPageBinding
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import kotlin.jvm.java
import kotlin.toString

class MainActivity : AppCompatActivity() {
    lateinit var binding: ActivityMainBinding

   lateinit var bindingXml: SignUpPageBinding
   lateinit var bottomSheetDialog: BottomSheetDialog

   lateinit var Detabase: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityMainBinding.inflate(layoutInflater)
        enableEdgeToEdge()
        setContentView(binding.root)

        bottomSheetDialog=BottomSheetDialog(this)
        bindingXml= SignUpPageBinding.inflate(layoutInflater)
        bottomSheetDialog.setContentView(bindingXml.root)

        binding.tvSignup.setOnClickListener {
            bottomSheetDialog.show()
        }

        bindingXml.btnRegister.setOnClickListener {
            sign_up()
        }

        binding.btnLogin16.setOnClickListener {

            val username=binding.etusername.text.toString().trim()
            val password=binding.etPassword.text.toString().trim()
            raj(username,password)
        }

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }

    private fun sign_up() {

        val UserName = bindingXml.Username.text.toString().trim()
        val FullName = bindingXml.etFullName.text.toString().trim()
        val Password = bindingXml.etPassword.text.toString().trim()
        val RetypePassword = bindingXml.etRetypePassword.text.toString().trim()

        if (UserName.isEmpty() || FullName.isEmpty() || Password.isEmpty() || RetypePassword.isEmpty()) {
        } else if (Password != RetypePassword) {
            Toast.makeText(this, "Passwords do not match!", Toast.LENGTH_SHORT).show()

        }else{

            val setdeta= user_Deta(UserName,FullName,Password,RetypePassword)
            bindingXml.progressBar.visibility=View.VISIBLE
            Detabase= FirebaseDatabase.getInstance().getReference("User")
            Detabase.child(UserName).setValue(setdeta)

                .addOnSuccessListener {
                    runOnUiThread {
                        bindingXml.progressBar.visibility = View.GONE
                        Toast.makeText(this, "Add Sucessful", Toast.LENGTH_SHORT).show()
                        bottomSheetDialog.dismiss()
                        cleartext()
                    }

            }.addOnFailureListener{

                    runOnUiThread {
                        Toast.makeText(this, "User Registration Failed!", Toast.LENGTH_SHORT).show()
                        bottomSheetDialog.dismiss()
                    }
            }
        }
    }

   private fun cleartext(){
        bindingXml.Username.text?.clear()
       bindingXml.etFullName.text?.clear()
       bindingXml.etPassword.text?.clear()
       bindingXml.etRetypePassword.text?.clear()
    }

    private fun raj(username: String, password: String) {

        binding.progressBar.visibility=View.VISIBLE
        Detabase= FirebaseDatabase.getInstance().getReference("User")

        Detabase.child(username).get().addOnSuccessListener { snapshot ->
            if (snapshot.exists()) {
                val savedPassword = snapshot.child("password").value.toString()

                if (savedPassword == password) {
                    runOnUiThread {

                        binding.progressBar.visibility = View.GONE
                        Toast.makeText(this, "User Exists & Password Correct", Toast.LENGTH_SHORT).show()
                        intent=Intent(this, MainActivity2::class.java)
                        startActivity(intent)
                    }
                }
                else {
                    runOnUiThread {
                        binding.progressBar.visibility = View.GONE
                        Toast.makeText(this, "User Not Found", Toast.LENGTH_SHORT).show()

                    }
                }

            } else {
                runOnUiThread {
                    binding.progressBar.visibility = View.GONE
                    Toast.makeText(this, "User Exists but UserName,Password Check", Toast.LENGTH_SHORT).show()
                }

            }

        }.addOnFailureListener {
            Toast.makeText(this, "Process Failed", Toast.LENGTH_SHORT).show()
        }
    }

}


