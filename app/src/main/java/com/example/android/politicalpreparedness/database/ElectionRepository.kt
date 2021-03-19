package com.example.android.politicalpreparedness.database

import android.util.Log
import com.example.android.politicalpreparedness.network.CivicsApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class ElectionRepository(private val database: ElectionDatabase) {

    /**
     * Refresh the elections stored in the offline cache.
     * To actually load the elections for use, observe [elections]
     */
    suspend fun refreshElections() {
        withContext(Dispatchers.IO) {
            try {
                val elections = CivicsApi.retrofitService.getElections()
                val result = elections.elections
                database.electionDao.insertAllElection(*result.toTypedArray())

                Log.d(TAG, result.toString())
            } catch (err: Exception) {
                Log.d(TAG, err.printStackTrace().toString())
            }
        }
    }

}

private const val TAG = "RepositoryrefreshElections"