package com.example.android.politicalpreparedness.database

import android.util.Log
import androidx.lifecycle.LiveData
import com.example.android.politicalpreparedness.network.CivicsApi
import com.example.android.politicalpreparedness.network.models.Election
import com.example.android.politicalpreparedness.network.models.Followed
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class ElectionRepository(private val database: ElectionDatabase) {

    /**
     * A playlist of elections that can be shown on the screen.
     */
    val allElection: LiveData<List<Election>> = database.electionDao.getAllElection()

    /**
     * A playlist of elections that can be shown on the screen with saved on Database
     */
    val allElectionFollowed: LiveData<List<Election>> = database.electionDao.getFollowedElections()

    /**
     *
     */
    suspend fun isFollowed(election: Election): Boolean {
        var flagIsFollowed = false
        withContext(Dispatchers.IO) {
            val result = database.electionDao.getFollow(election.id)
            flagIsFollowed = result > 0

        }
        Log.d(TAG, "FLAG IS FOLLOW: ${election.id}: $flagIsFollowed")
        return flagIsFollowed
    }

    /**
     *
     */
    suspend fun addFollowed(election: Election) {
        withContext(Dispatchers.IO) {
            val followed = Followed(election.id)
            database.electionDao.insertFollowed(followed)
        }
    }

    /**
     *
     */
    suspend fun delFollowed(election: Election) {
        withContext(Dispatchers.IO) {
            database.electionDao.delFollowed(election.id)
        }
    }

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

private const val TAG = "RepositoryRefreshElections"