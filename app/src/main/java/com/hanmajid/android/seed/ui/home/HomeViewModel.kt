package com.hanmajid.android.seed.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.hanmajid.android.seed.api.AppService
import com.hanmajid.android.seed.data.ChatRepository
import com.hanmajid.android.seed.model.Chat
import kotlinx.coroutines.flow.Flow

class HomeViewModel(
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

object Injection {

    private fun provideChatRepository(): ChatRepository {
        return ChatRepository(AppService.create())
    }

    fun provideViewModelFactory(): ViewModelProvider.Factory {
        return ViewModelFactory(provideChatRepository())
    }
}

class ViewModelFactory(private val repository: ChatRepository) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(HomeViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return HomeViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}