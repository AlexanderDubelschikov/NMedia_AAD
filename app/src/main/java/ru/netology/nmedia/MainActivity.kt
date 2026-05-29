package ru.netology.nmedia

import android.os.Bundle
import android.util.Log
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import ru.netology.nmedia.databinding.ActivityMainBinding
import ru.netology.nmedia.dto.Post


class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        val binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(binding.main) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // Домашка

        val post = Post(
            id = 1,
            author = "Нетология. Университет интернет-профессий будущего",
            content = "Привет, это новая Нетология! Когда-то Нетология начиналась с интенсивов по онлайн-маркетингу. Затем появились курсы по дизайну, разработке, аналитике и управлению. Мы растём сами и помогаем расти студентам: от новичков до уверенных профессионалов. Но самое важное остаётся с нами: мы верим, что в каждом уже есть сила, которая заставляет хотеть больше, целиться выше, бежать быстрее. Наша миссия — помочь встать на путь роста и начать цепочку перемен → http://netolo.gy/fyb",
            published = "21 мая в 18:36",
            likes = 999,
            likedByMe = false,
            views = 10000,
            share = 1095
        )
        with(binding) {
            author.text = post.author
            published.text = post.published
            content.text = post.content

            fun numToString(num: Int): String{
                return when {
                    num < 1000 -> "$num"
                    num < 10000 -> "${(num / 100) / 10.0}K".replace(".0", "")
                    num < 1000000 -> "${num / 1000}K"
                    else -> "${(num / 100000) / 10.0}M".replace(".0", "")
                }
            }

            likeCount?.text = numToString(post.likes)
            viewsCount?.text = numToString(post.views)
            shareCount?.text = numToString(post.share)


            if (post.likedByMe) {
                likeIcon?.setImageResource(R.drawable.ic_liked_24)
            }

            root.setOnClickListener {
                Log.d("stuff", "stuff")
            }

            avatar.setOnClickListener {
                Log.d("stuff", "avatar")
            }

            likeIcon?.setOnClickListener {
                Log.d("stuff", "like")
                post.likedByMe = !post.likedByMe
                likeIcon.setImageResource(
                    if (post.likedByMe) R.drawable.ic_liked_24 else R.drawable.ic_like_24
                )
                if (post.likedByMe) post.likes++ else post.likes--
                likeCount?.text = numToString(post.likes)
            }

            shareIcon?.setOnClickListener {
                Log.d("stuff", "share")
                post.share++
                shareCount?.text=numToString(post.share)
            }
        }
    }
}