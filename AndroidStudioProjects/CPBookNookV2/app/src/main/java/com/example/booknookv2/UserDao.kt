package com.example.booknookv2

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface UserDao {
    // Login Logic:
    // Matches username AND password
    @Query("SELECT * FROM users WHERE username = :name AND passwordHash = :password LIMIT 1")
    suspend fun login(name: String, password: String): User?
    // Forgotten User:
    // flow for reseting user pw
    @Query("SELECT * FROM users WHERE email = :email LIMIT 1")
    suspend fun findByEmail(email: String): User?

    // PW Update:
    // updates after forgoten pw flow
    @Query("UPDATE users SET passwordHash = :newHash WHERE id = :userId")
    suspend fun updatePassword(userId: Int, newHash: String)

    // Registration Check:
    // Ensures the name isn't taken
    @Query("SELECT * FROM users WHERE username = :name LIMIT 1")
    suspend fun findByUsername(name: String): User?

    @Insert
    suspend fun register(user: User): Long
}