package com.example.personalexpenseandbudgetplanners.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.personalexpenseandbudgetplanners.ui.viewmodel.ExpenseViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DashboardScreen(viewModel: ExpenseViewModel, onAddExpenseClick: () -> Unit) {
    val remainingBalance by viewModel.remainingBalance.collectAsState()
    val totalIncome by viewModel.totalIncome.collectAsState()
    val totalExpense by viewModel.totalExpense.collectAsState()
    val expenses by viewModel.allExpenses.collectAsState()
    val monthlySpent by viewModel.currentMonthSpent.collectAsState()

    Scaffold(
        topBar = { TopAppBar(title = { Text("Finance Manager") }) },
        floatingActionButton = {
            FloatingActionButton(onClick = onAddExpenseClick) {
                Icon(Icons.Default.Add, contentDescription = "Add Entry")
            }
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
                .padding(16.dp)
        ) {
            Card(
                modifier = Modifier.fillMaxWidth(),
                elevation = CardDefaults.cardElevation(4.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(text = "Remaining Balance", style = MaterialTheme.typography.titleMedium)
                    Text(
                        text = "${String.format("%.2f", remainingBalance)} TK",
                        style = MaterialTheme.typography.headlineLarge
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                        Column {
                            Text(text = "Total Cash In", style = MaterialTheme.typography.labelMedium)
                            Text(text = "${String.format("%.2f", totalIncome)} TK", color = MaterialTheme.colorScheme.primary)
                        }
                        Column {
                            Text(text = "Total Spent", style = MaterialTheme.typography.labelMedium)
                            Text(text = "${String.format("%.2f", totalExpense)} TK", color = MaterialTheme.colorScheme.error)
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))
            Text(text = "This Month's Expense: ${String.format("%.2f", monthlySpent)} TK", style = MaterialTheme.typography.bodyMedium)
            Spacer(modifier = Modifier.height(24.dp))

            Text(text = "Recent History", style = MaterialTheme.typography.titleLarge)
            
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(vertical = 8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(expenses) { expense ->
                    ExpenseItem(expense)
                }
            }
        }
    }
}

@Composable
fun ExpenseItem(expense: com.example.personalexpenseandbudgetplanners.data.Expense) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
    ) {
        Row(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column {
                Text(text = expense.title, style = MaterialTheme.typography.bodyLarge)
                Text(
                    text = java.text.DateFormat.getDateInstance().format(expense.date),
                    style = MaterialTheme.typography.bodySmall
                )
            }
            Text(
                text = (if (expense.isIncome) "+" else "-") + "${String.format("%.2f", expense.amount)} TK",
                style = MaterialTheme.typography.titleMedium,
                color = if (expense.isIncome) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.error
            )
        }
    }
}
