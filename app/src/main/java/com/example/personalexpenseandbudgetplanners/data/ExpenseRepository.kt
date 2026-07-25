package com.example.personalexpenseandbudgetplanners.data

import kotlinx.coroutines.flow.Flow

class ExpenseRepository(
    private val expenseDao: ExpenseDao,
    private val categoryDao: CategoryDao
) {
    val allExpenses: Flow<List<Expense>> = expenseDao.getAllExpenses()
    val allCategories: Flow<List<Category>> = categoryDao.getAllCategories()
    val totalIncome: Flow<Double?> = expenseDao.getTotalIncome()
    val totalExpense: Flow<Double?> = expenseDao.getTotalExpense()
    val remainingBalance: Flow<Double?> = expenseDao.getRemainingBalance()

    suspend fun insertExpense(expense: Expense) = expenseDao.insertExpense(expense)
    suspend fun deleteExpense(expense: Expense) = expenseDao.deleteExpense(expense)

    suspend fun insertCategory(category: Category) = categoryDao.insertCategory(category)
    suspend fun deleteCategory(category: Category) = categoryDao.deleteCategory(category)

    fun getSpentInRange(startDate: Long, endDate: Long): Flow<Double?> = 
        expenseDao.getSpentInRange(startDate, endDate)
}
