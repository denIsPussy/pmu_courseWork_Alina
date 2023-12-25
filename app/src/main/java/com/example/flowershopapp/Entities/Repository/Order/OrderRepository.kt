package com.example.flowershopapp.Entities.Repository.Order

import androidx.paging.PagingData
import com.example.flowershopapp.Entities.Model.Bouquet
import com.example.flowershopapp.Entities.Model.Order
import com.example.flowershopapp.Entities.Model.OrderByDate
import kotlinx.coroutines.flow.Flow

interface OrderRepository {
    suspend fun getBouquetsByOrder(orderId: Int): Flow<List<Bouquet>>
    fun getOrdersByUser(id: Int): Flow<PagingData<Order>>
    suspend fun insert(order: Order)
    suspend fun insertWithReturn(order: Order): Order
    suspend fun getOrdersByDate(userId: Int, starDate: String, endDate: String): Flow<OrderByDate>
    suspend fun delete(order: Order)
    suspend fun getById(id: Int): Order
}