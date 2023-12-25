package com.example.flowershopapp.API.Repository

import android.util.Log
import com.example.flowershopapp.API.Model.OrderBouquetCrossRef
import com.example.flowershopapp.API.Model.OrderBouquetCrossRefRemote
import com.example.flowershopapp.API.MyServerService
import com.example.flowershopapp.Entities.Model.OrderBouquetCrossRef
import com.example.flowershopapp.Entities.Repository.OrderBouquets.OfflineOrdersWithBouquetsRepository
import com.example.flowershopapp.Entities.Repository.OrderBouquets.OrdersWithBouquetsRepository

class RestOrderBouquetRepository(
    private val service: MyServerService,
    private val dbOrderBouquetRepository: OfflineOrdersWithBouquetsRepository,
) : OrdersWithBouquetsRepository {
    override suspend fun getAll(): List<OrderBouquetCrossRef> {
        Log.d(RestOrderBouquetRepository::class.simpleName, "Get OrderBouquets")

        val existOrderBouquets = dbOrderBouquetRepository.getAll().toMutableList()
        val serverOrderBouquets = service.getOrdersBouquets().map { it.OrderBouquetCrossRef() }


        val toDelete = existOrderBouquets.filterNot { serverOrderBouquets.contains(it) }
        toDelete.forEach { dbOrderBouquetRepository.delete(it) }


        val toAdd = serverOrderBouquets.filterNot { existOrderBouquets.contains(it) }
        toAdd.forEach { dbOrderBouquetRepository.insert(it) }


        return dbOrderBouquetRepository.getAll()
    }

    override suspend fun insert(orderBouquet: OrderBouquetCrossRef) {
        service.createOrderBouquet(orderBouquet.OrderBouquetCrossRefRemote())
    }

    override suspend fun delete(orderBouquet: OrderBouquetCrossRef) {
        service.deleteOrderBouquet(orderBouquet.orderId)
    }

    override suspend fun deleteAll() {
        TODO("Not yet implemented")
    }
}