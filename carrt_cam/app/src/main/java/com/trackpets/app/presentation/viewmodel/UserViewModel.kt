package com.trackpets.app.presentation.viewmodel

import androidx.compose.runtime.*
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.trackpets.app.domain.model.User
import com.trackpets.app.domain.repository.RentalRepository
import kotlinx.coroutines.launch

class UserViewModel(private val repository: RentalRepository) : ViewModel() {
    var users by mutableStateOf<List<User>>(emptyList())
        private set

    var currentUser by mutableStateOf<User?>(null)
        private set

    var isLoading by mutableStateOf(false)
        private set

    var userRole by mutableStateOf<String?>(null)
        private set

    init {
        loadInitialData()
    }

    fun loadInitialData() {
        viewModelScope.launch {
            isLoading = true
            userRole = repository.getUserRole()
            if (userRole == "ADMIN") {
                loadAllUsers()
            } else {
                loadProfile()
            }
            isLoading = false
        }
    }

    fun loadAllUsers() {
        viewModelScope.launch {
            repository.getUsers().onSuccess {
                users = it
            }
        }
    }

    private fun loadProfile() {
        viewModelScope.launch {
            val userId = repository.getCurrentUserId()
            if (userId != -1) {
                repository.getUser(userId).onSuccess {
                    currentUser = it
                }
            }
        }
    }

    fun saveUser(user: User) {
        viewModelScope.launch {
            val result = if (user.id == null) {
                repository.createUser(user)
            } else {
                repository.updateUser(user)
            }
            result.onSuccess { loadAllUsers() }
        }
    }

    fun deleteUser(id: Int) {
        viewModelScope.launch {
            repository.deleteUser(id).onSuccess {
                loadAllUsers()
            }
        }
    }
    
    fun logout(onLogout: () -> Unit) {
        viewModelScope.launch {
            repository.logout()
            onLogout()
        }
    }
}
