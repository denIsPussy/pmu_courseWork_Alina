package com.example.flowershopapp.Entities.Repository.OrderBouquets

import com.example.flowershopapp.Entities.DAO.OrdersWithBouquet
import com.example.flowershopapp.Entities.Model.OrderBouquetCrossRef

class OfflineOrdersWithBouquetsRepository(private val orderBouquetsDAO: OrdersWithBouquet) : OrdersWithBouquetsRepository {
    override suspend fun getAll() = orderBouquetsDAO.getAll()
    override suspend fun insert(order: OrderBouquetCrossRef) = orderBouquetsDAO.insert(order)
    override suspend fun delete(order: OrderBouquetCrossRef) = orderBouquetsDAO.delete(order)
    override suspend fun deleteAll() = orderBouquetsDAO.deleteAll()

    suspend fun insertAll(orderBouquet: List<OrderBouquetCrossRef>) =
        orderBouquetsDAO.insert(*orderBouquet.toTypedArray())
}