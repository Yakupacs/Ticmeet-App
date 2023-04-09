package com.mtaner.android_ticmeet.ui.fragment

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.firebase.auth.FirebaseAuth
import com.mtaner.android_ticmeet.data.model.Comment
import com.mtaner.android_ticmeet.data.model.EventItem
import com.mtaner.android_ticmeet.data.model.User
import com.mtaner.android_ticmeet.databinding.LayoutSendCommentBinding
import com.mtaner.android_ticmeet.ui.viewmodel.AuthViewModel
import com.mtaner.android_ticmeet.ui.viewmodel.EventsViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class EventCommentFragment(private val event: EventItem) : BottomSheetDialogFragment() {

    private lateinit var binding: LayoutSendCommentBinding

    private val eventsViewModel: EventsViewModel by viewModels()
    private val authViewModel: AuthViewModel by viewModels()

    private lateinit var auth: FirebaseAuth

    var currentUser : User? = null

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = BottomSheetDialog(requireContext(), theme)
        dialog.setOnShowListener {

            val bottomSheetDialog = it as BottomSheetDialog
            val parentLayout =
                bottomSheetDialog.findViewById<View>(com.google.android.material.R.id.design_bottom_sheet)
            parentLayout?.let { it ->
                val behaviour = BottomSheetBehavior.from(it)
                setupFullHeight(it)
                behaviour.state = BottomSheetBehavior.STATE_EXPANDED
            }
        }


        return dialog
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = LayoutSendCommentBinding.inflate(inflater, container, false)

        auth = FirebaseAuth.getInstance()

        binding.textViewClose.setOnClickListener {
            dialog?.hide()
        }

        binding.textViewSend.setOnClickListener {

            val commentText = binding.editTextComment.text.toString().trim()

            if (commentText.length > 29) {
                binding.textViewAlert.visibility = View.GONE
                val comment = Comment(commentText,auth.currentUser!!.email,event.eventID,currentUser?.userName,currentUser?.userUsername)
                eventsViewModel.sendEventComment(comment)
                lifecycleScope.launch {
                    eventsViewModel.sendComment.collect {
                        if (it.isLoading) {
                            binding.progressBarLoading.visibility = View.VISIBLE
                            binding.textViewSend.visibility = View.GONE
                        } else {
                            binding.progressBarLoading.visibility = View.GONE
                            dialog?.hide()
                        }
                    }
                }
            } else {
                binding.textViewAlert.visibility = View.VISIBLE
            }


        }

        authViewModel.getUser(auth.currentUser!!.email!!)
        lifecycleScope.launch {
            authViewModel.userData.collect {
                if (it.data != null) {
                    currentUser = it.data
                }
            }
        }



        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }



    private fun setupFullHeight(bottomSheet: View) {
        val layoutParams = bottomSheet.layoutParams
        layoutParams.height = WindowManager.LayoutParams.MATCH_PARENT
        bottomSheet.layoutParams = layoutParams
    }

}