package com.example.flowershopapp.Entities.Repository.Order

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.PagingSource
import com.example.flowershopapp.Database.AppContainer
import com.example.flowershopapp.Entities.DAO.OrderDAO
import com.example.flowershopapp.Entities.Model.Bouquet
import com.example.flowershopapp.Entities.Model.Order
import com.example.flowershopapp.Entities.Model.OrderByDate
import kotlinx.coroutines.flow.Flow

class OfflineOrderRepository(private val orderDAO: OrderDAO) : OrderRepository {
    override fun getOrdersByUser(id: Int): Flow<PagingData<Order>> = Pager(
        config = PagingConfig(
            pageSize = AppContainer.LIMIT,
            enablePlaceholders = false
        ),
        pagingSourceFactory = { orderDAO.getOrdersByUser(id) }
    ).flow

    override suspend fun getBouquetsByOrder(orderId: Int): Flow<List<Bouquet>> =
        orderDAO.getBouquetsByOrder(orderId)

    override suspend fun insert(order: Order) = orderDAO.insert(order)
    override suspend fun insertWithReturn(order: Order): Order {
        TODO("Not yet implemented")
    }

    override suspend fun getOrdersByDate(
        userId: Int,
        starDate: String,
        endDate: String
    ): Flow<OrderByDate> {
        TODO("Not yet implemented")
    }

    override suspend fun delete(order: Order) = orderDAO.delete(order)
    fun getOrdersByUserPagingSource(id: Int): PagingSource<Int, Order> =
        orderDAO.getOrdersByUser(id)

    suspend fun deleteAll() = orderDAO.deleteAll()
    override suspend fun getById(id: Int): Order = orderDAO.getById(id)
    suspend fun insertOrders(orders: List<Order>) =
        orderDAO.insert(*orders.toTypedArray())
}