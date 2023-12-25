package com.example.flowershopapp.Entities.Model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey

@Entity(tableName = "users")
data class User(
    @PrimaryKey(autoGenerate = true)
    val userId: Int?,
    @ColumnInfo(name = "name")
    val userName: String,
    @ColumnInfo(name = "dateOfBirth")
    val dateOfBirth: String,
    @ColumnInfo(name = "phoneNumber")
    val phoneNumber: String,
    @ColumnInfo(name = "password")
    val password: String
) {
    @Ignore
    constructor(
        userName: String,
        dateOfBirth: String,
        phoneNumber: String,
        password: String
    ) : this(null, userName, dateOfBirth, phoneNumber, password)

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as User

        if (userId != other.userId) return false
        if (userName != other.userName) return false
        if (dateOfBirth != other.dateOfBirth) return false
        if (phoneNumber != other.phoneNumber) return false
        if (password != other.password) return false

        return true
    }

    override fun hashCode(): Int {
        var result = userId ?: 0
        result = 31 * result + userName.hashCode()
        result = 31 * result + dateOfBirth.hashCode()
        result = 31 * result + phoneNumber.hashCode()
        result = 31 * result + password.hashCode()
        return result
    }

}