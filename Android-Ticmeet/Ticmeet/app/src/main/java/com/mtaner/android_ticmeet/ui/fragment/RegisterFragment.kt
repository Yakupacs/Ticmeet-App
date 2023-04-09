package com.mtaner.android_ticmeet.ui.fragment

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.coroutineScope
import androidx.navigation.fragment.findNavController
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.mtaner.android_ticmeet.R
import com.mtaner.android_ticmeet.data.model.User
import com.mtaner.android_ticmeet.databinding.FragmentRegisterBinding
import com.mtaner.android_ticmeet.ui.activities.MainActivity
import com.mtaner.android_ticmeet.ui.viewmodel.AuthViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect

@AndroidEntryPoint
class RegisterFragment : Fragment() {

    private var _binding : FragmentRegisterBinding? = null
    private val binding get() = _binding!!

    private val authViewModel : AuthViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
       _binding = FragmentRegisterBinding.inflate(inflater,container,false)


        binding.buttonRegister.setOnClickListener {
            val email = binding.editTextEmail.text.toString().trim()
            val password = binding.editTextPassword.text.toString().trim()
            val passwordChange = binding.editTextChangePassword.text.toString().trim()

            if (password == passwordChange && email.isNotEmpty() && passwordChange.isNotEmpty() && passwordChange.isNotEmpty()) {
                authViewModel.register(email,password, saveInfo = false)
                findNavController().navigate(R.id.action_registerFragment_to_registerCompleteFragment)
            } else {
                Toast.makeText(requireContext(), "Şifreler uyuşmuyor", Toast.LENGTH_SHORT).show()
            }
        }

        binding.textViewLogin.setOnClickListener {
            findNavController().navigate(R.id.action_registerFragment_to_loginFragment)
        }
        return binding.root
    }

}