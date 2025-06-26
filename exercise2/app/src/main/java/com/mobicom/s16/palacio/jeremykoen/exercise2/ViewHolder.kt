package com.mobicom.s16.palacio.jeremykoen.exercise2

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView.ViewHolder

class ViewHolder(itemView:View): ViewHolder(itemView) {
    private val userImage: ImageView = itemView.findViewById(R.id.userImage)
    private val username: TextView = itemView.findViewById(R.id.username)
    private val location: TextView = itemView.findViewById(R.id.location)
    private val postImage: ImageView = itemView.findViewById(R.id.postImage)
    private val likeButton: ImageView = itemView.findViewById(R.id.likeButton)
    private val caption: TextView = itemView.findViewById(R.id.caption)
    private val datePosted: TextView = itemView.findViewById(R.id.datePosted)

    fun bind(post:Post){
        userImage.setImageResource(post.userImageId)
        username.text = post.username
        location.text = post.location ?: ""
        postImage.setImageResource(post.imageId)
        caption.text = post.caption ?: ""
        datePosted.text = post.datePosted

        if(post.liked){
            likeButton.setImageResource(R.drawable.heartcolored)
        }else{
            likeButton.setImageResource(R.drawable.heart)
        }

        //click button
        likeButton.setOnClickListener{
            post.liked = !post.liked
            likeButton.setImageResource(
                if (post.liked) R.drawable.heartcolored else R.drawable.heart
            )
        }
    }
}