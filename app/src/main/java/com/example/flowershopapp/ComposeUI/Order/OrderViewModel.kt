package com.example.flowershopapp.ComposeUI.User

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import com.example.flowershopapp.ComposeUI.Network.NetworkViewModel
import com.example.flowershopapp.Entities.Model.AuthModel
import com.example.flowershopapp.Entities.Model.Bouquet
import com.example.flowershopapp.Entities.Model.Order
import com.example.flowershopapp.Entities.Model.OrderBouquetCrossRef
import com.example.flowershopapp.Entities.Model.OrderByDate
import com.example.flowershopapp.Entities.Model.OrdersWithBouquets
import com.example.flowershopapp.Entities.Repository.Order.OrderRepository
import com.example.flowershopapp.Entities.Repository.OrderBouquets.OrdersWithBouquetsRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class OrderViewModel(
    savedStateHandle: SavedStateHandle,
    private val orderRepository: OrderRepository,
    private val orderBouquetRepository: OrdersWithBouquetsRepository,
) : NetworkViewModel() {

    private val userId: Int = AuthModel.currentUser.userId!!
    private val orderId: Int = savedStateHandle["id"] ?: -1

    var ordersListUiState: Flow<PagingData<Order>> = MutableStateFlow<PagingData<Order>>(PagingData.empty())
        private set

    private val _orderBouquetListUiState = MutableStateFlow<List<Pair<Bouquet, Int>>>(emptyList())
    val orderBouquetListUiState: Flow<List<Pair<Bouquet, Int>>> = _orderBouquetListUiState.asStateFlow()

    private val _orderByDateListUiState = MutableStateFlow<OrderByDate>(OrderByDate(listOf(), 0, 0))
    val orderByDateListUiState: Flow<OrderByDate> = _orderByDateListUiState.asStateFlow()

    init {
        runInScope(
            actionSuccess = {
                ordersListUiState = orderRepository.getOrdersByUser(userId)
            }
        )
        loadBouquetsByOrder()
    }

    private fun loadBouquetsByOrder() {
        runInScope(
            actionSuccess = {
                if (orderId != -1) {
                    orderRepository.getBouquetsByOrder(orderId).collect { bouquets ->
                        withContext(Dispatchers.IO) {
                            val orderBouquet = orderBouquetRepository.getAll()
                            val orderBouquetMap = orderBouquet.associateBy { it.bouquetId }
                            _orderBouquetListUiState.value = bouquets.map { bouquet ->
                                val count = orderBouquetMap[bouquet.bouquetId]?.count ?: 0
                                Pair(bouquet, count)
                            }
                        }
                    }
                }
            },
            actionError = {
                _orderBouquetListUiState.value = emptyList()
            }
        )
    }

    fun loadStatistics(startDate: String, endDate: String) {
        runInScope(
            actionSuccess = {
                _orderByDateListUiState.value =
                    orderRepository.getOrdersByDate(userId, startDate, endDate).first()
            },
            actionError = {
                _orderByDateListUiState.value = OrderByDate(emptyList(), 0, 0)
            }
        )
    }

    fun createOrder(order: Order, bouquetsPair: List<Pair<Bouquet, Int>>) {
        runInScope(
            actionSuccess = {
                val createdOrder = orderRepository.insertWithReturn(order)
                bouquetsPair.forEach { bouquetPair ->
                    orderBouquetRepository.insert(
                        OrderBouquetCrossRef(
                            createdOrder.orderId!!,
                            bouquetPair.first.bouquetId!!,
                            bouquetPair.second
                        )
                    )
                }
            }
        )
    }
}

data class UserWithOrdersUiState(
    val userDetails: UserWithOrdersDetails = UserWithOrdersDetails()
)

data class UserWithOrdersDetails(
    val orders: List<OrdersWithBouquets> = listOf()
)

fun List<OrdersWithBouquets>.toDetails(): UserWithOrdersDetails = UserWithOrdersDetails(
    orders = this,
)

fun List<OrdersWithBouquets>.toUiState(): UserWithOrdersUiState = UserWithOrdersUiState(
    userDetails = this.toDetails()
)
