package com.example.android.politicalpreparedness.utils

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.example.android.politicalpreparedness.R
import com.example.android.politicalpreparedness.network.models.Address
import com.example.android.politicalpreparedness.network.models.Division
import com.google.android.material.snackbar.Snackbar


fun Fragment.setTitle(title: String) {
    if (activity is AppCompatActivity) {
        (activity as AppCompatActivity).supportActionBar?.title = title
    }
}

fun Fragment.setDisplayHomeAsUpEnabled(bool: Boolean) {
    if (activity is AppCompatActivity) {
        (activity as AppCompatActivity).supportActionBar?.setDisplayHomeAsUpEnabled(
                bool
        )
    }
}

fun Fragment.showSnackbar(message: Int) {
    //sbView.setBackgroundColor(ContextCompat.getColor(getActivity(), R.color.BLACK));
    val snackBar = Snackbar.make(requireView(),
            getString(message), Snackbar.LENGTH_LONG)
    snackBar.view.setBackgroundColor(ContextCompat.getColor(context!!, R.color.redError))
    snackBar.show()
}

//animate changing the view visibility
fun View.fadeIn() {
    this.visibility = View.VISIBLE
    this.alpha = 0f
    this.animate().alpha(1f).setListener(object : AnimatorListenerAdapter() {
        override fun onAnimationEnd(animation: Animator) {
            this@fadeIn.alpha = 1f
        }
    })
}

//animate changing the view visibility
fun View.fadeOut() {
    this.animate().alpha(0f).setListener(object : AnimatorListenerAdapter() {
        override fun onAnimationEnd(animation: Animator) {
            this@fadeOut.alpha = 1f
            this@fadeOut.visibility = View.GONE
        }
    })
}

fun formatDivisionVoterInfo(division: Division): String {
    val strBuilder = StringBuilder()

    strBuilder.append(division.country)
    if (division.state != "") {
        strBuilder.append(",")
        strBuilder.append(division.state)
    }

    Log.i("FormatDivision", strBuilder.toString())
    return strBuilder.toString()
}

fun convertAddress(address: Address?): String {
    val strAddress = StringBuilder()
    strAddress.append("geo:0,0?q=")
    if (!address?.line1.isNullOrBlank()) strAddress.append("${address?.line1}+")
    if (!address?.city.isNullOrBlank()) strAddress.append("${address?.city}+")
    if (!address?.state.isNullOrBlank()) strAddress.append("${address?.state}+")
    if (!address?.zip.isNullOrBlank()) strAddress.append("${address?.zip}")
    Log.i("serviceVoterInfo", "--> ADDRESS: $strAddress")
    return strAddress.toString()
}

