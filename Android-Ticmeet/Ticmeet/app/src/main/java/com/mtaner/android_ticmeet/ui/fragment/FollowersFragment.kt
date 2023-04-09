package com.mtaner.android_ticmeet.ui.fragment

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.firebase.auth.FirebaseAuth
import com.mtaner.android_ticmeet.data.model.User
import com.mtaner.android_ticmeet.databinding.LayoutSendMessageSelectUserBinding
import com.mtaner.android_ticmeet.ui.adapter.FollowAdapter
import com.mtaner.android_ticmeet.ui.viewmodel.AuthViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject


@AndroidEntryPoint
class FollowersFragment(private val userList: List<User>) : BottomSheetDialogFragment() {

    private lateinit var binding: LayoutSendMessageSelectUserBinding

    @Inject
    lateinit var followingAdapter: FollowAdapter

    private val authViewModel: AuthViewModel by viewModels()

    private lateinit var auth: FirebaseAuth

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
        binding = LayoutSendMessageSelectUserBinding.inflate(inflater, container, false)

        auth = FirebaseAuth.getInstance()

        binding.imageViewBack.setOnClickListener {
            dialog?.hide()
        }


        binding.textViewToolbar.text = "Takipçilerim"

        initRv()

        authViewModel.getUserFollowers(auth.currentUser!!.email!!)

        val list = arrayListOf<User>()
        lifecycleScope.launch {
            authViewModel.userFollowers.collect {
                if (it.isLoading) {
                    binding.progressBarLoading.visibility = View.VISIBLE
                    binding.recyclerViewUsers.visibility = View.GONE
                }
                it.data.let { userList ->
                    binding.progressBarLoading.visibility = View.GONE
                    binding.recyclerViewUsers.visibility = View.VISIBLE
                    if (userList != null) {
                        list.addAll(userList)
                        followingAdapter.setUserList(list)
                    }
                }

            }
        }




        return binding.root
    }

    private fun initRv() {
        binding.recyclerViewUsers.apply {
            layoutManager = LinearLayoutManager(requireContext())
            setHasFixedSize(true)
            adapter = followingAdapter
        }
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