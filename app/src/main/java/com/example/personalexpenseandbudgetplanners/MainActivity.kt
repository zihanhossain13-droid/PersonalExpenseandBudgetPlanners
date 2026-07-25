package com.example.personalexpenseandbudgetplanners

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.personalexpenseandbudgetplanners.ui.screens.AddExpenseScreen
import com.example.personalexpenseandbudgetplanners.ui.screens.DashboardScreen
import com.example.personalexpenseandbudgetplanners.ui.theme.PersonalExpenseAndBudgetPlannersTheme
import com.example.personalexpenseandbudgetplanners.ui.viewmodel.ExpenseViewModel
import com.example.personalexpenseandbudgetplanners.ui.viewmodel.ExpenseViewModelFactory

class MainActivity : ComponentActivity() {
    private val viewModel: ExpenseViewModel by viewModels {
        ExpenseViewModelFactory((application as ExpenseApplication).repository)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            PersonalExpenseAndBudgetPlannersTheme {
                val navController = rememberNavController()
                
                NavHost(navController = navController, startDestination = "dashboard") {
                    composable("dashboard") {
                        DashboardScreen(
                            viewModel = viewModel,
                            onAddExpenseClick = { navController.navigate("add_expense") }
                        )
                    }
                    composable("add_expense") {
                        AddExpenseScreen(
                            viewModel = viewModel,
                            onBack = { navController.popBackStack() }
                        )
                    }
                }
            }
        }
    }
}
