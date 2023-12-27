package com.example.flowershopapp.ComposeUI.Network

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.flowershopapp.API.APIStatus
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException

open class NetworkViewModel : ViewModel() {
    var apiStatus by mutableStateOf(APIStatus.DONE)
        private set
    var apiError by mutableStateOf("")
        private set

    fun runInScope(
        actionSuccess: suspend () -> Unit,
        actionError: suspend () -> Unit
    ) {
        viewModelScope.launch {
            apiStatus = APIStatus.LOADING
            runCatching {
                actionSuccess()
                apiStatus = APIStatus.DONE
                apiError = ""
            }.onFailure { e: Throwable ->
                when (e) {
                    is IOException,
                    is HttpException -> {
                        actionError()
                        apiStatus = APIStatus.ERROR
                        apiError = e.localizedMessage ?: e.toString()
                    }

                    else -> throw e
                }
            }
        }
    }

    fun runInScope(actionSuccess: suspend () -> Unit) {
        runInScope(actionSuccess, actionError = {})
    }
}
