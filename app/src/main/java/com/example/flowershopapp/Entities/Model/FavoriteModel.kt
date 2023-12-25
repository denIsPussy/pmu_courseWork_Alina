package com.example.flowershopapp.Entities.Model

import androidx.lifecycle.ViewModel
import com.example.flowershopapp.R
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class FavoriteModel : ViewModel() {
    private val _favoriteList = MutableStateFlow<List<Bouquet>>(emptyList())
    val bouquets: StateFlow<List<Bouquet>> = _favoriteList.asStateFlow()

    fun addBouquet(bouquet: Bouquet): Int {
        val currentList = _favoriteList.value.toMutableList()
        return if (currentList.contains(bouquet)) {
            currentList.remove(bouquet)
            _favoriteList.value = currentList
            R.drawable.heart_black
        } else {
            currentList.add(bouquet)
            _favoriteList.value = currentList
            R.drawable.heart_red
        }
    }

    fun addBouquets(vararg bouquets: Bouquet) {
        val currentList = _favoriteList.value.toMutableList()
        currentList.addAll(bouquets)
        _favoriteList.value = currentList
    }

    fun removeBouquets(bouquetRemove: Bouquet) {
        val currentList = _favoriteList.value.toMutableList()
        currentList.removeAll { it.bouquetId == bouquetRemove.bouquetId }
        _favoriteList.value = currentList
    }

    fun containsBouquet(bouquet: Bouquet): Int {
        val currentList = _favoriteList.value
        return if (currentList.contains(bouquet)) R.drawable.heart_red
        else R.drawable.heart_black
    }

    fun clearBouquets() {
        _favoriteList.value = emptyList()
    }

    companion object {
        val instance: FavoriteModel by lazy { FavoriteModel() }
    }
}