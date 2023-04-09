package com.mtaner.android_ticmeet.ui.fragment

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatButton
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.mtaner.android_ticmeet.data.model.EventItem
import com.mtaner.android_ticmeet.data.model.User
import com.mtaner.android_ticmeet.databinding.FragmentEventsBinding
import com.mtaner.android_ticmeet.ui.adapter.EventItemAdapter
import com.mtaner.android_ticmeet.ui.viewmodel.EventsViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

@AndroidEntryPoint
class EventsFragment : Fragment() {

    private var _binding: FragmentEventsBinding? = null
    private val binding get() = _binding!!

    @Inject
    lateinit var eventItemAdapter: EventItemAdapter

    @Inject
    lateinit var eventItemAdapter2: EventItemAdapter

    @Inject
    lateinit var eventItemAdapter3: EventItemAdapter

    @Inject
    lateinit var eventItemAdapter4: EventItemAdapter

    @Inject
    lateinit var eventItemAdapter5: EventItemAdapter

    @Inject
    lateinit var eventItemAdapter6: EventItemAdapter

    @Inject
    lateinit var eventItemAdapter7: EventItemAdapter

    @Inject
    lateinit var eventItemAdapter8: EventItemAdapter

    private val eventsViewModel: EventsViewModel by viewModels()

    private lateinit var firebaseFirestore: FirebaseFirestore

    private lateinit var auth: FirebaseAuth

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentEventsBinding.inflate(inflater, container, false)

        firebaseFirestore = FirebaseFirestore.getInstance()
        auth = FirebaseAuth.getInstance()

        initRv()
        getUserEventConcert()




        return binding.root
    }


    @SuppressLint("SetTextI18n")
    private fun getUserEventConcert() {
        eventsViewModel.getEvent()
        lifecycleScope.launch {
            eventsViewModel.event.collect {

                if (it.isLoading) {
                    binding.progressBarLoading.visibility = View.VISIBLE
                    binding.nestedScroolView.visibility = View.GONE
                }
                it.data?.let { eventList ->

                    binding.progressBarLoading.visibility = View.GONE
                    binding.nestedScroolView.visibility = View.VISIBLE

                    eventItemAdapter.setEventItemList(eventList,"Konser")
                    eventItemAdapter2.setEventItemList(eventList,"Tiyatro")
                    eventItemAdapter3.setEventItemList(eventList,"Söyleşi")
                    eventItemAdapter4.setEventItemList(eventList,"Sinema")
                    eventItemAdapter5.setEventItemList(eventList,"Sergi")
                    eventItemAdapter6.setEventItemList(eventList,"Opera")
                    eventItemAdapter7.setEventItemList(eventList,"Performans")
                    eventItemAdapter8.setEventItemList(eventList,"Bale")

                    if (!eventItemAdapter.getEventItemList("Konser")) binding.includeEventKonser.textViewTitle.text = "Konser"
                    if (!eventItemAdapter2.getEventItemList("Tiyatro")) binding.includeEventTiyatro.textViewTitle.text = "Tiyatro"
                    if (!eventItemAdapter3.getEventItemList("Söyleşi")) binding.includeEventSoylesi.textViewTitle.text = "Söyleşi"
                    if (!eventItemAdapter4.getEventItemList("Sinema")) binding.includeEventSinema.textViewTitle.text = "Sinema"
                    if (!eventItemAdapter5.getEventItemList("Sergi")) binding.includeEventSergi.textViewTitle.text = "Sergi"
                    if (!eventItemAdapter6.getEventItemList("Opera")) binding.includeEventOpera.textViewTitle.text = "Opera"
                    if (!eventItemAdapter7.getEventItemList("Performans")) binding.includeEventPerformans.textViewTitle.text = "Performans"
                    if (!eventItemAdapter8.getEventItemList("Bale")) binding.includeEventBale.textViewTitle.text = "Bale"


                }
            }
        }
    }

    private fun initRv() {

        binding.includeEventKonser.recyclerView.apply {
            layoutManager = LinearLayoutManager(
                requireContext(),
                LinearLayoutManager.HORIZONTAL,
                false
            )
            adapter = eventItemAdapter

        }

        binding.includeEventTiyatro.recyclerView.apply {
            layoutManager = LinearLayoutManager(
                requireContext(),
                LinearLayoutManager.HORIZONTAL,
                false
            )
            adapter = eventItemAdapter2

        }

        binding.includeEventSoylesi.recyclerView.apply {
            layoutManager = LinearLayoutManager(
                requireContext(),
                LinearLayoutManager.HORIZONTAL,
                false
            )
            adapter = eventItemAdapter3

        }

        binding.includeEventSinema.recyclerView.apply {
            layoutManager = LinearLayoutManager(
                requireContext(),
                LinearLayoutManager.HORIZONTAL,
                false
            )
            adapter = eventItemAdapter4

        }

        binding.includeEventSergi.recyclerView.apply {
            layoutManager = LinearLayoutManager(
                requireContext(),
                LinearLayoutManager.HORIZONTAL,
                false
            )
            adapter = eventItemAdapter5

        }

        binding.includeEventOpera.recyclerView.apply {
            layoutManager = LinearLayoutManager(
                requireContext(),
                LinearLayoutManager.HORIZONTAL,
                false
            )
            adapter = eventItemAdapter6

        }

        binding.includeEventPerformans.recyclerView.apply {
            layoutManager = LinearLayoutManager(
                requireContext(),
                LinearLayoutManager.HORIZONTAL,
                false
            )
            adapter = eventItemAdapter7

        }

        binding.includeEventBale.recyclerView.apply {
            layoutManager = LinearLayoutManager(
                requireContext(),
                LinearLayoutManager.HORIZONTAL,
                false
            )
            adapter = eventItemAdapter8

        }


    }



}