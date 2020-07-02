package com.hanmajid.android.seed.ui.chat

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.hanmajid.android.seed.databinding.ItemChatBinding
import com.hanmajid.android.seed.model.Chat

class ChatViewHolder(val binding: ItemChatBinding) : RecyclerView.ViewHolder(binding.root) {

    fun bind(chat: Chat?) {
        chat?.apply {
            binding.chat = this
        }
    }

    companion object {
        fun create(parent: ViewGroup): ChatViewHolder {
            val binding = ItemChatBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
            return ChatViewHolder(binding)
        }
    }
}