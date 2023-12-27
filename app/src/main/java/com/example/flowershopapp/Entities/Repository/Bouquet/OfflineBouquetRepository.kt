package com.example.flowershopapp.Entities.Repository.Bouquet

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.PagingSource
import com.example.flowershopapp.Database.AppContainer
import com.example.flowershopapp.Entities.DAO.BouquetDAO
import com.example.flowershopapp.Entities.Model.Bouquet
import kotlinx.coroutines.flow.Flow

class OfflineBouquetRepository(private val bouquetDAO: BouquetDAO) : BouquetRepository {
    override fun getAll(): Flow<PagingData<Bouquet>> = Pager(
        config = PagingConfig(
            pageSize = AppContainer.LIMIT,
            enablePlaceholders = false
        ),
        pagingSourceFactory = bouquetDAO::getAll
    ).flow

    override suspend fun getPopulateBouquets(): List<Bouquet> {
        TODO("Not yet implemented")
    }

    override suspend fun getBouquet(id: Int): Bouquet = bouquetDAO.getBouquet(id)
    override suspend fun insert(bouquet: Bouquet) = bouquetDAO.insert(bouquet)
    override suspend fun update(bouquet: Bouquet) = bouquetDAO.update(bouquet)
    override suspend fun delete(bouquet: Bouquet) = bouquetDAO.delete(bouquet)
    suspend fun deleteAll() = bouquetDAO.deleteAll()
    fun getAllBouquetsPagingSource(): PagingSource<Int, Bouquet> = bouquetDAO.getAll()
    suspend fun insertBouquets(bouquets: List<Bouquet>) =
        bouquetDAO.insert(*bouquets.toTypedArray())
}