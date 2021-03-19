package com.example.android.politicalpreparedness.election

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.android.politicalpreparedness.database.ElectionDao

// Create Factory to generate VoterInfoViewModel with provided election datasource
class VoterInfoViewModelFactory(private val app: Application, private val dataSource: ElectionDao): ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {

        if (modelClass.isAssignableFrom(VoterInfoViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return VoterInfoViewModel(app, dataSource) as T
        }
        throw IllegalArgumentException("Unable to construct viewmodel")
    }
}
