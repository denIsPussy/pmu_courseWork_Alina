package com.example.flowershopapp.Database.RemoteKeys.Repository

import com.example.flowershopapp.Database.RemoteKeys.Model.RemoteKeyType
import com.example.flowershopapp.Database.RemoteKeys.Model.RemoteKeys

interface RemoteKeyRepository {
    suspend fun getAllRemoteKeys(id: Int, type: RemoteKeyType): RemoteKeys?
    suspend fun createRemoteKeys(remoteKeys: List<RemoteKeys?>)
    suspend fun deleteRemoteKey(type: RemoteKeyType)
}