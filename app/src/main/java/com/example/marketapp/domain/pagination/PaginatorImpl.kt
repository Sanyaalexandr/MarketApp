package com.example.marketapp.domain.pagination

class PaginatorImpl<Key, Item>(
    private val initialKey: Key,
    private inline val onLoadUpdated: (Boolean) -> Unit,
    private inline val onRequest: suspend (nextKey: Key) -> Result<List<Item>>,
    private inline val getNextKey: suspend () -> Key,
    private inline val onError: suspend (Throwable?) -> Unit,
    private inline val onSuccess: suspend (items: List<Item>, newKey: Key) -> Unit
): Paginator<Key, Item> {

    companion object {
        const val DEFAULT_PAGE_SIZE = 20
    }

    private var currentKey = initialKey
    private var isMakingRequest = false

    override suspend fun loadNextItems() {
        if(isMakingRequest) {
            return
        }
        isMakingRequest = true
        onLoadUpdated(true)
        val result = onRequest(currentKey)
        val items = result.getOrElse {
            onError(it)
            onLoadUpdated(false)
            isMakingRequest = false
            return
        }
        currentKey = getNextKey()
        onSuccess(items, currentKey)
        onLoadUpdated(false)
        isMakingRequest = false
    }

    override fun reset() {
        currentKey = initialKey
    }
}