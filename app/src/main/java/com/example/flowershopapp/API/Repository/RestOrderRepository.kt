package com.example.flowershopapp.API.Repository

import android.util.Log
import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.example.flowershopapp.API.Mediator.OrderRemoteMediator
import com.example.flowershopapp.API.Model.toBouquet
import com.example.flowershopapp.API.Model.toOrder
import com.example.flowershopapp.API.Model.toOrderByDate
import com.example.flowershopapp.API.Model.toOrderRemote
import com.example.flowershopapp.API.MyServerService
import com.example.flowershopapp.Database.AppContainer
import com.example.flowershopapp.Database.AppDatabase
import com.example.flowershopapp.Database.RemoteKeys.Repository.OfflineRemoteKeyRepository
import com.example.flowershopapp.Entities.Model.Bouquet
import com.example.flowershopapp.Entities.Model.Order
import com.example.flowershopapp.Entities.Model.OrderByDate
import com.example.flowershopapp.Entities.Repository.Order.OfflineOrderRepository
import com.example.flowershopapp.Entities.Repository.Order.OrderRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

class RestOrderRepository(
    private val service: MyServerService,
    private val dbOrderRepository: OfflineOrderRepository,
    private val dbRemoteKeyRepository: OfflineRemoteKeyRepository,
    private val database: AppDatabase
) : OrderRepository {
    override suspend fun getBouquetsByOrder(orderId: Int): Flow<List<Bouquet>> {
        var bouquets = service.getAllBouquets()
        var bouquetsIdByOrderId =
            service.getOrdersBouquets().filter { it.orderId == orderId }.map { it.bouquetId }
        return flowOf(bouquets.map { bouquet ->
            bouquet.toBouquet()
        }.filter { it.bouquetId in bouquetsIdByOrderId })
    }

    override suspend fun getOrdersByDate(
        userId: Int,
        startDate: String,
        endDate: String
    ): Flow<OrderByDate> {
        return flowOf(service.getOrdersByDate(userId, startDate, endDate).toOrderByDate())
    }


    override fun getOrdersByUser(id: Int): Flow<PagingData<Order>> {
        Log.d(RestOrderRepository::class.simpleName, "Get Orders")

        val pagingSourceFactory = { dbOrderRepository.getOrdersByUserPagingSource(id) }
        @OptIn(ExperimentalPagingApi::class)
        return Pager(
            config = PagingConfig(
                pageSize = AppContainer.LIMIT,
                enablePlaceholders = false
            ),
            remoteMediator = OrderRemoteMediator(
                service,
                dbOrderRepository,
                dbRemoteKeyRepository,
                database
            ),
            pagingSourceFactory = pagingSourceFactory
        ).flow
    }

    override suspend fun insert(order: Order) {
        service.createOrder(order.toOrderRemote()).toOrder()
    }

    override suspend fun insertWithReturn(order: Order): Order {
        return service.createOrder(order.toOrderRemote()).toOrder()
    }

    override suspend fun delete(order: Order) {
        order.orderId?.let { service.deleteOrder(it).toOrder() }
    }

    override suspend fun getById(id: Int): Order {
        return service.getOrder(id).toOrder()
    }
}