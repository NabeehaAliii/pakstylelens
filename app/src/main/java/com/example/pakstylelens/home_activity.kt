package com.example.pakstylelens

import android.Manifest
import android.app.Activity
import android.app.AlertDialog
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.toRequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.ByteArrayOutputStream

class home_activity : AppCompatActivity() {

    private lateinit var chatInput: EditText
    private lateinit var menuButton: ImageView
    private lateinit var sendButton: Button
    private lateinit var cameraButton: ImageView
    private lateinit var imagePreview: ImageView
    private lateinit var resultRecyclerView: RecyclerView

    private var selectedImageBitmap: Bitmap? = null

    private val CAMERA_REQUEST_CODE = 100
    private val GALLERY_REQUEST_CODE = 101

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.home_activity)

        chatInput = findViewById(R.id.chatInput)
        menuButton = findViewById(R.id.menuButton)
        sendButton = findViewById(R.id.sendButton)
        cameraButton = findViewById(R.id.cameraButton)
        imagePreview = findViewById(R.id.imagePreview)
        resultRecyclerView = findViewById(R.id.resultRecyclerView)

        resultRecyclerView.layoutManager = LinearLayoutManager(this)

        Toast.makeText(this, "App Started", Toast.LENGTH_SHORT).show()
        Log.d("AppInit", "Activity created successfully")

        sendButton.setOnClickListener {
            val caption = chatInput.text.toString().trim()
            when {
                caption.isNotEmpty() -> {
                    sendSearchRequest(caption, null)
                }

                selectedImageBitmap != null -> {
                    sendSearchRequest(null, selectedImageBitmap)
                    if (selectedImageBitmap != null) {
                        // Only clear image if it was a user-selected one
                        selectedImageBitmap = null
                        imagePreview.setImageResource(R.drawable.fashion_logo)  // Reset logo after image search
                    }

                }

                else -> {
                    Toast.makeText(this, "Enter text or select image!", Toast.LENGTH_SHORT).show()
                    Log.w("InputCheck", "No caption or image provided")
                }
            }
        }

        menuButton.setOnClickListener { showMenu() }
        cameraButton.setOnClickListener { showCameraOptions() }
    }

    private fun showMenu() {
        val options = arrayOf("Past History", "Clear History", "Logout")
        AlertDialog.Builder(this)
            .setTitle("Menu")
            .setItems(options) { _, which ->
                when (which) {
                    0 -> Toast.makeText(this, "History not implemented yet.", Toast.LENGTH_SHORT).show()
                    1 -> {
                        resultRecyclerView.visibility = View.GONE
                        Toast.makeText(this, "Cleared results", Toast.LENGTH_SHORT).show()
                    }
                    2 -> {
                        Toast.makeText(this, "Logging out", Toast.LENGTH_SHORT).show()
                        startActivity(Intent(this, LoginActivity::class.java))
                        finish()
                    }
                }
            }.show()
    }

    private fun showCameraOptions() {
        val options = arrayOf("Take Picture", "Upload from Gallery")
        AlertDialog.Builder(this)
            .setTitle("Select Option")
            .setItems(options) { _, which ->
                when (which) {
                    0 -> openCamera()
                    1 -> openGallery()
                }
            }.show()
    }

    private fun openCamera() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
            != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.CAMERA),
                CAMERA_REQUEST_CODE
            )
        } else {
            startActivityForResult(Intent(MediaStore.ACTION_IMAGE_CAPTURE), CAMERA_REQUEST_CODE)
        }
    }

    private fun openGallery() {
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        startActivityForResult(intent, GALLERY_REQUEST_CODE)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            when (requestCode) {
                CAMERA_REQUEST_CODE -> {
                    val image = data?.extras?.get("data") as? Bitmap
                    if (image != null) {
                        selectedImageBitmap = image
                        imagePreview.setImageBitmap(image)
                        Toast.makeText(this, "Image captured", Toast.LENGTH_SHORT).show()
                        Log.d("Camera", "Image captured successfully")
                    }
                }

                GALLERY_REQUEST_CODE -> {
                    val uri = data?.data
                    if (uri != null) {
                        val stream = contentResolver.openInputStream(uri)
                        val bitmap = BitmapFactory.decodeStream(stream)
                        selectedImageBitmap = bitmap
                        imagePreview.setImageBitmap(bitmap)
                        Toast.makeText(this, "Image selected", Toast.LENGTH_SHORT).show()
                        Log.d("Gallery", "Image selected from gallery")
                    }
                }
            }
        }
    }

    private fun sendSearchRequest(caption: String?, image: Bitmap?) {
        val userId = "guest@user.com"
        val api = RetrofitClient.api

        val captionPart = caption?.toRequestBody("text/plain".toMediaTypeOrNull())
        val userIdPart = userId.toRequestBody("text/plain".toMediaTypeOrNull())

        val imagePart = image?.let {
            val stream = ByteArrayOutputStream()
            it.compress(Bitmap.CompressFormat.PNG, 100, stream)
            val byteArray = stream.toByteArray()
            val requestFile = byteArray.toRequestBody("image/*".toMediaTypeOrNull())
            MultipartBody.Part.createFormData("image", "image.png", requestFile)
        }

        // 🟡 Make a copy before we null selectedImageBitmap
        val inputImageCopy = selectedImageBitmap

        Toast.makeText(this, "Sending search request...", Toast.LENGTH_SHORT).show()
        Log.d("SearchDebug", "Sending API request with caption=$caption, image=${image != null}")

        api.sendSearchRequest(captionPart, userIdPart, imagePart)
            .enqueue(object : Callback<SearchResponse> {
                override fun onResponse(
                    call: Call<SearchResponse>,
                    response: Response<SearchResponse>
                ) {
                    Log.d("SearchDebug", "API Response received, success=${response.isSuccessful}")

                    if (response.isSuccessful) {
                        val resultList = response.body()?.results ?: emptyList()
                        Log.d("SearchDebug", "Results parsed: ${resultList.size}")
                        Log.d("SearchResult", "Received results: $resultList")

                        if (resultList.isEmpty()) {
                            Toast.makeText(
                                this@home_activity,
                                "No results found.",
                                Toast.LENGTH_SHORT
                            ).show()
                            resultRecyclerView.visibility = View.GONE
                            return
                        }

                        Toast.makeText(
                            this@home_activity,
                            "Found ${resultList.size} result(s)",
                            Toast.LENGTH_SHORT
                        ).show()

                        resultRecyclerView.visibility = View.VISIBLE
                        resultRecyclerView.adapter = SearchResultAdapter(
                            resultList,
                            caption,
                            inputImageCopy // ✅ use the backup!
                        )

                        chatInput.setText("")
                        selectedImageBitmap = null
                        imagePreview.setImageResource(R.drawable.fashion_logo)

                    } else {
                        Toast.makeText(
                            this@home_activity,
                            "API Error: ${response.code()}",
                            Toast.LENGTH_SHORT
                        ).show()
                        Log.e("SearchDebug", "Error code: ${response.code()}, message: ${response.message()}")
                    }
                }

                override fun onFailure(call: Call<SearchResponse>, t: Throwable) {
                    Toast.makeText(
                        this@home_activity,
                        "Request failed: ${t.message}",
                        Toast.LENGTH_SHORT
                    ).show()
                    Log.e("SearchDebug", "API Failure", t)
                }
            })
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
        Toast.makeText(this, "home_activity: onStart", Toast.LENGTH_SHORT).show()
        Log.d("Lifecycle", "home_activity: onStart")
    }

    override fun onResume() {
        super.onResume()
        Toast.makeText(this, "home_activity: onResume", Toast.LENGTH_SHORT).show()
        Log.d("Lifecycle", "home_activity: onResume")
    }

    override fun onPause() {
        super.onPause()
        Toast.makeText(this, "home_activity: onPause", Toast.LENGTH_SHORT).show()
        Log.d("Lifecycle", "home_activity: onPause")
    }

    override fun onStop() {
        super.onStop()
        Toast.makeText(this, "home_activity: onStop", Toast.LENGTH_SHORT).show()
        Log.d("Lifecycle", "home_activity: onStop")
    }

    override fun onDestroy() {
        super.onDestroy()
        Toast.makeText(this, "home_activity: onDestroy", Toast.LENGTH_SHORT).show()
        Log.d("Lifecycle", "home_activity: onDestroy")
    }

    override fun onRestart() {
        super.onRestart()
        Toast.makeText(this, "home_activity: onRestart", Toast.LENGTH_SHORT).show()
        Log.d("Lifecycle", "home_activity: onRestart")
    }

}
