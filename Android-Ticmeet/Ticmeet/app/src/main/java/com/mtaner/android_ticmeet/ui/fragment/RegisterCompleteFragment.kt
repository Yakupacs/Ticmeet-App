package com.mtaner.android_ticmeet.ui.fragment

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FieldValue
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
import com.mtaner.android_ticmeet.databinding.FragmentRegisterCompleteBinding
import com.mtaner.android_ticmeet.ui.activities.MainActivity
import com.mtaner.android_ticmeet.ui.viewmodel.AuthViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.*


@AndroidEntryPoint
class RegisterCompleteFragment : Fragment() {

    private var _binding : FragmentRegisterCompleteBinding? = null
    private val binding get() = _binding!!

    private val authViewModel : AuthViewModel by viewModels()

    private lateinit var storageReference: StorageReference
    private lateinit var auth: FirebaseAuth
    private lateinit var firebaseFirestore: FirebaseFirestore

    var gender = ""
    var age = 0

    var resultLauncherGallery: ActivityResultLauncher<Intent>? = null

    var userSelectedProfile = ""
    var selectedImageUri : Uri? = null


    companion object {
        const val STORAGE_PERMISSION_CODE = 100
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
       _binding = FragmentRegisterCompleteBinding.inflate(inflater,container,false)

        auth = FirebaseAuth.getInstance()
        storageReference = FirebaseStorage.getInstance().reference
        firebaseFirestore = FirebaseFirestore.getInstance()

        resultLauncherGallery =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
                //loading
                if (result.resultCode == Activity.RESULT_OK) {
                    // There are no request codes
                    val data = result?.data?.data
                    println("selectedImage1: " + data)
                    getImage(data)
                    selectedImageUri = data


                } else if (result.resultCode == Activity.RESULT_CANCELED) {
                    //loadingDialog.dismissDialog()
                }
            }

        val ageList = arrayListOf<Int>()
        for (i in 18..100) ageList.add(i)

        val genderList = listOf("Erkek","Kadın","Belirtmek istemiyorum")

        val ageAdapter =
            ArrayAdapter(requireContext(), R.layout.spinner_aligned, ageList)
        ageAdapter.setDropDownViewResource(R.layout.spinner_aligned)

        val genderAdapter =
            ArrayAdapter(requireContext(), R.layout.spinner_aligned, genderList)
        genderAdapter.setDropDownViewResource(R.layout.spinner_aligned)

        binding.spinnerAge.adapter = ageAdapter
        binding.spinnerGender.adapter = genderAdapter

        binding.imageViewRegister.setOnClickListener {
            requestPermission()
        }

        binding.buttonNext.setOnClickListener {

            val userUserName = binding.editTextUserName.text.toString()
            val userNameAndLastName = binding.editTextUserNameAndLastName.text.toString()
            val bio = binding.editTextBio.text.toString()
            val city = binding.editTextCity.text.toString()



            selectedImageUri?.let { saveStorage(it,userUserName,auth.currentUser!!.email,userNameAndLastName,bio,city) }




        }

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





        return binding.root
    }

    private fun saveStorage(
        uri: Uri,
        userUserName: String,
        email: String?,
        userNameAndLastName: String,
        bio: String,
        city: String
    ) {
        binding.buttonNext.visibility = View.GONE
        binding.progressBar.visibility = View.VISIBLE
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
                                binding.buttonNext.visibility = View.VISIBLE
                                binding.progressBar.visibility = View.GONE
                                userSelectedProfile = uri.toString()

                                val user = User(userUserName,auth.currentUser!!.email, userName = userNameAndLastName, userImage = userSelectedProfile, userGender = gender, userAge = age
                                    , userBio = bio, userLocation = city)
                                authViewModel.register("","",user,true)
                                goToHomePage()
                            }
                        }
                    }
                }
            }
        }

    }


    private fun getImage(data: Uri?) {
        Glide.with(requireActivity()).load(data).into(binding.imageViewRegister)
    }


    private fun requestPermission() {
        Dexter.withContext(requireContext())
            .withPermissions(
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE,

            )
            .withListener(object : MultiplePermissionsListener {
                override fun onPermissionsChecked(multiplePermissionsReport: MultiplePermissionsReport) {

                    if (multiplePermissionsReport.areAllPermissionsGranted()) {
                        val takePicGallery =
                            Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
                        resultLauncherGallery?.launch(takePicGallery)
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







    private fun goToHomePage() {
        val intent = Intent(requireContext(),MainActivity::class.java)
        startActivity(intent)
        requireActivity().finish()
    }

}