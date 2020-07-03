package com.hanmajid.android.seed.ui.home

import androidx.hilt.Assisted
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.hanmajid.android.seed.model.Post

class PostViewModel @ViewModelInject constructor(
    @Assisted private val savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val allPosts = listOf(
        Post(
            "1",
            "Hello world",
            "1 July 2020",
            "2",
            "https://pbs.twimg.com/profile_images/1278222393220034560/a8OqzwjZ_400x400.jpg",
            "hanmajid"
        ),
        Post(
            "2",
            "It's wednesday my dude",
            "1 July 2020",
            "3",
            "https://pbs.twimg.com/profile_images/738161643570356225/e7poYrfJ_400x400.jpg",
            "mydude"
        )
    )

    var posts = MutableLiveData<List<Post>>()

    init {
        posts.postValue(allPosts)
    }

    fun getPostById(id: String): Post = allPosts.firstOrNull { it.id == id } ?: allPosts[0]
}