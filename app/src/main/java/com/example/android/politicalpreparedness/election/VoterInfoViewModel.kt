package com.example.android.politicalpreparedness.election

import android.app.Application
import android.util.Log
import androidx.lifecycle.*
import com.example.android.politicalpreparedness.network.CivicsApi
import com.example.android.politicalpreparedness.network.models.Address
import com.example.android.politicalpreparedness.network.models.Election
import com.example.android.politicalpreparedness.network.models.VoterInfoResponse
import com.example.android.politicalpreparedness.utils.convertAddress
import com.example.android.politicalpreparedness.utils.formatDivisionVoterInfo
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class VoterInfoViewModel(private val database: Application,
                         private val election: Election)
    : ViewModel() {

    // Add live data to hold voter info
    private val _voterInfo = MutableLiveData<VoterInfoResponse>()
    val voterInfo: LiveData<VoterInfoResponse>
        get() = _voterInfo


    private val _state = MutableLiveData<CONECTION>()
    val state: LiveData<CONECTION>
        get() = _state

    val showNoData: MutableLiveData<Boolean> = MutableLiveData()


    // Add var and methods to populate voter info
    init {
        viewModelScope.launch {
            serviceVoterInfo()
        }
    }

    private suspend fun serviceVoterInfo() {
        withContext(Dispatchers.Main) {
            try {
                withContext(Dispatchers.IO) {
                    val strDivision = formatDivisionVoterInfo(election.division)
                    _voterInfo.postValue(CivicsApi.retrofitService.getVoterinfo(strDivision, election.id))
                }
                _state.value = CONECTION.CONNECTED
                showNoData.value = false
            } catch (err: Exception) {
                _state.value = CONECTION.DISCONNECTED
                showNoData.value = true
                Log.d(TAG, err.printStackTrace().toString())
            }
        }
    }

    // Add var and methods to support loading URLs
    val urlIntent = MutableLiveData<String>()
    fun loadingURLs(uri: String?) {
        urlIntent.value = uri
    }

    fun uriFormatAddress(address: Address?): String {
        return convertAddress(address)
    }


    //TODO: Add var and methods to save and remove elections to local database
    //TODO: cont'd -- Populate initial state of save button to reflect proper action based on election saved status

    /**
     * Hint: The saved state can be accomplished in multiple ways. It is directly related to how elections are saved/removed from the database.
     */
}
private const val TAG = "serviceVoterInfo"
enum class CONECTION {CONNECTED, DISCONNECTED}