package com.hanmajid.android.seed.ui.home

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.NavController
import androidx.recyclerview.widget.RecyclerView
import com.hanmajid.android.seed.databinding.ItemPostBinding
import com.hanmajid.android.seed.model.Post

class PostViewHolder(val binding: ItemPostBinding) : RecyclerView.ViewHolder(binding.root) {

    fun bind(post: Post?, navController: NavController) {
        post?.apply {
            binding.post = this
            binding.navController = navController
        }
    }

    companion object {
        fun create(parent: ViewGroup): PostViewHolder {
            val binding = ItemPostBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
            return PostViewHolder(binding)
        }
    }
}