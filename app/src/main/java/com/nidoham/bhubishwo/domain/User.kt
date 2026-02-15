package com.nidoham.bhubishwo.domain

data class User(
    val uid: String,
    val name: String,
    val username: String,
    val email: String,
    val avatar: String? = null,
    val point: Long = 0L,
    val experience: Long = 0L,
    val banned: Boolean = false,
    val verified: Boolean = false,
    val joined: Long = System.currentTimeMillis()
) {
    init {
        require(uid.isNotBlank()) { "UID cannot be blank" }
        require(name.isNotBlank()) { "Name cannot be blank" }
        require(username.isNotBlank()) { "Username cannot be blank" }
        require(email.isNotBlank() && email.contains("@")) { "Invalid email" }
        require(point >= 0) { "Point cannot be negative" }
        require(experience >= 0) { "Experience cannot be negative" }
    }

    val isActive: Boolean
        get() = !banned && verified

    val level: Int
        get() = (experience / 1000).toInt() + 1
}