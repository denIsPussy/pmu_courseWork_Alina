package com.example.flowershopapp.Entities.DAO

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.example.flowershopapp.Entities.Model.OrderBouquetCrossRef

@Dao
interface OrdersWithBouquet {
    @Query("select * from orderbouquetcrossref")
    fun getAll(): List<OrderBouquetCrossRef>

    @Insert
    suspend fun insert(vararg order: OrderBouquetCrossRef)

    @Delete
    suspend fun delete(order: OrderBouquetCrossRef)

    @Query("DELETE FROM orderbouquetcrossref")
    suspend fun deleteAll()
}