package com.example.flowershopapp.ComposeUI.Bouquet

import com.example.flowershopapp.ComposeUI.Network.NetworkViewModel
import com.example.flowershopapp.Entities.Model.Bouquet
import com.example.flowershopapp.Entities.Repository.Bouquet.BouquetRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class PopulateBouquetViewModel(
    private val bouquetRepository: BouquetRepository
) : NetworkViewModel() {

    private val _bouquetPopulateListUiState = MutableStateFlow<List<Bouquet>>(emptyList())
    var bouquetPopulateListUiState: Flow<List<Bouquet>> = MutableStateFlow(emptyList())
    init {
        collectPopulateBouquets()
    }
    private fun collectPopulateBouquets() {
        runInScope(
            actionSuccess = {
               bouquetRepository.getPopulateBouquets()
            },
            actionError = {
                _bouquetPopulateListUiState.value = emptyList()
            }
        )
    }
}

