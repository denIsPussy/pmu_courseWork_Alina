package com.example.flowershopapp.ComposeUI.User

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import com.example.flowershopapp.Entities.Model.AuthModel
import com.example.flowershopapp.Entities.Model.Bouquet
import com.example.flowershopapp.Entities.Model.Order
import com.example.flowershopapp.Entities.Model.OrderBouquetCrossRef
import com.example.flowershopapp.Entities.Model.OrderByDate
import com.example.flowershopapp.Entities.Model.OrdersWithBouquets
import com.example.flowershopapp.Entities.Repository.Order.OrderRepository
import com.example.flowershopapp.Entities.Repository.OrderBouquets.OfflineOrdersWithBouquetsRepository
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
) : ViewModel() {

    private val userId: Int = AuthModel.currentUser.userId!!
    private val orderId: Int = savedStateHandle["id"] ?: -1

    var ordersListUiState: Flow<PagingData<Order>> = orderRepository.getOrdersByUser(userId)
        private set

    private val _orderBouListUiState = MutableStateFlow<List<Pair<Bouquet, Int>>>(emptyList())
    val orderBouListUiState: Flow<List<Pair<Bouquet, Int>>> = _orderBouListUiState.asStateFlow()

    private val _orderListUiState = MutableStateFlow<OrderByDate>(OrderByDate(0, 0))
    val orderListUiState: Flow<OrderByDate> = _orderListUiState.asStateFlow()

    init {
        loadBouquetsByOrder()
    }

    private fun loadBouquetsByOrder() {
        viewModelScope.launch {
            if (orderId != -1) {
                //_orderBouListUiState.value = orderRepository.getBouquetsByOrder(orderId).first()
                orderRepository.getBouquetsByOrder(orderId).collect { bouquets ->
                    withContext(Dispatchers.IO){
                        val orderBouquet = orderBouquetRepository.getAll()
                        val orderBouquetMap = orderBouquet.associateBy { it.bouquetId }
                        _orderBouListUiState.value = bouquets.map { bouquet ->
                            val count = orderBouquetMap[bouquet.bouquetId]?.count ?: 0 // Если нет совпадения, используем 0
                            Pair(bouquet, count)
                        }
                    }
                }
            }
        }
    }
    fun loadStatistics(startDate: String, endDate: String) {
        viewModelScope.launch {
            _orderListUiState.value = orderRepository.getOrdersByDate(userId, startDate, endDate).first()
        }
    }

    fun createOrder(order: Order, bouquetsPair: List<Pair<Bouquet, Int>>){
        viewModelScope.launch {
            val createdOrder = orderRepository.insertWithReturn(order)
            bouquetsPair.forEach { bouquetPair ->
                orderBouquetRepository.insert(OrderBouquetCrossRef(createdOrder.orderId!!, bouquetPair.first.bouquetId!!, bouquetPair.second))
            }
        }
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