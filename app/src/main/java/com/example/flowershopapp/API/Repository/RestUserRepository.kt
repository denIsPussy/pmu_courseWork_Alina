package com.example.flowershopapp.API.Repository

import android.util.Log
import com.example.flowershopapp.API.Model.toUser
import com.example.flowershopapp.API.Model.toUserRemote
import com.example.flowershopapp.API.MyServerService
import com.example.flowershopapp.Entities.Model.User
import com.example.flowershopapp.Entities.Repository.User.OfflineUserRepository
import com.example.flowershopapp.Entities.Repository.User.UserRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class RestUserRepository(
    private val service: MyServerService,
    private val dbUserRepository: OfflineUserRepository,
) : UserRepository {
    override suspend fun getAll(): List<User> {
        Log.d(RestUserRepository::class.simpleName, "Get Users")
        var existUsersList = listOf<User>()
        withContext(Dispatchers.IO) {
            existUsersList = dbUserRepository.getAll()
        }
        var existUsers = existUsersList.associateBy { it.userId }.toMutableMap()
        var remoteUsers = service.getUsers()
        remoteUsers
            .map { it.toUser() }
            .forEach { user ->
                val existUser = existUsers[user.userId!!]
                if (existUser == null) {
                    dbUserRepository.insert(user)
                } else if (existUser != user) {
                    dbUserRepository.update(user)
                }
                existUsers[user.userId!!] = user
            }

        return service.getUsers().map { it.toUser() }
    }

    override suspend fun getUserByName(userName: String): User {
        return service.getUser(userName)[0].toUser()
    }


    override suspend fun insert(user: User) {
        service.createUser(user.toUserRemote()).toUser()
    }

    override suspend fun update(user: User) {
        user.userId?.let { service.updateUser(it, user.toUserRemote()).toUser() }
    }

    override suspend fun delete(user: User) {
        user.userId?.let { service.deleteUser(it).toUser() }
    }

    override suspend fun deleteAll() {
        TODO("Not yet implemented")
    }
}