package com.example.android.politicalpreparedness.election.adapter

import android.text.format.DateUtils
import android.view.View
import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.example.android.politicalpreparedness.network.models.Address
import com.example.android.politicalpreparedness.utils.fadeIn
import com.example.android.politicalpreparedness.utils.fadeOut
import java.util.*


@BindingAdapter("textDate")
fun dateFormat(textView: TextView, value: Date?) {
    var localizedDate = ""
    if (value != null) {
        localizedDate = DateUtils.formatDateTime(
                textView.context,
                value.time,
                DateUtils.FORMAT_SHOW_DATE or DateUtils.FORMAT_SHOW_YEAR
        )
    }

    textView.text = localizedDate
}


@BindingAdapter("android:fadeVisible")
fun setFadeVisible(view: View, visible: Boolean? = true) {
    if (view.tag == null) {
        view.tag = true
        view.visibility = if (visible == true) View.VISIBLE else View.GONE
    } else {
        view.animate().cancel()
        if (visible == true) {
            if (view.visibility == View.GONE)
                view.fadeIn()
        } else {
            if (view.visibility == View.VISIBLE)
                view.fadeOut()
        }
    }
}


@BindingAdapter("android:setFadeVisibleString")
fun setFadeVisibleString(view: TextView, str: String?) {
    val visible = !str.isNullOrBlank()

    if (view.tag == null) {
        view.tag = true
        view.visibility = if (visible) View.VISIBLE else View.GONE
    } else {
        view.animate().cancel()
        if (visible) {
            if (view.visibility == View.GONE)
                view.fadeIn()
        } else {
            if (view.visibility == View.VISIBLE)
                view.fadeOut()
        }
    }
}

@BindingAdapter("android:setStrAddress")
fun setStrAddress(view: TextView, address: Address?) {
    setFadeVisible(view, address != null)

    val strAddress = StringBuilder()
    if (!address?.line1.isNullOrBlank()) strAddress.append("${address?.line1}\n")
    if (!address?.line2.isNullOrBlank()) strAddress.append("${address?.line2}\n")
    if (!address?.city.isNullOrBlank()) strAddress.append("${address?.city}\n")
    if (!address?.state.isNullOrBlank()) strAddress.append("${address?.state}\n")
    if (!address?.zip.isNullOrBlank()) strAddress.append("${address?.zip}")
    view.text = strAddress
}





