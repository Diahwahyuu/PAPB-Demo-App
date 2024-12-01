package com.example.papb_praktikum

import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

class ToDoRepository {
    private val db = FirebaseFirestore.getInstance()

    // Create
    suspend fun addToDoItem(item: ToDoItem) {
        db.collection("todos")
            .document(item.id)
            .set(item)
            .await()
    }

    // Read
    suspend fun getToDoItems(): List<ToDoItem> {
        val result = db.collection("todos").get().await()
        return result.documents.mapNotNull { doc ->
            doc.toObject(ToDoItem::class.java)
        }
    }

    // Update
    suspend fun updateToDoItem(item: ToDoItem) {
        db.collection("todos")
            .document(item.id)
            .set(item) // Firestore's `set` will update if document exists
            .await()
    }

    // Delete
    suspend fun deleteToDoItem(itemId: String) {
        db.collection("todos")
            .document(itemId)
            .delete()
            .await()
    }
}