package com.example.marketapp

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.ViewModel
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavController
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.FlowCollector
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.mapNotNull
import kotlinx.coroutines.flow.onEach

@Composable
inline fun <reified T : ViewModel> NavBackStackEntry.sharedViewModel(
    navController: NavController,
): T {
    val navGraphRoute = destination.parent?.route ?: return hiltViewModel<T>()
    val parentEntry: NavBackStackEntry = remember(this) {
        navController.getBackStackEntry(navGraphRoute)
    }
    return hiltViewModel<T>(parentEntry)
}

class SingleEventFlow<T> : Flow<T> {

    private val eventHolder: MutableStateFlow<T?> = MutableStateFlow(null)

    override suspend fun collect(collector: FlowCollector<T>) {
        asFlow().collect(collector)
    }

    fun emit(value: T) {
        eventHolder.value = value
    }

    private fun asFlow(): Flow<T> = eventHolder
        .mapNotNull { it }
        .onEach { eventHolder.value = null }
}