package com.example.flowershopapp.Entities.Model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Ignore
import androidx.room.PrimaryKey

@Entity(tableName = "bouquets")
data class Bouquet(
    @PrimaryKey(autoGenerate = true)
    val bouquetId: Int?,
    @ColumnInfo(name = "name")
    val name: String,
    @ColumnInfo(name = "quantityOfFlowers")
    val quantityOfFlowers: Int,
    @ColumnInfo(name = "duration")
    val price: Int,
    @ColumnInfo(typeAffinity = ColumnInfo.BLOB, name = "image")
    val image: ByteArray? = null,
) {

    @Ignore
    constructor(
        name: String,
        quantityOfFlowers: Int,
        price: Int,
        image: ByteArray,
    ) : this(null, name, quantityOfFlowers, price, image)

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false
        other as Bouquet
        if (bouquetId != other.bouquetId) return false
        return true
    }

    override fun hashCode(): Int {
        return bouquetId ?: -1
    }
}
