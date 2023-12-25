package com.example.flowershopapp.API.Mediator

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import androidx.room.withTransaction
import com.example.flowershopapp.API.Model.toBouquet
import com.example.flowershopapp.API.MyServerService
import com.example.flowershopapp.API.Repository.RestOrderBouquetRepository
import com.example.flowershopapp.API.Repository.RestOrderRepository
import com.example.flowershopapp.Database.AppDatabase
import com.example.flowershopapp.Database.RemoteKeys.Model.RemoteKeyType
import com.example.flowershopapp.Database.RemoteKeys.Model.RemoteKeys
import com.example.flowershopapp.Database.RemoteKeys.Repository.OfflineRemoteKeyRepository
import com.example.flowershopapp.Entities.Model.AuthModel
import com.example.flowershopapp.Entities.Model.Bouquet
import com.example.flowershopapp.Entities.Repository.Bouquet.OfflineBouquetRepository
import com.example.flowershopapp.Entities.Repository.OrderBouquets.OfflineOrdersWithBouquetsRepository
import retrofit2.HttpException
import java.io.IOException

@OptIn(ExperimentalPagingApi::class)
class BouquetRemoteMediator(
    private val service: MyServerService,
    private val dbBouquetRepository: OfflineBouquetRepository,
    private val dbOrderBouquetRepository: OfflineOrdersWithBouquetsRepository,
    private val dbRemoteKeyRepository: OfflineRemoteKeyRepository,
    private val orderBouquetRestRepository: RestOrderBouquetRepository,
    private val orderRestRepository: RestOrderRepository,
    private val database: AppDatabase
) : RemoteMediator<Int, Bouquet>() {

    override suspend fun initialize(): InitializeAction {
        return InitializeAction.LAUNCH_INITIAL_REFRESH
    }

    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, Bouquet>
    ): MediatorResult {
        val page = when (loadType) {
            LoadType.REFRESH -> {
                val remoteKeys = getRemoteKeyClosestToCurrentPosition(state)
                remoteKeys?.nextKey?.minus(1) ?: 1
            }

            LoadType.PREPEND -> {
                val remoteKeys = getRemoteKeyForFirstItem(state)
                remoteKeys?.prevKey
                    ?: return MediatorResult.Success(endOfPaginationReached = remoteKeys != null)
            }

            LoadType.APPEND -> {
                val remoteKeys = getRemoteKeyForLastItem(state)
                remoteKeys?.nextKey
                    ?: return MediatorResult.Success(endOfPaginationReached = remoteKeys != null)
            }
        }

        try {
            val bouquets = service.getBouquets(page, state.config.pageSize).map { it.toBouquet() }
            val endOfPaginationReached = bouquets.isEmpty()
            database.withTransaction {
                if (loadType == LoadType.REFRESH) {
                    dbRemoteKeyRepository.deleteRemoteKey(RemoteKeyType.BOUQUET)
                    dbBouquetRepository.deleteAll()
                    dbOrderBouquetRepository.deleteAll()
                }
                val prevKey = if (page == 1) null else page - 1
                val nextKey = if (endOfPaginationReached) null else page + 1
                val keys = bouquets.map {
                    it.bouquetId?.let { it1 ->
                        RemoteKeys(
                            entityId = it1,
                            type = RemoteKeyType.BOUQUET,
                            prevKey = prevKey,
                            nextKey = nextKey
                        )
                    }
                }
                //orderRestRepository.getOrdersByUser(AuthModel.currentUser.userId!!)
                orderBouquetRestRepository.getAll()
                dbRemoteKeyRepository.createRemoteKeys(keys)
                dbBouquetRepository.insertBouquets(bouquets)
            }
            return MediatorResult.Success(endOfPaginationReached = endOfPaginationReached)
        } catch (exception: IOException) {
            return MediatorResult.Error(exception)
        } catch (exception: HttpException) {
            return MediatorResult.Error(exception)
        }
    }

    private suspend fun getRemoteKeyForLastItem(state: PagingState<Int, Bouquet>): RemoteKeys? {
        return state.pages.lastOrNull { it.data.isNotEmpty() }?.data?.lastOrNull()
            ?.let { bouquet ->
                bouquet.bouquetId?.let {
                    dbRemoteKeyRepository.getAllRemoteKeys(
                        it,
                        RemoteKeyType.BOUQUET
                    )
                }
            }
    }

    private suspend fun getRemoteKeyForFirstItem(state: PagingState<Int, Bouquet>): RemoteKeys? {
        return state.pages.firstOrNull { it.data.isNotEmpty() }?.data?.firstOrNull()
            ?.let { bouquet ->
                bouquet.bouquetId?.let {
                    dbRemoteKeyRepository.getAllRemoteKeys(
                        it,
                        RemoteKeyType.BOUQUET
                    )
                }
            }
    }

    private suspend fun getRemoteKeyClosestToCurrentPosition(
        state: PagingState<Int, Bouquet>
    ): RemoteKeys? {
        return state.anchorPosition?.let { position ->
            state.closestItemToPosition(position)?.bouquetId?.let { bouquetUid ->
                dbRemoteKeyRepository.getAllRemoteKeys(bouquetUid, RemoteKeyType.BOUQUET)
            }
        }
    }
}