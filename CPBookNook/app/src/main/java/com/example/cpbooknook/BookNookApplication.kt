package com.example.cpbooknook

import android.app.Application

/** ************************************************
 * Name:John Deardorff
 * Personal Library Application
 *
 * Custom Application class used for global setup and dependency injection.
 * It initializes and holds the singleton instances of the Manager (Model)
 * and the Controller for the entire application lifecycle.
 */
class BookNookApplication : Application() {

    //Initialize the Manager (Model)
    val bookManager by lazy {
        BookManager(this)
    }
    //Initialize the Controller
    val bookController by lazy {
        BookController(bookManager)
    }
}