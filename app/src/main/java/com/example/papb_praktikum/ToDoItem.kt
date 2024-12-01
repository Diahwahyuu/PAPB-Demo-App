package com.example.papb_praktikum

data class ToDoItem(
    val id: String = "",
    val title: String = "",
    val description: String = "",
    val isDone: Boolean = false
)