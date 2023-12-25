package com.example.flowershopapp.API.Mediator

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import androidx.room.withTransaction
import com.example.flowershopapp.API.Model.toOrder
import com.example.flowershopapp.API.MyServerService
import com.example.flowershopapp.Database.AppDatabase
import com.example.flowershopapp.Database.RemoteKeys.Model.RemoteKeyType
import com.example.flowershopapp.Database.RemoteKeys.Model.RemoteKeys
import com.example.flowershopapp.Database.RemoteKeys.Repository.OfflineRemoteKeyRepository
import com.example.flowershopapp.Entities.Model.AuthModel
import com.example.flowershopapp.Entities.Model.Order
import com.example.flowershopapp.Entities.Repository.Order.OfflineOrderRepository
import retrofit2.HttpException
import java.io.IOException

@OptIn(ExperimentalPagingApi::class)
class OrderRemoteMediator(
    private val service: MyServerService,
    private val dbOrderRepository: OfflineOrderRepository,
    private val dbRemoteKeyRepository: OfflineRemoteKeyRepository,
    private val database: AppDatabase
) : RemoteMediator<Int, Order>() {

    override suspend fun initialize(): InitializeAction {
        return InitializeAction.LAUNCH_INITIAL_REFRESH
    }

    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, Order>
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
            val userId = AuthModel.currentUser.userId ?: throw Exception("Error get orders by user")
            val orders = service.getOrders(userId, page, state.config.pageSize).map { it.toOrder() }
            val endOfPaginationReached = orders.isEmpty()
            database.withTransaction {
                if (loadType == LoadType.REFRESH) {
                    dbRemoteKeyRepository.deleteRemoteKey(RemoteKeyType.ORDER)
                    dbOrderRepository.deleteAll()
                }
                val prevKey = if (page == 1) null else page - 1
                val nextKey = if (endOfPaginationReached) null else page + 1
                val keys = orders.map {
                    it.orderId?.let { it1 ->
                        RemoteKeys(
                            entityId = it1,
                            type = RemoteKeyType.ORDER,
                            prevKey = prevKey,
                            nextKey = nextKey
                        )
                    }
                }

                dbRemoteKeyRepository.createRemoteKeys(keys)
                dbOrderRepository.insertOrders(orders)
            }
            return MediatorResult.Success(endOfPaginationReached = endOfPaginationReached)
        } catch (exception: IOException) {
            return MediatorResult.Error(exception)
        } catch (exception: HttpException) {
            return MediatorResult.Error(exception)
        }
    }

    private suspend fun getRemoteKeyForLastItem(state: PagingState<Int, Order>): RemoteKeys? {
        return state.pages.lastOrNull { it.data.isNotEmpty() }?.data?.lastOrNull()
            ?.let { Order ->
                Order.orderId?.let {
                    dbRemoteKeyRepository.getAllRemoteKeys(
                        it,
                        RemoteKeyType.ORDER
                    )
                }
            }
    }

    private suspend fun getRemoteKeyForFirstItem(state: PagingState<Int, Order>): RemoteKeys? {
        return state.pages.firstOrNull { it.data.isNotEmpty() }?.data?.firstOrNull()
            ?.let { Order ->
                Order.orderId?.let {
                    dbRemoteKeyRepository.getAllRemoteKeys(
                        it,
                        RemoteKeyType.ORDER
                    )
                }
            }
    }

    private suspend fun getRemoteKeyClosestToCurrentPosition(
        state: PagingState<Int, Order>
    ): RemoteKeys? {
        return state.anchorPosition?.let { position ->
            state.closestItemToPosition(position)?.orderId?.let { OrderUid ->
                dbRemoteKeyRepository.getAllRemoteKeys(OrderUid, RemoteKeyType.ORDER)
            }
        }
    }
}