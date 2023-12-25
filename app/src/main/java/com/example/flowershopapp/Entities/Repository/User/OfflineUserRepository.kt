package com.example.flowershopapp.Entities.Repository.User

import com.example.flowershopapp.Entities.DAO.UserDAO
import com.example.flowershopapp.Entities.Model.User

class OfflineUserRepository(private val userDAO: UserDAO) : UserRepository {
    override suspend fun getAll(): List<User> = userDAO.getAll()
    override suspend fun getUserByName(userName: String): User = userDAO.getUserByName(userName)
    override suspend fun insert(user: User) = userDAO.insert(user)
    override suspend fun update(user: User) = userDAO.update(user)
    override suspend fun delete(user: User) = userDAO.delete(user)
    override suspend fun deleteAll() = userDAO.deleteAll()
    suspend fun insertUsers(users: List<User>) =
        userDAO.insert(*users.toTypedArray())
}