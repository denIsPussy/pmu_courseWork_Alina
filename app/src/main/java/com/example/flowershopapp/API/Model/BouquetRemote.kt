package com.example.flowershopapp.API.Model

import com.example.flowershopapp.Entities.Model.Bouquet
import kotlinx.serialization.Serializable
import java.util.Base64

@Serializable
data class BouquetRemote(
    val id: Int? = 0,
    val name: String = "",
    val quantityOfFlowers: Int = 0,
    val price: Int = 0,
    val image: String = "",
)

fun BouquetRemote.toBouquet(): Bouquet = Bouquet(
    id,
    name,
    quantityOfFlowers,
    price,
    Base64.getDecoder().decode(image)
)

fun Bouquet.toBouquetRemote(): BouquetRemote = BouquetRemote(
    bouquetId,
    name,
    quantityOfFlowers,
    price,
    image.toString()
)