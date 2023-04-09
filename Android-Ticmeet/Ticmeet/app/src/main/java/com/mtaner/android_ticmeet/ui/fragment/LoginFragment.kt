package com.mtaner.android_ticmeet.ui.fragment

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.coroutineScope
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.mtaner.android_ticmeet.R
import com.mtaner.android_ticmeet.databinding.FragmentLoginBinding
import com.mtaner.android_ticmeet.ui.activities.MainActivity
import com.mtaner.android_ticmeet.ui.viewmodel.AuthViewModel

import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class LoginFragment : Fragment() {

    private var _binding : FragmentLoginBinding? = null
    private val binding get() = _binding!!

    private val authViewModel : AuthViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentLoginBinding.inflate(inflater,container,false)

        authViewModel.loggedUser()

        lifecycle.coroutineScope.launchWhenCreated {
            authViewModel.user.collect {
                if (it.isLoading) {
                    binding.progressBar.visibility = View.VISIBLE
                    binding.loginLayout.visibility = View.GONE
                    binding.imageViewLogin.visibility = View.GONE
                    binding.layoutOther.visibility = View.GONE
                }
                if (it.error.isNotBlank()) {
                    binding.loginLayout.visibility = View.VISIBLE
                    binding.imageViewLogin.visibility = View.VISIBLE
                    binding.layoutOther.visibility = View.VISIBLE
                    binding.progressBar.visibility = View.GONE
                }

                it.data?.let {
                    val intent = Intent(requireContext(), MainActivity::class.java)
                    startActivity(intent)
                    requireActivity().finish()
                }
            }
        }

        binding.textViewResetPassword.setOnClickListener {
            val bottomSheetDialog = ResetPasswordFragment()
            bottomSheetDialog.show(parentFragmentManager, "resetPassword")
        }

        binding.buttonLogin.setOnClickListener {
            val email = binding.editTextEmail.text.toString()
            val password = binding.editTextPassword.text.toString().trim()
            if (email.isNotEmpty() && password.isNotEmpty()) {
                authViewModel.login(email, password)
            }

        }

        binding.textViewRegister.setOnClickListener {
            findNavController().navigate(R.id.action_loginFragment_to_registerFragment)
        }
        return binding.root
    }

}