package com.example.booknookv2

/**
 * A simple global object to hold the currently logged-in user's info.
 * This is the "Brain" of your personalization layer.
 */
object UserSession {
    var currentUser: User? = null

    fun isLoggedIn(): Boolean = currentUser != null

    fun logout() {
        currentUser = null
    }

    val currentUserId: Int
        get() = currentUser?.id ?: -1
}