package com.example.flowershopapp.Entities.Model

data class OrderByDate(
    val orderCount: Int,
    val totalSum: Int,
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as OrderByDate

        if (orderCount != other.orderCount) return false
        return totalSum == other.totalSum
    }

    override fun hashCode(): Int {
        var result = orderCount
        result = 31 * result + totalSum
        return result
    }
}
