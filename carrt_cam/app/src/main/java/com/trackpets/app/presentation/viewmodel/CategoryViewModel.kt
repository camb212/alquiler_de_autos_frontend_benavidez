package com.trackpets.app.presentation.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.trackpets.app.domain.model.Category
import com.trackpets.app.domain.repository.RentalRepository
import kotlinx.coroutines.launch

sealed interface CategoryUiState {
    data object Loading : CategoryUiState
    data class Success(val categories: List<Category>) : CategoryUiState
    data class Error(val message: String) : CategoryUiState
}

class CategoryViewModel(private val repository: RentalRepository) : ViewModel() {

    var uiState: CategoryUiState by mutableStateOf(CategoryUiState.Loading)
        private set

    var searchQuery by mutableStateOf("")
        private set

    var isAdmin by mutableStateOf(false)
        private set

    init {
        viewModelScope.launch {
            isAdmin = repository.isUserLoggedIn() && repository.getUserRole() == "ADMIN"
            getCategories()
        }
    }

    fun onSearchQueryChange(newQuery: String) {
        searchQuery = newQuery
        getCategories(newQuery)
    }

    fun getCategories(query: String? = null) {
        viewModelScope.launch {
            uiState = CategoryUiState.Loading
            repository.getCategories(search = query)
                .onSuccess { categories ->
                    uiState = CategoryUiState.Success(categories)
                }
                .onFailure {
                    uiState = CategoryUiState.Error("Error al cargar categorías")
                }
        }
    }

    fun deleteCategory(id: Int) {
        viewModelScope.launch {
            repository.deleteCategory(id).onSuccess {
                getCategories(searchQuery)
            }
        }
    }

    fun saveCategory(category: Category) {
        viewModelScope.launch {
            if (category.id == null) {
                repository.createCategory(category)
            } else {
                repository.updateCategory(category)
            }
            getCategories(searchQuery)
        }
    }
}
