package com.example.android.politicalpreparedness.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.android.politicalpreparedness.network.models.Election

@Dao
interface ElectionDao {

    // Add insert query
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAllElection(vararg election: Election)

    // Add select all election query
    @Query("SELECT * FROM election_table ORDER BY electionDay DESC")
    fun getAllElection(): LiveData<List<Election>>

    // Add select single election query
    @Query("SELECT * FROM election_table where id = :id")
    fun getSingleElection(id: Int): Election

    // Add delete query
    @Query("DELETE FROM election_table where id = :id")
    fun delSingleElection(id: Int)

    // Add clear query
    @Query("DELETE FROM election_table")
    fun clearElection()

}