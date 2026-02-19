package com.example.booknookv2

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Insert
import androidx.room.Update
import androidx.room.Delete

@Dao
interface LoanDao {

    @Transaction
    @Query("SELECT * FROM Loans")
    fun getAllLoansWithBookInfo(): LiveData<List<LoanWithBook>>

    @Insert
    suspend fun insertLoan(loan: Loan)

    @Update
    suspend fun updateLoan(loan: Loan)

    @Delete
    suspend fun deleteLoan(loan: Loan)
}