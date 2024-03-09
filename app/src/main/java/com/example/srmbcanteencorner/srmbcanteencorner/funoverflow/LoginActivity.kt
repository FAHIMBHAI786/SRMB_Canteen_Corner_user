package com.example.srmbcanteencorner.srmbcanteencorner.funoverflow

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth

class LoginActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        val Login = findViewById<Button>(R.id.logIn)
        val Email = findViewById<EditText>(R.id.etEmail)
        val Password = findViewById<EditText>(R.id.etPassword)

        var mAuth: FirebaseAuth

        mAuth = FirebaseAuth.getInstance()

        Login.setOnClickListener {
            val email = Email.text.toString()
            val password = Password.text.toString()

            mAuth.signInWithEmailAndPassword("$email@gmail.com", password)
                .addOnCompleteListener { task: Task<AuthResult?> ->
                    if (task.isSuccessful) {
                        val intent = Intent(this, MainActivity::class.java)
                        finish()
                        startActivity(intent)

                    } else {
                        val text = "Acoount Does Not Exist"
                        val duration = Toast.LENGTH_SHORT

                        val toast = Toast.makeText(this, text, duration)
                        toast.show()
                    }
                }

        }
    }
}