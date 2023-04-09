package com.mtaner.android_ticmeet.ui.activities

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.provider.Settings
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.karumi.dexter.Dexter
import com.karumi.dexter.MultiplePermissionsReport
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.DexterError
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.multi.MultiplePermissionsListener
import com.mtaner.android_ticmeet.R
import com.mtaner.android_ticmeet.data.model.User
import com.mtaner.android_ticmeet.databinding.ActivityEditProfileBinding
import com.mtaner.android_ticmeet.ui.fragment.RegisterCompleteFragment
import com.mtaner.android_ticmeet.ui.viewmodel.AuthViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.*
import java.util.*

@AndroidEntryPoint
class EditProfileActivity : AppCompatActivity() {

    private lateinit var binding: ActivityEditProfileBinding


    var gender = ""
    var age = 0

    private var resultLauncherGalleryProfilePhoto: ActivityResultLauncher<Intent>? = null
    private var resultLauncherGalleryTopImage: ActivityResultLauncher<Intent>? = null

    private lateinit var storageReference: StorageReference
    private lateinit var auth: FirebaseAuth
    private lateinit var firebaseFirestore: FirebaseFirestore

    private var userSelectedProfile = ""
    private var selectedImageUriProfilePhoto: Uri? = null

    var userSelectedTopImage = ""
    var selectedImageUriTop: Uri? = null


