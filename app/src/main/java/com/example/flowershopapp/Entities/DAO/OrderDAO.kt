package com.example.flowershopapp.Entities.DAO

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.example.flowershopapp.Entities.Model.Bouquet
import com.example.flowershopapp.Entities.Model.Order
import com.example.flowershopapp.Entities.Model.OrdersWithBouquets
import kotlinx.coroutines.flow.Flow

@Dao
interface OrderDAO {
    @Query(
        """
        SELECT orders.* FROM orders
        WHERE userId = :userId
    """
    )
    fun getOrdersByUser(userId: Int): PagingSource<Int, Order>

    @Query(
        """
        SELECT bouquets.* FROM bouquets
        INNER JOIN orderbouquetcrossref ON bouquets.bouquetId = orderbouquetcrossref.bouquetId
        WHERE orderbouquetcrossref.orderId = :orderId
    """
    )
    fun getBouquetsByOrder(orderId: Int): Flow<List<Bouquet>>

    @Query("select * from orders")
    fun getOrdersWithBouquet(): Flow<List<OrdersWithBouquets>>

    @Query("select * from orders where orderId = :id")
    fun getOrderWithBouquet(id: Int): OrdersWithBouquets

    @Insert
    suspend fun insert(vararg order: Order)

    @Delete
    suspend fun delete(order: Order)

    @Query("DELETE FROM orders")
    suspend fun deleteAll()

    @Query("select * from orders where orderId = :id")
    suspend fun getById(id: Int): Order
}