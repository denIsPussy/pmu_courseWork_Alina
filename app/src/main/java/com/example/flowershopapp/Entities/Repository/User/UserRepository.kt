package com.example.flowershopapp.Entities.Repository.User

import com.example.flowershopapp.Entities.Model.User

interface UserRepository {
    suspend fun getAll(): List<User>
    suspend fun getUserByName(userName: String): User
    suspend fun insert(user: User)
    suspend fun update(user: User)
    suspend fun delete(user: User)
    suspend fun deleteAll()
}