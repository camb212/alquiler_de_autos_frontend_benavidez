package com.trackpets.app.presentation.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.trackpets.app.domain.model.Category
import com.trackpets.app.presentation.components.SectionHeader
import com.trackpets.app.presentation.viewmodel.CategoryUiState
import com.trackpets.app.presentation.viewmodel.CategoryViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CategoriesScreen(
    viewModel: CategoryViewModel
) {
    val uiState = viewModel.uiState
    var showDialog by remember { mutableStateOf(false) }
    var selectedCategory by remember { mutableStateOf<Category?>(null) }

    Scaffold(
        floatingActionButton = {
            if (viewModel.isAdmin) {
                FloatingActionButton(onClick = { 
                    selectedCategory = null
                    showDialog = true 
                }) {
                    Icon(Icons.Default.Add, contentDescription = "Agregar Categoría")
                }
            }
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp)
        ) {
            OutlinedTextField(
                value = viewModel.searchQuery,
                onValueChange = { viewModel.onSearchQueryChange(it) },
                modifier = Modifier.fillMaxWidth(),
                placeholder = { Text("Buscar categorías...") },
                leadingIcon = { Icon(Icons.Default.Search, contentDescription = null) },
                singleLine = true
            )

            Spacer(modifier = Modifier.height(16.dp))

            when (uiState) {
                is CategoryUiState.Loading -> {
                    Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        CircularProgressIndicator()
                    }
                }
                is CategoryUiState.Error -> {
                    Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        Text(text = uiState.message, color = MaterialTheme.colorScheme.error)
                    }
                }
                is CategoryUiState.Success -> {
                    LazyColumn(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                        item {
                            SectionHeader(
                                title = "Categorías",
                                subtitle = "Gestión de tipos de vehículos.",
                            )
                        }
                        items(uiState.categories) { category ->
                            CategoryItem(
                                category = category,
                                isAdmin = viewModel.isAdmin,
                                onEdit = {
                                    selectedCategory = category
                                    showDialog = true
                                },
                                onDelete = { category.id?.let { viewModel.deleteCategory(it) } }
                            )
                        }
                    }
                }
            }
        }
    }

    if (showDialog) {
        CategoryDialog(
            category = selectedCategory,
            onDismiss = { showDialog = false },
            onConfirm = { name, desc ->
                viewModel.saveCategory(Category(id = selectedCategory?.id, name = name, description = desc))
                showDialog = false
            }
        )
    }
}

@Composable
fun CategoryItem(
    category: Category,
    isAdmin: Boolean,
    onEdit: () -> Unit,
    onDelete: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(text = category.name, style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
                if (category.description.isNotBlank()) {
                    Text(text = category.description, style = MaterialTheme.typography.bodyMedium)
                }
            }
            if (isAdmin) {
                Row {
                    IconButton(onClick = onEdit) {
                        Icon(Icons.Default.Edit, contentDescription = "Editar", tint = Color.Gray)
                    }
                    IconButton(onClick = onDelete) {
                        Icon(Icons.Default.Delete, contentDescription = "Eliminar", tint = MaterialTheme.colorScheme.error)
                    }
                }
            }
        }
    }
}

@Composable
fun CategoryDialog(
    category: Category?,
    onDismiss: () -> Unit,
    onConfirm: (String, String) -> Unit
) {
    var name by remember { mutableStateOf(category?.name ?: "") }
    var desc by remember { mutableStateOf(category?.description ?: "") }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(if (category == null) "Nueva Categoría" else "Editar Categoría") },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                OutlinedTextField(value = name, onValueChange = { name = it }, label = { Text("Nombre") })
                OutlinedTextField(value = desc, onValueChange = { desc = it }, label = { Text("Descripción") })
            }
        },
        confirmButton = {
            Button(onClick = { onConfirm(name, desc) }, enabled = name.isNotBlank()) {
                Text("Guardar")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) { Text("Cancelar") }
        }
    )
}
