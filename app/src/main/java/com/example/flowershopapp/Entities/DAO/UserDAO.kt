package com.example.flowershopapp.Entities.DAO

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.example.flowershopapp.Entities.Model.User

@Dao
interface UserDAO {
    @Query("select * from users")
    fun getAll(): List<User>

    @Query("select * from users where name = :userName")
    fun getUserByName(userName: String): User

    @Insert
    suspend fun insert(vararg user: User)

    @Update
    suspend fun update(user: User)

    @Delete
    suspend fun delete(user: User)

    @Query("DELETE FROM users")
    suspend fun deleteAll()
}