package com.example.flowershopapp.ComposeUI.Bouquet

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import com.example.flowershopapp.ComposeUI.Network.NetworkViewModel
import com.example.flowershopapp.Entities.Model.Bouquet
import com.example.flowershopapp.Entities.Repository.Bouquet.BouquetRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class BouquetCatalogViewModel(
    private val bouquetRepository: BouquetRepository
) : NetworkViewModel() {

    var bouquetListUiState: Flow<PagingData<Bouquet>> = MutableStateFlow<PagingData<Bouquet>>(PagingData.empty())

    private val _bouquetPopulateListUiState = MutableStateFlow<List<Bouquet>>(emptyList())
    var bouquetPopulateListUiState: List<Bouquet> = emptyList()

    init {
        collectAllBouquets()
        collectPopulateBouquets()
    }

    private fun collectAllBouquets() {
        runInScope(
            actionSuccess = {
                bouquetListUiState = bouquetRepository.getAll()
            }
        )
    }
    fun collectPopulateBouquets() {
        runInScope(
            actionSuccess = {
                bouquetPopulateListUiState = bouquetRepository.getPopulateBouquets()
            },
            actionError = {
                bouquetPopulateListUiState = emptyList()
            }
        )
    }
}