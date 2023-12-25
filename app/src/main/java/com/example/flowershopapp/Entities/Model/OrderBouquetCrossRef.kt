package com.example.flowershopapp.Entities.Model

import androidx.room.Entity

@Entity(primaryKeys = ["orderId", "bouquetId"])
class OrderBouquetCrossRef (
    val orderId: Int,
    val bouquetId: Int,
    val count: Int
)