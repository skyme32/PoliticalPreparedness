package com.example.android.politicalpreparedness.representative

import android.content.Context
import android.location.Geocoder
import android.location.Location
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.android.politicalpreparedness.R
import com.example.android.politicalpreparedness.databinding.FragmentRepresentativeBinding
import com.example.android.politicalpreparedness.election.CONECTION
import com.example.android.politicalpreparedness.network.models.Address
import com.example.android.politicalpreparedness.representative.adapter.RepresentativeListAdapter
import com.example.android.politicalpreparedness.utils.showSnackbar
import java.util.*

class DetailFragment : Fragment() {

    companion object {
        //TODO: Add Constant for Location request
    }

    // Declare ViewModel
    private lateinit var viewModel: RepresentativeViewModel
    private lateinit var binding: FragmentRepresentativeBinding

    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        // Establish bindings
        viewModel = ViewModelProvider(this).get(RepresentativeViewModel::class.java)
        binding = FragmentRepresentativeBinding.inflate(inflater)
        binding.lifecycleOwner = this
        binding.viewModel = viewModel


        // Define and assign Representative adapter
        // Populate Representative adapter -> in XML with DataBinding
        val representativeAdapter = RepresentativeListAdapter()
        //representativeAdapter.setHasStableIds(true)
        binding.recyclerRepresentative.adapter = representativeAdapter


        //
        viewModel.status.observe(viewLifecycleOwner, androidx.lifecycle.Observer {
            if (it == CONECTION.DISCONNECTED) {
                showSnackbar(R.string.not_find_representative)
            }
        })


        //TODO: Establish button listeners for field and location search
        binding.buttonSearch.setOnClickListener {
            // Hide the keyboard
            hideKeyboard()

            // Take the data of EditText and convert to addres
            viewModel.getAddress(getTextDataBinding())

            // search the representatives with the address
            viewModel.getRepresentatives()
        }

        binding.buttonLocation.setOnClickListener {
            hideKeyboard()
            checkLocationPermissions()
        }


        return binding.root
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        //TODO: Handle location permission result to get location on permission granted
    }

    private fun checkLocationPermissions(): Boolean {
        return if (isPermissionGranted()) {
            true
        } else {
            //TODO: Request Location permissions
            false
        }
    }

    private fun isPermissionGranted(): Boolean {
        //TODO: Check if permission is already granted and return (true = granted, false = denied/other)
        return false
    }

    private fun getLocation() {
        //TODO: Get location from LocationServices
        //TODO: The geoCodeLocation method is a helper function to change the lat/long location to a human readable street address
    }

    private fun setTextDataBinding() {

    }

    private fun getTextDataBinding(): Address {
        return Address(
                line1 = binding.addressLine1.text.toString(),
                line2 = binding.addressLine2.text.toString(),
                city = binding.city.text.toString(),
                state = binding.state.selectedItem.toString(),
                zip = binding.zip.text.toString()
        )
    }

    private fun geoCodeLocation(location: Location): Address {
        val geocoder = Geocoder(context, Locale.getDefault())
        return geocoder.getFromLocation(location.latitude, location.longitude, 1)
                .map { address ->
                    Address(address.thoroughfare, address.subThoroughfare, address.locality, address.adminArea, address.postalCode)
                }
                .first()
    }

    private fun hideKeyboard() {
        val imm = activity?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(view!!.windowToken, 0)
    }

}