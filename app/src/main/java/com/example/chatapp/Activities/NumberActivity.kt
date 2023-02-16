package com.example.chatapp.Activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.chatapp.MainActivity
import com.example.chatapp.databinding.ActivityNumberBinding
import com.google.firebase.auth.FirebaseAuth

class NumberActivity : AppCompatActivity() {
    private lateinit var binding: ActivityNumberBinding
    private lateinit var auth : FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding= ActivityNumberBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = FirebaseAuth.getInstance()

        if (auth.currentUser != null){
            startActivity(Intent(this,MainActivity::class.java))
            finish()
        }

        binding.btnContinue.setOnClickListener {
           if ( binding.etPhoneNo.text.isEmpty()){
               Toast.makeText(this, "enter your no.", Toast.LENGTH_SHORT).show()
           }
            else{
                val intent = Intent(this,OtpActivity::class.java)
               intent.putExtra("Number",binding.etPhoneNo.text)
               startActivity(intent)

           }
        }

    }
}