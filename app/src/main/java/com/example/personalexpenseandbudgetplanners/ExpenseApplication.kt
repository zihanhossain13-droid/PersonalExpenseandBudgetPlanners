package com.example.personalexpenseandbudgetplanners

import android.app.Application
import com.example.personalexpenseandbudgetplanners.data.AppDatabase
import com.example.personalexpenseandbudgetplanners.data.ExpenseRepository

class ExpenseApplication : Application() {
    val database: AppDatabase by lazy { AppDatabase.getDatabase(this) }
    val repository: ExpenseRepository by lazy { 
        ExpenseRepository(database.expenseDao(), database.categoryDao()) 
    }
}
