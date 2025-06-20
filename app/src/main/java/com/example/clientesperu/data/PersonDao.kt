package com.example.clientesperu.data

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface PersonDao {
    @Query("SELECT * FROM persons")
    fun getAllPersons(): LiveData<List<Person>>

    @Query("SELECT * FROM persons WHERE fullName LIKE :search OR documentId LIKE :search")
    fun searchPersons(search: String): LiveData<List<Person>>

    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun insert(person: Person)

    @Query("SELECT COUNT(*) FROM persons WHERE documentId = :documentId")
    suspend fun isDocumentExists(documentId: String): Int

    @Delete
    suspend fun delete(person: Person)

    @Update
    suspend fun update(person: Person)
}
