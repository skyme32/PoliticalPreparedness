package com.example.android.politicalpreparedness.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.android.politicalpreparedness.network.models.Election
import com.example.android.politicalpreparedness.network.models.Followed

@Dao
interface ElectionDao {

    /**
     * ELECTION [Election::class]
     */

    // Add insert query
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAllElection(vararg election: Election)

    // Add select all election query
    @Query("SELECT * FROM election_table ORDER BY electionDay DESC")
    fun getAllElection(): LiveData<List<Election>>

    // Add select single election query
    @Query("SELECT * FROM election_table where id = :id")
    fun getSingleElection(id: Int): Election

    // Select all election Followed
    @Query("SELECT * from election_table where id in (SELECT id FROM followed_table) ORDER BY electionDay DESC")
    fun getFollowedElections(): LiveData<List<Election>>

    // Add delete query
    @Query("DELETE FROM election_table where id = :id")
    fun delSingleElection(id: Int)

    // Add clear query
    @Query("DELETE FROM election_table")
    fun clearElection()


    /**
     * FOLLOWED ELECTION [Followed::class]
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFollowed(followed: Followed)

    @Query("SELECT count(*) FROM followed_table where id = :id")
    fun getFollow(id: Int): Int

    @Query("DELETE FROM followed_table where id = :id")
    suspend fun delFollowed(id: Int)

    @Query("DELETE FROM followed_table")
    fun clearFollowed()




}