    private val authViewModel: AuthViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEditProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)


        auth = FirebaseAuth.getInstance()
        storageReference = FirebaseStorage.getInstance().reference
        firebaseFirestore = FirebaseFirestore.getInstance()

        val user = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            intent.getParcelableExtra("user", User::class.java)
        } else {
            intent.getParcelableExtra("user")
        }

        if (user != null) {
            initData(user)
        }

        binding.imageViewBack.setOnClickListener {
            finish()
        }

        requestPermission()

        binding.textViewSave.setOnClickListener {

            val userUserName = binding.editTextUserUserName.text.toString()
            val userNameAndLastName = binding.editTextUserNameAndLastName.text.toString()
            val bio = binding.editTextBio.text.toString()
            val city = binding.editTextCity.text.toString()



            selectedImageUriProfilePhoto?.let {

            }

            if (selectedImageUriProfilePhoto != null && selectedImageUriTop != null) {
                saveStorageTopAndProfileImage(
                    selectedImageUriProfilePhoto!!,
                    selectedImageUriTop!!,
                    userUserName,
                    userNameAndLastName,
                    bio,
                    city
                )
            } else if (selectedImageUriProfilePhoto != null) {
                saveStorageImage(
                    selectedImageUriProfilePhoto!!,
                    userUserName,
                    auth.currentUser!!.email,
                    userNameAndLastName,
                    bio,
                    city
                )
            } else if (selectedImageUriTop != null) {
                saveStorageTopImage(
                    selectedImageUriTop!!,
                    userUserName,
                    userNameAndLastName,
                    bio,
                    city
                )
            } else {
                saveStorage(userUserName, auth.currentUser!!.email, userNameAndLastName, bio, city)
            }


        }

        resultLauncherGalleryProfilePhoto =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
                //loading
                if (result.resultCode == Activity.RESULT_OK) {
                    val data = result?.data?.data
                    getImage(data)
                    selectedImageUriProfilePhoto = data


                } else if (result.resultCode == Activity.RESULT_CANCELED) {

                }
            }

        resultLauncherGalleryTopImage =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
                //loading
                if (result.resultCode == Activity.RESULT_OK) {
                    val data = result?.data?.data
                    getTopImage(data)
                    selectedImageUriTop = data

                } else if (result.resultCode == Activity.RESULT_CANCELED) {

                }
            }


    }

    private fun requestPermission() {
        Dexter.withContext(this)
            .withPermissions(
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE,

                )
            .withListener(object : MultiplePermissionsListener {
                override fun onPermissionsChecked(multiplePermissionsReport: MultiplePermissionsReport) {

                    if (multiplePermissionsReport.areAllPermissionsGranted()) {
                        binding.imageViewUserProfile.setOnClickListener {
                            val takePicGallery =
                                Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
                            resultLauncherGalleryProfilePhoto?.launch(takePicGallery)
                        }

                        binding.imageViewEditTopPhoto.setOnClickListener {
                            val takePicGallery =
                                Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
                            resultLauncherGalleryTopImage?.launch(takePicGallery)
                        }
                    }
                }

                override fun onPermissionRationaleShouldBeShown(
                    list: List<PermissionRequest?>?,
                    permissionToken: PermissionToken
                ) {
                    permissionToken.continuePermissionRequest()
                }
            }).withErrorListener { error: DexterError? ->

            }
            .onSameThread().check()
    }

    private fun getTopImage(data: Uri?) {
        Glide.with(this).load(data).into(binding.imageViewUserTopImage)
    }


    private fun getImage(data: Uri?) {
        Glide.with(this).load(data).into(binding.imageViewUserProfile)
    }

    private fun saveStorageTopAndProfileImage(
        uriUserProfilePhoto: Uri,
        uriUserTopImage: Uri,
        userUserName: String,
        userNameAndLastName: String,
        bio: String,
        city: String,
    ) {

        binding.progressBarLoading.visibility = View.VISIBLE
        binding.textViewSave.visibility = View.GONE

        val randomImageId = Random().nextInt(99999999)

        CoroutineScope(Dispatchers.IO).launch {
            val fileRefProfilePhoto: StorageReference = storageReference.child("userImages/")
                .child("user_${auth.currentUser?.email}${randomImageId}.jpg")

            val fileRefTopImage: StorageReference = storageReference.child("userImages/")
                .child("user_top_${auth.currentUser?.email}${randomImageId}.jpg")

            withContext(Dispatchers.Main) {
                uriUserTopImage.let {
                    fileRefTopImage.putFile(it).addOnSuccessListener { task ->
                        if (task.task.isSuccessful) {
                            fileRefTopImage.downloadUrl.addOnSuccessListener { uri ->

                                userSelectedTopImage = uri.toString()

                                val userMap = hashMapOf<String, Any>()
                                userMap["userAge"] = age
                                userMap["userBio"] = bio
                                userMap["userGender"] = gender
                                if (userSelectedTopImage.isNotEmpty()) userMap["userTopImage"] =
                                    userSelectedTopImage
                                userMap["userName"] = userNameAndLastName
                                userMap["userUsername"] = userUserName
                                userMap["userLocation"] = city
                                authViewModel.update(userMap)
                                lifecycleScope.launch {
                                    authViewModel.update.collect { loading ->
                                        if (!loading.isLoading) {
                                            binding.progressBarLoading.visibility = View.GONE
                                            if (userSelectedProfile.isNotEmpty() && userSelectedTopImage.isNotEmpty())
                                                finish()
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }

            withContext(Dispatchers.Main) {
                uriUserProfilePhoto.let {
                    fileRefProfilePhoto.putFile(it).addOnSuccessListener { task ->
                        if (task.task.isSuccessful) {
                            fileRefProfilePhoto.downloadUrl.addOnSuccessListener { uri ->

                                userSelectedProfile = uri.toString()

                                val userMap = hashMapOf<String, Any>()
                                if (userSelectedProfile.isNotEmpty()) userMap["userImage"] =
                                    userSelectedProfile

                                authViewModel.update(userMap)
                                lifecycleScope.launch {
                                    authViewModel.update.collect { loading ->
                                        if (!loading.isLoading) {
                                            binding.progressBarLoading.visibility = View.GONE
                                            if (userSelectedProfile.isNotEmpty())
                                                finish()
                                        }
                                    }
                                }


                            }
                        }
                    }
                }
            }
        }


    }

    private fun saveStorageImage(
        uri: Uri,
        userUserName: String,
        email: String?,
        userNameAndLastName: String,
        bio: String,
        city: String,
    ) {

        binding.progressBarLoading.visibility = View.VISIBLE
        binding.textViewSave.visibility = View.GONE

        val randomImageId = Random().nextInt(99999999)
        CoroutineScope(Dispatchers.IO).launch {
            val fileRef: StorageReference = storageReference.child("userImages/")
                .child("user_${auth.currentUser?.email}${randomImageId}.jpg")
            withContext(Dispatchers.Main) {
                uri.let {
                    fileRef.putFile(it).addOnSuccessListener { task ->
                        if (task.task.isSuccessful) {
                            fileRef.downloadUrl.addOnSuccessListener { uri ->
                                println("uri---------$uri")

                                userSelectedProfile = uri.toString()

                                val userMap = hashMapOf<String, Any>()
                                userMap["userAge"] = age
                                userMap["userBio"] = bio
                                userMap["userGender"] = gender
                                userMap["userImage"] = userSelectedProfile
                                userMap["userName"] = userNameAndLastName
                                userMap["userUsername"] = userUserName
                                userMap["userLocation"] = city
                                authViewModel.update(userMap)
                                lifecycleScope.launch {
                                    authViewModel.update.collect { loading ->
                                        if (!loading.isLoading) {
                                            binding.progressBarLoading.visibility = View.GONE
                                            if (userSelectedProfile.isNotEmpty())
                                                finish()
                                        }
                                    }
                                }


                            }
                        }
                    }
                }
            }
        }


    }

    private fun saveStorageTopImage(
        uri: Uri,
        userUserName: String,
        userNameAndLastName: String,
        bio: String,
        city: String,
    ) {

        binding.progressBarLoading.visibility = View.VISIBLE
        binding.textViewSave.visibility = View.GONE

        val randomImageId = Random().nextInt(99999999)
        CoroutineScope(Dispatchers.IO).launch {
            val fileRef: StorageReference = storageReference.child("userImages/")
                .child("user_top_${auth.currentUser?.email}${randomImageId}.jpg")
            withContext(Dispatchers.Main) {
                uri.let {
                    fileRef.putFile(it).addOnSuccessListener { task ->
                        if (task.task.isSuccessful) {
                            fileRef.downloadUrl.addOnSuccessListener { uri ->
                                println("uri---------$uri")

                                userSelectedTopImage = uri.toString()

                                val userMap = hashMapOf<String, Any>()
                                userMap["userAge"] = age
                                userMap["userBio"] = bio
                                userMap["userGender"] = gender
                                userMap["userTopImage"] = userSelectedTopImage
                                userMap["userName"] = userNameAndLastName
                                userMap["userUsername"] = userUserName
                                userMap["userLocation"] = city
                                authViewModel.update(userMap)
                                lifecycleScope.launch {
                                    authViewModel.update.collect { loading ->
                                        if (!loading.isLoading) {
                                            binding.progressBarLoading.visibility = View.GONE
                                            if (userSelectedTopImage.isNotEmpty())
                                                finish()
                                        }
                                    }
                                }


                            }
                        }
                    }
                }
            }
        }


    }

    private fun saveStorage(
        userUserName: String,
        email: String?,
        userNameAndLastName: String,
        bio: String,
        city: String,
    ) {

        val userMap = hashMapOf<String, Any>()
        userMap["userAge"] = age
        userMap["userBio"] = bio
        userMap["userGender"] = gender
        userMap["userName"] = userNameAndLastName
        userMap["userUsername"] = userUserName
        userMap["userLocation"] = city
        authViewModel.update(userMap)

        lifecycleScope.launch {
            authViewModel.update.collect { loading ->
                if (loading.isLoading) {
                    binding.progressBarLoading.visibility = View.VISIBLE
                    binding.textViewSave.visibility = View.GONE
                } else {
                    binding.progressBarLoading.visibility = View.GONE
                    finish()
                }
            }
        }

    }


    private fun initData(user: User) {

        val ageList = arrayListOf<Int>()
        for (i in 18..100) ageList.add(i)

        val genderList = listOf("Erkek", "Kadın", "Belirtmek istemiyorum")

        val ageAdapter =
            ArrayAdapter(this, R.layout.spinner_aligned, ageList)
        ageAdapter.setDropDownViewResource(R.layout.spinner_aligned)

        val genderAdapter =
            ArrayAdapter(this, R.layout.spinner_aligned, genderList)
        genderAdapter.setDropDownViewResource(R.layout.spinner_aligned)

        binding.spinnerAge.adapter = ageAdapter
        binding.spinnerGender.adapter = genderAdapter


        binding.spinnerGender.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>,
                view: View,
                position: Int,
                id: Long
            ) {
                gender = parent.getItemAtPosition(position).toString()
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }

        binding.spinnerAge.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>,
                view: View,
                position: Int,
                id: Long
            ) {
                age = parent.getItemAtPosition(position).toString().toInt()
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }


        if (!user.userTopImage.isNullOrEmpty()) {
            Glide.with(this).load(user.userTopImage).into(binding.imageViewUserTopImage)
        }

        Glide.with(this).load(user.userImage).into(binding.imageViewUserProfile)
        binding.editTextUserNameAndLastName.setText(user.userName)
        binding.editTextUserUserName.setText(user.userUsername)
        binding.editTextBio.setText(user.userBio)
        binding.editTextCity.setText(user.userLocation)
        binding.spinnerGender.setSelection(genderList.indexOf(user.userGender), true)
        binding.spinnerAge.setSelection(ageList.indexOf(user.userAge), true)


    }


}