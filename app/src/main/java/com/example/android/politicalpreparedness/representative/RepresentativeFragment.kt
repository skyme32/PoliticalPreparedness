package com.example.android.politicalpreparedness.representative

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.location.Geocoder
import android.location.Location
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.android.politicalpreparedness.R
import com.example.android.politicalpreparedness.databinding.FragmentRepresentativeBinding
import com.example.android.politicalpreparedness.election.CONECTION
import com.example.android.politicalpreparedness.network.models.Address
import com.example.android.politicalpreparedness.representative.adapter.RepresentativeListAdapter
import com.example.android.politicalpreparedness.utils.showSnackbar
import com.google.android.gms.location.*
import java.util.*

class DetailFragment : Fragment() {

    companion object {
        // Add Constant for Location request
        private const val REQUEST_LOCATION_PERMISSION = 1001
        private const val TAG = "ErrorRepresentativeFrag"
    }

    // Declare ViewModel
    private lateinit var viewModel: RepresentativeViewModel
    private lateinit var binding: FragmentRepresentativeBinding
    private lateinit var fusedLocationClient: FusedLocationProviderClient


    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?,
                              savedInstanceState: Bundle?): View {

        // Establish bindings
        viewModel = ViewModelProvider(this).get(RepresentativeViewModel::class.java)
        binding = FragmentRepresentativeBinding.inflate(inflater)
        binding.lifecycleOwner = this
        binding.viewModel = viewModel


        // Define and assign Representative adapter
        // Populate Representative adapter -> in XML with DataBinding
        val representativeAdapter = RepresentativeListAdapter()
        binding.recyclerRepresentative.adapter = representativeAdapter


        // Show a message if the estate not CONNECTED
        viewModel.status.observe(viewLifecycleOwner, Observer {
            if (it == CONECTION.DISCONNECTED) {
                showSnackbar(R.string.not_find_representative)
            }
        })


        // Establish button listeners for field and location search
        binding.buttonSearch.setOnClickListener {
            // Hide the keyboard
            hideKeyboard()

            // Take the data of EditText and convert to address
            viewModel.getAddress(getTextDataBinding())

            // search the representatives with the address
            viewModel.getRepresentatives()
        }

        binding.buttonLocation.setOnClickListener {
            hideKeyboard()
            checkLocationPermissions()
        }

        // Initialize the location
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireActivity())

        return binding.root
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        // Handle location permission result to get location on permission granted
        if (requestCode == REQUEST_LOCATION_PERMISSION) {
            if (grantResults.isNotEmpty() && (grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                getLocation()
            }
        }
    }

    private fun checkLocationPermissions(): Boolean {
        return if (isPermissionGranted()) {
            getLocation()
            true
        } else {
            // Request Location permissions
            ActivityCompat.requestPermissions(
                    requireActivity(),
                    arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                    REQUEST_LOCATION_PERMISSION
            )
            false
        }
    }

    private fun isPermissionGranted(): Boolean {
        return ContextCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED
    }

    @SuppressLint("MissingPermission")
    private fun getLocation() {

        try {
            val mLocRequest = LocationRequest.create();
            mLocRequest.interval = 20000
            mLocRequest.smallestDisplacement = 5F
            mLocRequest.priority = LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY;

            fusedLocationClient.requestLocationUpdates(mLocRequest,
                    locationCallback,
                    null)
        } catch (e: SecurityException) {
            Log.e(TAG, e.message!!)
        }
    }

    override fun onPause() {
        super.onPause()
        fusedLocationClient.removeLocationUpdates(locationCallback)
    }

    private val locationCallback = object : LocationCallback() {
        override fun onLocationResult(locationResult: LocationResult?) {
            locationResult ?: return
                for (location in locationResult.locations) {
                    // Update UI with location data
                    val address = geoCodeLocation(location)
                    viewModel.getAddress(address)
                    viewModel.getRepresentatives()
                }
        }
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