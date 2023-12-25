package com.example.flowershopapp.Entities.Repository.Bouquet

import androidx.paging.PagingData
import com.example.flowershopapp.Entities.Model.Bouquet
import kotlinx.coroutines.flow.Flow

interface BouquetRepository {
    fun getAll(): Flow<PagingData<Bouquet>>
    suspend fun getPopulateBouquets(): Flow<List<Bouquet>>
    suspend fun getBouquet(id: Int): Bouquet
    suspend fun insert(bouquet: Bouquet)
    suspend fun update(bouquet: Bouquet)
    suspend fun delete(bouquet: Bouquet)
}