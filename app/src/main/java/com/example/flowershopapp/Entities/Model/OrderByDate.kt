package com.example.flowershopapp.Entities.Model

data class OrderByDate(
    val orders: List<Order>,
    val orderCount: Int,
    val totalSum: Int,
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as OrderByDate

        if (orders != other.orders) return false
        if (orderCount != other.orderCount) return false
        return totalSum == other.totalSum
    }

    override fun hashCode(): Int {
        var result = orders.hashCode()
        result = 31 * result + orderCount
        result = 31 * result + totalSum
        return result
    }
}
