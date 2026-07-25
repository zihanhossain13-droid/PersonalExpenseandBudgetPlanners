package com.example.personalexpenseandbudgetplanners.data

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface ExpenseDao {
    @Query("SELECT * FROM expenses ORDER BY date DESC")
    fun getAllExpenses(): Flow<List<Expense>>

    @Query("SELECT * FROM expenses WHERE categoryId = :categoryId ORDER BY date DESC")
    fun getExpensesByCategory(categoryId: Long): Flow<List<Expense>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertExpense(expense: Expense)

    @Delete
    suspend fun deleteExpense(expense: Expense)

    @Query("SELECT SUM(amount) FROM expenses WHERE isIncome = 1")
    fun getTotalIncome(): Flow<Double?>

    @Query("SELECT SUM(amount) FROM expenses WHERE isIncome = 0")
    fun getTotalExpense(): Flow<Double?>

    @Query("SELECT (SELECT IFNULL(SUM(amount), 0) FROM expenses WHERE isIncome = 1) - (SELECT IFNULL(SUM(amount), 0) FROM expenses WHERE isIncome = 0)")
    fun getRemainingBalance(): Flow<Double?>

    @Query("SELECT SUM(amount) FROM expenses WHERE date >= :startDate AND date <= :endDate AND isIncome = 0")
    fun getSpentInRange(startDate: Long, endDate: Long): Flow<Double?>
}
