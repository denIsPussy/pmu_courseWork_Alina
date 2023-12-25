package com.example.flowershopapp.Entities.Model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Ignore
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "orders",
    foreignKeys = [ForeignKey(
        entity = User::class,
        parentColumns = arrayOf("userId"),
        childColumns = arrayOf("userId"),
        onDelete = ForeignKey.CASCADE
    )],
    indices = [Index(value = ["userId"])]
)
data class Order(
    @PrimaryKey(autoGenerate = true)
    val orderId: Int?,
    @ColumnInfo(name = "date")
    val date: String,
    @ColumnInfo(name = "sum")
    val sum: Int,
    val userId: Int,
) {

    @Ignore
    constructor(
        userId: Int,
        date: String,
        sum: Int,
    ) : this(null, date, sum, userId)

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false
        other as Order
        if (orderId != other.orderId) return false
        return true
    }

    override fun hashCode(): Int {
        return orderId ?: -1
    }
}
