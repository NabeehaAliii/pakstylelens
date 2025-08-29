package com.example.pakstylelens

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import android.util.Log

class LoginActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var emailEditText: EditText
    private lateinit var passwordEditText: EditText
    private lateinit var loginButton: Button
    private lateinit var createAccountButton: Button
    private lateinit var progressBar: ProgressBar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.login_activity)

        emailEditText = findViewById(R.id.etUsername)
        passwordEditText = findViewById(R.id.etPassword)
        loginButton = findViewById(R.id.btnLogin)
        createAccountButton = findViewById(R.id.btnCreateAccount)
        progressBar = findViewById(R.id.progressBar)

        auth = FirebaseAuth.getInstance()

        loginButton.setOnClickListener {
            val email = emailEditText.text.toString().trim()
            val password = passwordEditText.text.toString().trim()

            if (email.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Please enter email and password", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            progressBar.visibility = View.VISIBLE

            auth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener { task ->
                    progressBar.visibility = View.GONE
                    if (task.isSuccessful) {
                        startActivity(Intent(this, RecommendationFormActivity::class.java))
                        finish()
                    } else {
                        Toast.makeText(this, "Login failed: ${task.exception?.message}", Toast.LENGTH_SHORT).show()
                    }
                }
        }

        createAccountButton.setOnClickListener {
            startActivity(Intent(this, SignupActivity::class.java))
        }
    }
    // Add this below all your existing functions in home_activity.kt
    override fun onBackPressed() {
        AlertDialog.Builder(this)
            .setTitle("Exit?")
            .setMessage("Do you really want to exit PakStyle Lens?")
            .setPositiveButton("Yes") { _, _ -> super.onBackPressed() }
            .setNegativeButton("No", null)
            .show()
    }

    override fun onStart() {
        super.onStart()
        Log.d("LifeCycle", "LoginActivity: onStart")
        Toast.makeText(this, "LoginActivity: onStart", Toast.LENGTH_SHORT).show()
    }

    override fun onResume() {
        super.onResume()
        Log.d("LifeCycle", "LoginActivity: onResume")
        Toast.makeText(this, "LoginActivity: onResume", Toast.LENGTH_SHORT).show()
    }

    override fun onPause() {
        super.onPause()
        Log.d("LifeCycle", "LoginActivity: onPause")
        Toast.makeText(this, "LoginActivity: onPause", Toast.LENGTH_SHORT).show()
    }

    override fun onStop() {
        super.onStop()
        Log.d("LifeCycle", "LoginActivity: onStop")
        Toast.makeText(this, "LoginActivity: onStop", Toast.LENGTH_SHORT).show()
    }

    override fun onRestart() {
        super.onRestart()
        Log.d("LifeCycle", "LoginActivity: onRestart")
        Toast.makeText(this, "LoginActivity: onRestart", Toast.LENGTH_SHORT).show()
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d("LifeCycle", "LoginActivity: onDestroy")
        Toast.makeText(this, "LoginActivity: onDestroy", Toast.LENGTH_SHORT).show()
    }

}
