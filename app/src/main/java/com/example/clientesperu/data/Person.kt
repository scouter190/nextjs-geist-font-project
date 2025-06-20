package com.example.clientesperu.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "persons")
data class Person(
    @PrimaryKey val documentId: String,
    val fullName: String,
    val address: String,
    val phone: String,
    val district: String,
    val maritalStatus: String,
    val gender: String
)
