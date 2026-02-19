package com.example.booknookv2

import kotlinx.coroutines.flow.Flow

class UserRepository(private val userDao: UserDao) {

    suspend fun insertUser(user: User): Long {
        return userDao.register(user)
    }

    suspend fun getUserByEmail(email: String): User? {
        return userDao.findByEmail(email)
    }

    suspend fun login(username: String, passwordHash: String): User? {
        return userDao.login(username, passwordHash)
    }
}