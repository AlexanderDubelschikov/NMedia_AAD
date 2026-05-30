package ru.netology.nmedia.viewmodel

import androidx.lifecycle.*
import ru.netology.nmedia.dto.Post
import ru.netology.nmedia.repository.PostRepository
import ru.netology.nmedia.repository.PostRepositoryInMemoryImpl
private var originalPost: Post? = null
private val empty = Post(
    id = 0,
    author = "",
    content = "",
    published = "",
    likes = 0,
    likedByMe = false,
    views = 0,
    share = 0
)

class PostViewModel : ViewModel() {
    private val repository: PostRepository = PostRepositoryInMemoryImpl()
    val data = repository.getAll()
    val edited = MutableLiveData(empty)

    fun save(content: String) {
        edited.value?.let {
            val text = content.trim()
            if (it.content != text) {
                repository.save(it.copy(content = text))
            }
        }
        edited.value = empty
    }

    fun edit(post: Post) {
        originalPost = post
        edited.value = post
    }

    fun cancelEdit() {
        originalPost?.let { original ->
            edited.value = original
        }
        edited.value = empty
        originalPost = null
    }

    fun likeById(id: Long) = repository.likeById(id)
    fun removeById(id: Long) = repository.removeById(id)
    fun updatePost(id: Long, content: String) = repository.updatePost(id, content)
    //fun shareById(id: Long) = repository.shareById(id)
}


