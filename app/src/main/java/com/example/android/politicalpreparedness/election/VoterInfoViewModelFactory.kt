package com.example.android.politicalpreparedness.election

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.android.politicalpreparedness.database.ElectionDatabase
import com.example.android.politicalpreparedness.database.ElectionRepository
import com.example.android.politicalpreparedness.network.models.Election

// Create Factory to generate VoterInfoViewModel with provided election datasource
class VoterInfoViewModelFactory(val app: Application,
                                private val election: Election)
    : ViewModelProvider.Factory {

    private val database = ElectionDatabase.getInstance(app)
    private val electionRepository = ElectionRepository(database)

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {

        if (modelClass.isAssignableFrom(VoterInfoViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return VoterInfoViewModel(electionRepository, election) as T
        }
        throw IllegalArgumentException("Unable to construct viewmodel")
    }
}
