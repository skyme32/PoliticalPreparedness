package com.example.android.politicalpreparedness.election

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.observe
import com.example.android.politicalpreparedness.databinding.FragmentVoterInfoBinding
import com.example.android.politicalpreparedness.utils.setTitle


class VoterInfoFragment : Fragment() {

    private lateinit var viewModel: VoterInfoViewModel

    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?,
                              savedInstanceState: Bundle?): View {

        // Bundle args election info
        val infoElection = VoterInfoFragmentArgs.fromBundle(requireArguments()).election

        // Add ViewModel values and create ViewModel
        viewModel = ViewModelProvider(
                this,
                VoterInfoViewModelFactory(requireActivity().application, infoElection)).get(VoterInfoViewModel::class.java
        )

        // Add binding values
        val binding = FragmentVoterInfoBinding.inflate(inflater)
        binding.lifecycleOwner = this
        binding.viewModelVoter = viewModel


        // Populate voter info -- hide views without provided data.
        /**
        Hint: You will need to ensure proper data is provided from previous fragment.
        */
        binding.election = infoElection
        viewModel.state.observe(viewLifecycleOwner, Observer {
            Log.i("HOHO_TEST", it.name)

            when(it) {
                CONECTION.CONNECTED -> {
                    viewModel.voterInfo.observe(viewLifecycleOwner, Observer { voterInfo ->
                        binding.voterInfo = voterInfo
                    })
                }
                else -> {
                    binding.stateLocations.visibility = View.GONE
                    binding.stateBallot.visibility = View.GONE
                    binding.address.visibility = ViewGroup.GONE
                    binding.stateCorrespondenceHeader.visibility = View.GONE
                    binding.stateHeader.visibility = View.GONE
                }
            }
        })

        // Handle loading of URLs
        viewModel.urlIntent.observe(viewLifecycleOwner, Observer {
            loadURLIntents(it)
        })

        //TODO: Handle save button UI state
        //TODO: cont'd Handle save button clicks


        setTitle(infoElection.name)
        return binding.root
    }


    // Create method to load URL intents
    private fun loadURLIntents(strUri: String?) {
        if (!strUri.isNullOrBlank()) {
            val i = Intent(Intent.ACTION_VIEW, Uri.parse(strUri))
            startActivity(i)
        }
    }


}