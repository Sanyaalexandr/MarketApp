package com.example.marketapp.domain.pagination

interface Paginator<Key, Item> {
    suspend fun loadNextItems()
    fun reset()
}