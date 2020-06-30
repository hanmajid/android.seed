package com.hanmajid.android.seed.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.hanmajid.android.seed.data.ChatRepository
import com.hanmajid.android.seed.model.Chat
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class HomeViewModel @Inject constructor(
    private val repository: ChatRepository
) : ViewModel() {
    private var currentResult: Flow<PagingData<Chat>>? = null

    init {
        refresh()
    }

    fun refresh(): Flow<PagingData<Chat>> {
        val newResult: Flow<PagingData<Chat>> = repository.getChatsStream()
            .cachedIn(viewModelScope)
        currentResult = newResult
        return newResult
    }
}