package com.example.flowershopapp.Entities.Model

import androidx.room.Embedded
import androidx.room.Junction
import androidx.room.Relation

data class OrdersWithBouquets(
    @Embedded val order: Order,
    @Relation(
        parentColumn = "orderId",
        entityColumn = "bouquetId",
        associateBy = Junction(OrderBouquetCrossRef::class)
    )
    val bouquets: List<Bouquet>
)