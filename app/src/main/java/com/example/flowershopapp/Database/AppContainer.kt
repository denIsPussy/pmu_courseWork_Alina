package com.example.flowershopapp.Database

import android.content.Context
import com.example.flowershopapp.API.MyServerService
import com.example.flowershopapp.API.Repository.RestBouquetRepository
import com.example.flowershopapp.API.Repository.RestOrderBouquetRepository
import com.example.flowershopapp.API.Repository.RestOrderRepository
import com.example.flowershopapp.API.Repository.RestUserRepository
import com.example.flowershopapp.Database.RemoteKeys.Repository.OfflineRemoteKeyRepository
import com.example.flowershopapp.Entities.Repository.Bouquet.OfflineBouquetRepository
import com.example.flowershopapp.Entities.Repository.Order.OfflineOrderRepository
import com.example.flowershopapp.Entities.Repository.OrderBouquets.OfflineOrdersWithBouquetsRepository
import com.example.flowershopapp.Entities.Repository.User.OfflineUserRepository

interface AppContainer {
    val bouquetRestRepository: RestBouquetRepository
    val userRestRepository: RestUserRepository
    val orderRestRepository: RestOrderRepository
    val orderBouquetRestRepository: RestOrderBouquetRepository

    companion object {
        const val TIMEOUT = 5000L
        const val LIMIT = 5
    }
}
class AppDataContainer(private val context: Context) : AppContainer {
    private val userRepository: OfflineUserRepository by lazy {
        OfflineUserRepository(AppDatabase.getInstance(context).userDao())
    }
    private val bouquetRepository: OfflineBouquetRepository by lazy {
        OfflineBouquetRepository(AppDatabase.getInstance(context).bouquetDao())
    }
    private val orderRepository: OfflineOrderRepository by lazy {
        OfflineOrderRepository(AppDatabase.getInstance(context).orderDao())
    }
    private val orderBouquetsRepository: OfflineOrdersWithBouquetsRepository by lazy {
        OfflineOrdersWithBouquetsRepository(AppDatabase.getInstance(context).orderWithBouquetsDao())
    }
    private val remoteKeyRepository: OfflineRemoteKeyRepository by lazy {
        OfflineRemoteKeyRepository(AppDatabase.getInstance(context).remoteKeysDAO())
    }
    override val orderBouquetRestRepository: RestOrderBouquetRepository by lazy {
        RestOrderBouquetRepository(
            MyServerService.getInstance(),
            orderBouquetsRepository
        )
    }
    override val orderRestRepository: RestOrderRepository by lazy {
        RestOrderRepository(
            MyServerService.getInstance(),
            orderRepository,
            remoteKeyRepository,
            AppDatabase.getInstance(context)
        )
    }
    override val bouquetRestRepository: RestBouquetRepository by lazy {
        RestBouquetRepository(
            MyServerService.getInstance(),
            bouquetRepository,
            orderBouquetsRepository,
            remoteKeyRepository,
            orderBouquetRestRepository,
            orderRestRepository,
            AppDatabase.getInstance(context)
        )
    }
    override val userRestRepository: RestUserRepository by lazy {
        RestUserRepository(
            MyServerService.getInstance(),
            userRepository
        )
    }
}