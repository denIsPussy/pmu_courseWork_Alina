package com.example.flowershopapp.Entities.Model

import androidx.lifecycle.ViewModel
import com.example.flowershopapp.R
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow


class CartModel : ViewModel() {
    private val _cartList = MutableStateFlow<List<Pair<Bouquet, Int>>>(emptyList())
    val bouquets: StateFlow<List<Pair<Bouquet, Int>>> = _cartList.asStateFlow()

    private val _totalSum = MutableStateFlow<Int>(0)
    val totalSum: StateFlow<Int> = _totalSum.asStateFlow()

    fun updateList(bouquet: Bouquet): Int {
        val currentList = _cartList.value.toMutableList()
        val isContains =
            currentList.find { bouquetPair -> bouquetPair.first.bouquetId == bouquet.bouquetId } != null
        return if (isContains) {
            val currElement = currentList.find { it.first.bouquetId == bouquet.bouquetId }
            currentList.remove(currElement)
            _cartList.value = currentList
            calculatingTotalSum(currentList.toList())
            R.drawable.shopping_cart
        } else {
            currentList.add(Pair(bouquet, 1))
            _cartList.value = currentList
            calculatingTotalSum(currentList.toList())
            R.drawable.shopping_cart_fill
        }
    }

    fun updateCount(bouquet: Bouquet, isPlus: Boolean) {
        val currentList = _cartList.value.toMutableList()
        val currElement = currentList.find { it.first.bouquetId == bouquet.bouquetId }

        if (currElement != null) {
            val indexCurrElement = currentList.indexOf(currElement)
            val newCount = if (isPlus) currElement.second + 1 else currElement.second - 1

            if (newCount == 0) {
                currentList.removeAt(indexCurrElement)
                _cartList.value = currentList
                calculatingTotalSum(currentList.toList())
                return
            }

            val replaceElement = Pair(currElement.first, newCount)
            currentList.removeAt(indexCurrElement)
            currentList.add(indexCurrElement, replaceElement)

            _cartList.value = currentList
            calculatingTotalSum(currentList.toList())
        }
    }

    fun containsBouquet(bouquet: Bouquet): Int {
        val currentList = _cartList.value.toMutableList()
        return if (currentList.find { it.first.bouquetId == bouquet.bouquetId } != null) {
            R.drawable.shopping_cart_fill
        } else {
            R.drawable.shopping_cart
        }
    }

    fun calculatingTotalSum(list: List<Pair<Bouquet, Int>>) {
        _totalSum.value = list.sumOf { it.first.price * it.second }
    }

    fun getTotalSum(): Int {
        return _cartList.value.sumOf { it.first.price * it.second }
    }

    fun getCount(): Int {
        val currentList = _cartList.value.toMutableList()
        return currentList.size
    }

    fun clearBouquets() {
        _cartList.value = emptyList()
        calculatingTotalSum(emptyList())
    }

    companion object {
        val instance: CartModel by lazy { CartModel() }
    }
}