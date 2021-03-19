package com.example.android.politicalpreparedness.election

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.android.politicalpreparedness.R
import com.example.android.politicalpreparedness.databinding.FragmentElectionBinding
import com.example.android.politicalpreparedness.election.adapter.ElectionListAdapter
import com.example.android.politicalpreparedness.election.adapter.ElectionListener
import com.example.android.politicalpreparedness.network.jsonadapter.ElectionAdapter
import com.example.android.politicalpreparedness.network.models.Election

class ElectionsFragment: Fragment() {

    // Declare ViewModel
    private lateinit var viewModel: ElectionsViewModel

    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        // Add ViewModel values and create ViewModel
        viewModel = ViewModelProvider(
                this,
                ElectionsViewModelFactory(requireActivity().application)).get(ElectionsViewModel::class.java
        )

        // Add binding values
        val binding = FragmentElectionBinding.inflate(inflater)
        binding.lifecycleOwner = this

        // Link elections to voter info
        viewModel.navigateToDetailElection.observe(viewLifecycleOwner, Observer { election ->
            if (election != null) {
                findNavController().navigate(ElectionsFragmentDirections.actionElectionsFragmentToVoterInfoFragment(election.id, election.division))
                viewModel.onElectionNavigated()
            }
        })

        // Initiate recycler adapters
        val electionAdapter = ElectionListAdapter(ElectionListener { election ->
            viewModel.onElectionClicked(election)
        })

        // Populate recycler adapters
        //binding.asteroidRecycler.adapter = asteroidAdapter
        binding.electionRecycler.adapter = electionAdapter

        // Refresh adapters when fragment loads
        viewModel.electionUpcoming.observe(viewLifecycleOwner, Observer<List<Election>> { electionList ->
            electionList.apply {
                electionAdapter.submitList(electionList)
            }
        })

        return binding.root
    }


}