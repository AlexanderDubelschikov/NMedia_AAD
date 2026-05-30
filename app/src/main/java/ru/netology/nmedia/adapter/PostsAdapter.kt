package ru.netology.nmedia.adapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.PopupMenu
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import ru.netology.nmedia.R
import ru.netology.nmedia.databinding.CardPostBinding
import ru.netology.nmedia.dto.Post
import android.net.Uri
import android.util.Log

interface OnInteractionListener {
    fun onLike(post: Post) {}
    fun onEdit(post: Post) {}
    fun onRemove(post: Post) {}
    fun onShare(post: Post) {}
}

class PostsAdapter(
    private val onInteractionListener: OnInteractionListener,
) : ListAdapter<Post, PostViewHolder>(PostDiffCallback) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostViewHolder {
        val binding = CardPostBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return PostViewHolder(binding, onInteractionListener)
    }

    override fun onBindViewHolder(holder: PostViewHolder, position: Int) {
        val post = getItem(position)
        holder.bind(post)
    }
}

class PostViewHolder(
    private val binding: CardPostBinding,
    private val onInteractionListener: OnInteractionListener,
) : RecyclerView.ViewHolder(binding.root) {

    private fun numToString(num: Int): String {
        return when {
            num < 1000 -> "$num"
            num < 10000 -> "${(num / 100) / 10.0}K".replace(".0", "")
            num < 1000000 -> "${num / 1000}K"
            else -> "${(num / 100000) / 10.0}M".replace(".0", "")
        }
    }

    fun bind(post: Post) {
        binding.apply {
            author.text = post.author
            published.text = post.published
            content.text = post.content

            likeButton.text = numToString(post.likes)
            likeButton.icon = ContextCompat.getDrawable(
                likeButton.context,
                if (post.likedByMe) R.drawable.ic_liked_24 else R.drawable.ic_like_24
            )
            likeButton.setOnClickListener {
                onInteractionListener.onLike(post)
            }

            shareButton.text = numToString(post.share)
            shareButton.setOnClickListener {
                onInteractionListener.onShare(post)
            }

            viewsButton.text = numToString(post.views)

            menu.setOnClickListener {
                PopupMenu(it.context, it).apply {
                    inflate(R.menu.options_post)
                    setOnMenuItemClickListener { item ->
                        when (item.itemId) {
                            R.id.remove -> {
                                onInteractionListener.onRemove(post)
                                true
                            }
                            R.id.edit -> {
                                onInteractionListener.onEdit(post)
                                true
                            }
                            else -> false
                        }
                    }
                }.show()
            }

            if (post.video.isNullOrBlank()) {
                binding.videoBlock.visibility = View.GONE
            } else {
                binding.videoBlock.visibility = View.VISIBLE

                val clickListener = View.OnClickListener {
                    val uri = Uri.parse(post.video)
                    val intent = Intent(Intent.ACTION_VIEW, uri)
                    it.context.startActivity(intent)
                }
                binding.videoBlock.setOnClickListener(clickListener)
                binding.playButton.setOnClickListener(clickListener)
            }
        }
    }
}

object PostDiffCallback : DiffUtil.ItemCallback<Post>() {

    override fun areItemsTheSame(oldItem: Post, newItem: Post): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: Post, newItem: Post): Boolean {
        return oldItem == newItem
    }

}