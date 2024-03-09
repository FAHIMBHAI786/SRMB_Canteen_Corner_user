package com.example.srmbcanteencorner.srmbcanteencorner.funoverflow

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
import com.google.firebase.firestore.auth.User
import com.google.zxing.BarcodeFormat
import com.google.zxing.WriterException
import com.google.zxing.common.BitMatrix
import com.journeyapps.barcodescanner.BarcodeEncoder

class MainActivity : AppCompatActivity() {

    private lateinit var mDatabase: DatabaseReference
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

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
                        val userDetails = snapshot.getValue(com.example.srmbcanteencorner.srmbcanteencorner.funoverflow.User::class.java)

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
}