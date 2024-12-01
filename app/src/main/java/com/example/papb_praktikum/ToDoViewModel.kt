package com.example.papb_praktikum

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.util.UUID

class ToDoViewModel : ViewModel() {
    private val repository = ToDoRepository()

    private val _toDoList = MutableStateFlow<List<ToDoItem>>(emptyList())
    val toDoList: StateFlow<List<ToDoItem>> = _toDoList

    // Load items from Firestore
    fun loadToDoItems() {
        viewModelScope.launch {
            val items = repository.getToDoItems()
            _toDoList.value = items
        }
    }

    // Create new item
    fun addToDoItem(title: String, description: String) {
        val newItem = ToDoItem(
            id = UUID.randomUUID().toString(),
            title = title,
            description = description,
            isDone = false
        )
        viewModelScope.launch {
            repository.addToDoItem(newItem)
            loadToDoItems()
        }
    }

    // Update existing item
    fun updateToDoItem(item: ToDoItem) {
        viewModelScope.launch {
            repository.updateToDoItem(item)
            loadToDoItems()
        }
    }

    // Delete item
    fun deleteToDoItem(itemId: String) {
        viewModelScope.launch {
            repository.deleteToDoItem(itemId)
            loadToDoItems()
        }
    }
}