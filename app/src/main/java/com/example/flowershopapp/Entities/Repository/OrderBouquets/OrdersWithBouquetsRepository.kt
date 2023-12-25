package com.example.flowershopapp.Entities.Repository.OrderBouquets

import com.example.flowershopapp.Entities.Model.OrderBouquetCrossRef

interface OrdersWithBouquetsRepository {
    suspend fun getAll(): List<OrderBouquetCrossRef>
    suspend fun insert(order: OrderBouquetCrossRef)
    suspend fun delete(order: OrderBouquetCrossRef)
    suspend fun deleteAll()
}