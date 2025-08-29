package com.example.pakstylelens

import android.content.Intent
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class RecommendationFormActivity : AppCompatActivity() {

    private lateinit var firestore: FirebaseFirestore
    private lateinit var auth: FirebaseAuth

    private lateinit var colourGroup: RadioGroup
    private lateinit var priceGroup: RadioGroup
    private lateinit var styleGroup: RadioGroup
    private lateinit var materialGroup: RadioGroup
    private lateinit var submitButton: Button
    private lateinit var skipButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_recommendation_form)

        // Initialize Firebase
        firestore = FirebaseFirestore.getInstance()
        auth = FirebaseAuth.getInstance()

        // Find views by ID
        colourGroup = findViewById(R.id.radioGroupColour)
        priceGroup = findViewById(R.id.radioGroupPrice)
        styleGroup = findViewById(R.id.radioGroupStyle)
        materialGroup = findViewById(R.id.radioGroupMaterial)
        submitButton = findViewById(R.id.btnSubmit)
        skipButton = findViewById(R.id.btnSkip)

        submitButton.setOnClickListener {
            val colourId = colourGroup.checkedRadioButtonId
            val priceId = priceGroup.checkedRadioButtonId
            val styleId = styleGroup.checkedRadioButtonId
            val materialId = materialGroup.checkedRadioButtonId

            if (colourId == -1 || priceId == -1 || styleId == -1 || materialId == -1) {
                Toast.makeText(this, "Please answer all questions", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val colour = findViewById<RadioButton>(colourId).text.toString()
            val price = findViewById<RadioButton>(priceId).text.toString()
            val style = findViewById<RadioButton>(styleId).text.toString()
            val material = findViewById<RadioButton>(materialId).text.toString()

            val formResponse = hashMapOf(
                "favouriteColour" to colour,
                "preferredPrice" to price,
                "preferredStyle" to style,
                "favouriteMaterial" to material
            )

            val userId = auth.currentUser?.uid
            if (userId != null) {
                firestore.collection("users").document(userId)
                    .update(formResponse as Map<String, Any>)
                    .addOnSuccessListener {
                        Toast.makeText(this, "Form submitted successfully", Toast.LENGTH_SHORT).show()
                        startActivity(Intent(this, home_activity::class.java))
                        finish()
                    }
                    .addOnFailureListener { e ->
                        Toast.makeText(this, "Error submitting form: ${e.message}", Toast.LENGTH_SHORT).show()
                    }
            } else {
                Toast.makeText(this, "User not logged in", Toast.LENGTH_SHORT).show()
            }
        }

        skipButton.setOnClickListener {
            // Just go to home without saving anything
            startActivity(Intent(this, home_activity::class.java))
            finish()
        }
    }
}
