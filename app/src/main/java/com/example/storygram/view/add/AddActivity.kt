package com.example.storygram.view.add

import android.app.AlertDialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.example.storygram.R
import com.example.storygram.data.Result
import com.example.storygram.databinding.ActivityAddBinding
import com.example.storygram.utils.ObtainViewModelFactory
import com.example.storygram.utils.getImageUri
import com.example.storygram.utils.uriToFile
import com.example.storygram.view.main.MainActivity
import java.io.File

class AddActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAddBinding
    private var currentImageUri: Uri? = null
    private var file: File? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val viewModel = ObtainViewModelFactory.obtain<AddViewModel>(this)

        val alertBuilder = AlertDialog.Builder(this)

        binding.btnCamera.setOnClickListener {
            startCamera()
        }

        binding.btnGallery.setOnClickListener {
            startGallery()
        }

        binding.btnUpload.setOnClickListener {
            val description = binding.edDescStory.text.toString()
            viewModel.uploadStory(this, file, description, lat = null, lon = null)
                .observe(this) { result ->
                    if (result != null) {
                        when (result) {
                            is Result.Loading -> {
                                binding.progressBar.visibility = View.VISIBLE
                                binding.btnUpload.isClickable = false
                            }

                            is Result.Success -> {
                                binding.progressBar.visibility = View.GONE
                                binding.btnUpload.isClickable = true
                                alertBuilder.setTitle(getString(R.string.upload_success))
                                alertBuilder.setMessage(getString(R.string.upload_success_msg))
                                alertBuilder.setPositiveButton("OK") { _, _ ->
                                    val intent = Intent(this, MainActivity::class.java)
                                    intent.flags =
                                        Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                                    startActivity(intent)
                                    finishAffinity()
                                }.create().show()
                                alertBuilder.setOnCancelListener {
                                    val intent = Intent(this, MainActivity::class.java)
                                    intent.flags =
                                        Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                                    startActivity(intent)
                                    finishAffinity()
                                }.show()
                            }

                            is Result.Error -> {
                                binding.progressBar.visibility = View.GONE
                                binding.btnUpload.isClickable = true
                                alertBuilder.setTitle(getString(R.string.login_error))
                                alertBuilder.setMessage(result.error)
                                alertBuilder.setPositiveButton("OK") { _, _ -> }.create().show()
                            }
                        }
                    }
                }
        }
    }

    private fun startGallery() {
        launcherGallery.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
    }

    private val launcherGallery = registerForActivityResult(
        ActivityResultContracts.PickVisualMedia()
    ) { uri: Uri? ->
        if (uri != null) {
            currentImageUri = uri
            currentImageUri.let {
                if (it != null) {
                    file = uriToFile(it, this)
                }
            }
            showImage()
        } else {
            Log.d("Photo Picker", "No media selected")
        }
    }

    private fun startCamera() {
        currentImageUri = getImageUri(this)
        launcherIntentCamera.launch(currentImageUri)
    }

    private val launcherIntentCamera = registerForActivityResult(
        ActivityResultContracts.TakePicture()
    ) { isSuccess ->
        if (isSuccess) {
            currentImageUri.let {
                if (it != null) {
                    file = uriToFile(it, this)
                }
            }
            showImage()
        }
    }

    private fun showImage() {
        currentImageUri?.let {
            Log.d("Image URI", "showImage: $it")
            binding.ivDetailPhoto.setImageURI(it)
        }
    }
}