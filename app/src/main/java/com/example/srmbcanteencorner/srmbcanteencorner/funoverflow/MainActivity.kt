package com.example.srmbcanteencorner.srmbcanteencorner.funoverflow

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

import com.google.zxing.BarcodeFormat
import com.google.zxing.WriterException
import com.google.zxing.common.BitMatrix
import com.journeyapps.barcodescanner.BarcodeEncoder
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

class MainActivity : AppCompatActivity() {

    private lateinit var logoutButton: Button
    private lateinit var auth: FirebaseAuth
    private lateinit var mDatabase: DatabaseReference
    private var isPressed = true
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        auth = FirebaseAuth.getInstance()

        logoutButton = findViewById(R.id.logoutButton)
        logoutButton.setOnClickListener {
            // Call signOut() to log the user out
            auth.signOut()
            val intent = Intent(this, LoginActivity::class.java)
            finish()
            startActivity(intent)
            // Optionally, navigate to the login screen or perform other actions
        }



        val Lunch = findViewById<Button>(R.id.Lunch)
        Lunch.setOnClickListener {
            if (isPressed) {
                // If button is already pressed, revert to original state
                Lunch.text = "Cancel Order"
                saveLunchData(true)
                Lunch.setBackgroundResource(R.drawable.button_pressed_state)
            } else {
                // If button is not pressed, update to pressed state
                Lunch.text = "Order Lunch"
                saveLunchData(false)


            }
            // Toggle the flag
            isPressed = !isPressed
        }

        val UserDetails = findViewById<TextView>(R.id.userDetails)

        mDatabase = FirebaseDatabase.getInstance().reference

        val currentUserUid = FirebaseAuth.getInstance().currentUser?.uid

        currentUserUid?.let { uid ->
            // Get reference to the current user's node in the database
            val userRef = mDatabase.child("users").child(uid)

            // Add listener to fetch user data from the database
            userRef.addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    // Check if data exists for the current user
                    if (snapshot.exists()) {
                        // Get user details from the snapshot
                        val userDetails = snapshot.getValue(User::class.java)

                        // Display user details in the TextView
                        userDetails?.let {
                            val userDetailsText = "Name: ${it.name}\n" +
                                    "Employee ID: ${it.eid}\n" +
                                    "Phone: ${it.phone}\n" +
                                    "Password: ${it.password}"
                            UserDetails.text = userDetailsText

                            val qrCodeBitmap = generateQRCode(userDetailsText)
                            val qrCodeImageView = findViewById<ImageView>(R.id.qrCode)
                            val generateQr = findViewById<Button>(R.id.generateQr)
                            generateQr.setOnClickListener{qrCodeImageView.setImageBitmap(qrCodeBitmap)}

                        }
                    } else {
                        // User data not found
                        UserDetails.text = "User details not found"
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    // Handle database error
                    UserDetails.text = "Failed to fetch user details: ${error.message}"
                }
            })
        }




    }private fun generateQRCode(data: String): Bitmap? {
        try {
            val bitMatrix: BitMatrix = BarcodeEncoder().encode(data, BarcodeFormat.QR_CODE, 400, 400)
            return Bitmap.createBitmap(bitMatrix.width, bitMatrix.height, Bitmap.Config.RGB_565).apply {
                for (x in 0 until bitMatrix.width) {
                    for (y in 0 until bitMatrix.height) {
                        setPixel(x, y, if (bitMatrix[x, y]) 0xFF000000.toInt() else 0xFFFFFFFF.toInt())
                    }
                }
            }
        } catch (e: WriterException) {
            e.printStackTrace()
        }
        return null
    }
    override fun onPause() {
        super.onPause()
        val sharedPref = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)
        val editor = sharedPref.edit()
        editor.putString("lastActivity", "Home")
        editor.apply()
    }
    private fun saveLunchData(hasLunch: Boolean) {
        // Implement code to save lunch data to the database
        // For example, you can use Firebase Realtime Database or Firestore
        // Here is a pseudo-code example using Firebase Realtime Database
        val currentUserUid = FirebaseAuth.getInstance().currentUser?.uid
        currentUserUid?.let { uid ->
            val timestamp = Calendar.getInstance().timeInMillis
            val currentDate = getCurrentDate()
            val currentTime = getCurrentTime()
            val lunchRef = FirebaseDatabase.getInstance().getReference("orders").child(currentDate).child(uid).child("lunch")
            val lunchData = mapOf(
                "hasLunch" to hasLunch,
                "timestamp" to currentTime
             )
            lunchRef.setValue(lunchData)
                .addOnSuccessListener {
                    // Data successfully saved
                }
                .addOnFailureListener { error ->
                    // Handle the error
                }
        }
    }
    private fun getCurrentDate(): String {
        val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        return dateFormat.format(Date())
    }
    private fun getCurrentTime(): String {
        val timeFormat = SimpleDateFormat("HH:mm:ss", Locale.getDefault())
        return timeFormat.format(Date())
    }
}