package com.example.chatapp.Activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.example.chatapp.MainActivity
import com.example.chatapp.R
import com.example.chatapp.databinding.ActivityOtpBinding
import com.google.firebase.FirebaseException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthOptions
import com.google.firebase.auth.PhoneAuthProvider
import java.util.concurrent.TimeUnit

class OtpActivity : AppCompatActivity() {
    private lateinit var binding: ActivityOtpBinding
    private lateinit var auth: FirebaseAuth
    private lateinit var dialog: AlertDialog
    private lateinit var verificationId : String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityOtpBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = FirebaseAuth.getInstance()

        val builder = AlertDialog.Builder(this)

        builder.setMessage("Please Wait")
        builder.setTitle("Loading")
        builder.setCancelable(false)

        dialog = builder.create()
        dialog.show()

        val phoneNo = "+91"+intent.getStringExtra("Number")

        val options = PhoneAuthOptions.newBuilder(auth)
            .setPhoneNumber(phoneNo)
            .setTimeout(60L,TimeUnit.SECONDS)
            .setActivity(this)
            .setCallbacks(object : PhoneAuthProvider.OnVerificationStateChangedCallbacks(){
                override fun onVerificationCompleted(p0: PhoneAuthCredential) {

                }

                override fun onVerificationFailed(p0: FirebaseException) {
                  dialog.dismiss()
                    Toast.makeText(this@OtpActivity, "Please try again", Toast.LENGTH_SHORT).show()
                }

                override fun onCodeSent(p0: String, p1: PhoneAuthProvider.ForceResendingToken) {
                    super.onCodeSent(p0, p1)

                    dialog.dismiss()
                    verificationId = p0
                }

            }).build()

        PhoneAuthProvider.verifyPhoneNumber(options)

        binding.btnContinue.setOnClickListener {
            if (binding.etOtp.text.isEmpty()){
                Toast.makeText(this, "Please enter OTP", Toast.LENGTH_SHORT).show()
            }
            else{
                dialog.show()
                val credential = PhoneAuthProvider.getCredential(verificationId,binding.etOtp.text.toString())
                auth.signInWithCredential(credential)
                    .addOnCompleteListener {
                        if (it.isSuccessful){

                            dialog.dismiss()
                            startActivity(Intent(this,ProfileActivity::class.java))
                            finish()
                        }
                        else{
                            dialog.dismiss()
                            Toast.makeText(this, "Error:${it.exception} ", Toast.LENGTH_SHORT).show()
                        }
                    }
            }
        }

    }
}