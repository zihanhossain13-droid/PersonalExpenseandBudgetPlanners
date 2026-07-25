package com.example.personalexpenseandbudgetplanners.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.example.personalexpenseandbudgetplanners.ui.viewmodel.ExpenseViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddExpenseScreen(viewModel: ExpenseViewModel, onBack: () -> Unit) {
    var title by remember { mutableStateOf("") }
    var amount by remember { mutableStateOf("") }
    var isIncome by remember { mutableStateOf(false) }
    val categories by viewModel.allCategories.collectAsState()
    var selectedCategoryId by remember { mutableLongStateOf(0L) }
    var expanded by remember { mutableStateOf(false) }

    // Default category if none exists
    LaunchedEffect(categories) {
        if (categories.isEmpty()) {
            viewModel.addCategory("General", 0.0)
        } else if (selectedCategoryId == 0L) {
            selectedCategoryId = categories.firstOrNull()?.id ?: 0L
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(if (isIncome) "Add Cash" else "Add Expense") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Income/Expense Selector
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                FilterChip(
                    selected = !isIncome,
                    onClick = { isIncome = false },
                    label = { Text("Expense") },
                    modifier = Modifier.weight(1f)
                )
                FilterChip(
                    selected = isIncome,
                    onClick = { isIncome = true },
                    label = { Text("Income (Cash)") },
                    modifier = Modifier.weight(1f)
                )
            }

            OutlinedTextField(
                value = title,
                onValueChange = { title = it },
                label = { Text(if (isIncome) "Source" else "Title") },
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = amount,
                onValueChange = { amount = it },
                label = { Text("Amount (TK)") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                modifier = Modifier.fillMaxWidth()
            )

            if (!isIncome) {
                ExposedDropdownMenuBox(
                    expanded = expanded,
                    onExpandedChange = { expanded = !expanded }
                ) {
                    OutlinedTextField(
                        value = categories.find { it.id == selectedCategoryId }?.name ?: "Select Category",
                        onValueChange = {},
                        readOnly = true,
                        label = { Text("Category") },
                        trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
                        modifier = Modifier.menuAnchor().fillMaxWidth()
                    )
                    ExposedDropdownMenu(
                        expanded = expanded,
                        onDismissRequest = { expanded = false }
                    ) {
                        categories.forEach { category ->
                            DropdownMenuItem(
                                text = { Text(category.name) },
                                onClick = {
                                    selectedCategoryId = category.id
                                    expanded = false
                                }
                            )
                        }
                    }
                }
            }

            Button(
                onClick = {
                    val amountDouble = amount.toDoubleOrNull() ?: 0.0
                    if (title.isNotBlank() && amountDouble > 0) {
                        // For income, we can use a dummy category or specific one
                        val categoryId = if (isIncome) (categories.firstOrNull()?.id ?: 0L) else selectedCategoryId
                        viewModel.addExpense(title, amountDouble, categoryId, isIncome)
                        onBack()
                    }
                },
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(
                    containerColor = if (isIncome) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.error
                )
            ) {
                Text(if (isIncome) "Save Cash" else "Save Expense")
            }
        }
    }
}
