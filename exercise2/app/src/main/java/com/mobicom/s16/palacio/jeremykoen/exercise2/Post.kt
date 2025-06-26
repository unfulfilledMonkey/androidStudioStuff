package com.mobicom.s16.palacio.jeremykoen.exercise2

class Post(
    val imageId: Int,
    val datePosted: String,
    val caption: String?,
    val location: String?,
    var liked: Boolean,
    val username: String,
    val userImageId: Int) {
}