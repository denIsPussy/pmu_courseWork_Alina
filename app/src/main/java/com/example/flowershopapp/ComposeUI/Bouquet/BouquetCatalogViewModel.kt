package com.example.flowershopapp.ComposeUI.Bouquet

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import com.example.flowershopapp.Entities.Model.Bouquet
import com.example.flowershopapp.Entities.Repository.Bouquet.BouquetRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class BouquetCatalogViewModel(
    private val bouquetRepository: BouquetRepository
) : ViewModel() {
    val bouquetListUiState: Flow<PagingData<Bouquet>> = bouquetRepository.getAll()

    private val _bouquetPopulateListUiState = MutableStateFlow<List<Bouquet>>(emptyList())
    val bouquetPopulateListUiState: Flow<List<Bouquet>> = _bouquetPopulateListUiState.asStateFlow()

    init {
        collectPopulateBouquets()
    }

    private fun collectPopulateBouquets() {
        viewModelScope.launch(Dispatchers.IO) {
            bouquetRepository.getPopulateBouquets().collect { bouquets ->
                updatePopulateListUiState(bouquets)
            }
        }
    }

    private fun updatePopulateListUiState(bouquets: List<Bouquet>) {
        _bouquetPopulateListUiState.value = bouquets.take(5)
    }
}