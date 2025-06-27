package com.mobicom.s16.palacio.jeremykoen.exercise2

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView.ViewHolder

import android.text.SpannableStringBuilder
import android.text.Spanned
import android.text.style.StyleSpan
import android.graphics.Typeface
import android.text.style.LeadingMarginSpan


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

        //if location is null hide then reposition username
        if (post.location.isNullOrBlank()) {
            location.visibility = View.GONE
        } else {
            location.visibility = View.VISIBLE
            location.text = post.location
        }
        postImage.setImageResource(post.imageId)

        //caption indentation
        val prefix = "${post.username} "
        val indentPx = caption.paint.measureText(prefix).toInt()

        val raw = post.caption.orEmpty()
        val sb = SpannableStringBuilder()

        //for captions with \n in the string
        if (raw.contains("\n")) {
            val spaceWidth = caption.paint.measureText(" ").coerceAtLeast(1f)
            val spaceCount = (indentPx / spaceWidth).toInt().coerceAtLeast(1)
            val pad = " ".repeat(spaceCount)

            val lines = raw.split("\n")
            sb.append(prefix, StyleSpan(Typeface.BOLD), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
            if (lines.isNotEmpty()) sb.append(lines[0])

            // subsequent lines get a newline + pad + text
            for (i in 1 until lines.size) {
                sb.append("\n")
                    .append(pad)
                    .append(lines[i])
            }
        } else { //every other caption
            sb.apply {
                append(prefix, StyleSpan(Typeface.BOLD), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
                append(raw)
                setSpan(
                    LeadingMarginSpan.Standard(0, indentPx),
                    0,
                    length,
                    Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
                )
            }
        }
        caption.setText(sb, TextView.BufferType.SPANNABLE)


        datePosted.text = post.datePosted

        //click button
        likeButton.setOnClickListener{
            post.liked = !post.liked
            likeButton.setImageResource(
                if (post.liked) R.drawable.heartcolored else R.drawable.heart
            )
        }
    }
}