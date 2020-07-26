package com.hanmajid.android.seed.ui.chat

import androidx.hilt.Assisted
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.hanmajid.android.seed.data.ChatRepository
import com.hanmajid.android.seed.model.Chat
import kotlinx.coroutines.flow.Flow

class ChatViewModel @ViewModelInject constructor(
    private val repository: ChatRepository,
    @Assisted private val savedStateHandle: SavedStateHandle
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

    val chatLiveData: LiveData<PagingData<Chat>> = repository.getChatsLiveData()
}