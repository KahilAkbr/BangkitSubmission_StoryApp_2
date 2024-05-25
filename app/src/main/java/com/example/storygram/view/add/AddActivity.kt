package com.example.storygram.view.add

import android.app.AlertDialog
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.example.storygram.R
import com.example.storygram.data.Result
import com.example.storygram.databinding.ActivityAddBinding
import com.example.storygram.utils.ObtainViewModelFactory
import com.example.storygram.utils.getImageUri
import com.example.storygram.utils.uriToFile
import com.example.storygram.view.main.MainActivity
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import java.io.File
import android.Manifest
import android.location.Location
import android.widget.Toast

class AddActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAddBinding
    private var currentImageUri: Uri? = null
    private var file: File? = null
    private lateinit var viewModel: AddViewModel
    private lateinit var alertBuilder: AlertDialog.Builder
    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient
    private var latitude: Float? = null
    private var longitude: Float? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel = ObtainViewModelFactory.obtain<AddViewModel>(this)

        alertBuilder = AlertDialog.Builder(this)

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this)

        binding.btnCamera.setOnClickListener {
            startCamera()
        }

        binding.btnGallery.setOnClickListener {
            startGallery()
        }

        binding.locationSwitch.setOnCheckedChangeListener { _, isChecked ->

            if (isChecked) {
                requestPermissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
            } else {
                latitude = null
                longitude = null
            }
        }

        binding.btnUpload.setOnClickListener {
            uploadStory()
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

    private fun uploadStory() {
        val description = binding.edDescStory.text.toString()

        viewModel.uploadStory(this, file, description, lat = latitude, lon = longitude)
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

    private val requestPermissionLauncher =
        registerForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) { isGranted ->
            if (!isGranted) {
                binding.locationSwitch.isChecked = false
                Toast.makeText(
                    this,
                    getString(R.string.location_permission_denied),
                    Toast.LENGTH_SHORT
                ).show()
            } else {
                getLastLocation {
                    latitude = it?.latitude?.toFloat()
                    longitude = it?.longitude?.toFloat()
                }
            }
        }

    private fun getLastLocation(callback: (Location?) -> Unit) {
        if (ContextCompat.checkSelfPermission(
                this.applicationContext,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            fusedLocationProviderClient.lastLocation.addOnCompleteListener(this) { location ->
                val latLng: Location? = location.result
                callback(latLng)
            }
        } else {
            requestPermissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
        }
    }
}