package com.example.papb_praktikum.pages

import android.widget.Space
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.papb_praktikum.AuthViewModel
import androidx.compose.runtime.livedata.observeAsState
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.papb_praktikum.AuthState
import com.example.papb_praktikum.ToDoItem
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.graphics.Color
import com.example.papb_praktikum.ToDoViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Homepage(
    modifier: Modifier = Modifier,
    navController: NavController,
    authViewModel: AuthViewModel,
    toDoViewModel: ToDoViewModel = viewModel()
) {
    // Observe authentication state
    val authState = authViewModel.authState.observeAsState()

    // Check for unauthenticated state and navigate to login if true
    LaunchedEffect(authState.value) {
        when (authState.value) {
            is AuthState.Unauthenticated -> navController.navigate("login")
            else -> Unit
        }
    }

    // Variables for to-do input
    var title by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var showDialog by remember { mutableStateOf(false) }
    var selectedToDoItem by remember {
        mutableStateOf<ToDoItem?>(null)
    }

    // To-do list state from ViewModel
    val toDoList by toDoViewModel.toDoList.collectAsState()

    // Coroutine scope for operations
    val scope = rememberCoroutineScope()

    // Load to-do items on page load
    LaunchedEffect(Unit) {
        toDoViewModel.loadToDoItems()
    }

    // Layout
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = "Home Page", color = Color.White) },
                colors = TopAppBarDefaults.smallTopAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary // Warna latar belakang
                ),
                actions = {
                    // Tombol Sign Out
                    Button(
                        onClick = { authViewModel.signout() },
                        colors = ButtonDefaults.buttonColors(MaterialTheme.colorScheme.error),
                        modifier = Modifier.padding(horizontal = 8.dp)
                    ) {
                        Text(text = "Sign Out", color = MaterialTheme.colorScheme.onPrimary)
                    }
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    title = ""
                    description = ""
                    selectedToDoItem = null
                    showDialog = true
                          },
                content = {
                    Icon(Icons.Default.Add, contentDescription = "Add To-Do")
                }
            )
        },
        content = { paddingValues ->
            Column(
                modifier = modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .padding(16.dp)
            ) {
                // Display to-do list
                LazyColumn(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f)
                ) {
                    items(toDoList) { item ->
                        ToDoItemCard(
                            item,
                            onEdit = { toDoItem ->
                                selectedToDoItem = toDoItem
                                title = toDoItem.title
                                description = toDoItem.description
                                showDialog = true
                            },
                            onDelete = { toDoViewModel.deleteToDoItem(it.id) }
                        )
                    }
                }
            }

            // Show dialog when FAB is pressed
            if (showDialog) {
                AddToDoDialog(
                    title = title,
                    description = description,
                    onTitleChange = { title = it },
                    onDescriptionChange = { description = it },
                    onDismiss = { showDialog = false },
                    onAdd = {
                        if (selectedToDoItem == null) {
                            // Tambah to-do baru
                            toDoViewModel.addToDoItem(title, description)
                        } else {
                            // Edit to-do yang sudah ada
                            selectedToDoItem?.let { item ->
                                toDoViewModel.updateToDoItem(item.copy(title = title, description = description))
                            }
                        }
                        title = ""
                        description = ""
                        selectedToDoItem = null
                        showDialog = false
                    }
                )
            }

        }
    )
}

@Composable
fun AddToDoDialog(
    title: String,
    description: String,
    onTitleChange: (String) -> Unit,
    onDescriptionChange: (String) -> Unit,
    onDismiss: () -> Unit,
    onAdd: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(text = if (title.isEmpty() && description.isEmpty()) "Add New To-Do" else "Edit To-Do") },
        text = {
            Column {
                TextField(
                    value = title,
                    onValueChange = onTitleChange,
                    label = { Text("Title") },
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(8.dp))
                TextField(
                    value = description,
                    onValueChange = onDescriptionChange,
                    label = { Text("Description") },
                    modifier = Modifier.fillMaxWidth()
                )
            }
        },
        confirmButton = {
            Button(onClick = onAdd) {
                Text("Save")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel")
            }
        }
    )
}

@Composable
fun ToDoItemCard(
    item: ToDoItem,
    onEdit: (ToDoItem) -> Unit,
    onDelete: (ToDoItem) -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(text = item.title, style = MaterialTheme.typography.titleMedium)
            Text(text = item.description, style = MaterialTheme.typography.bodyMedium)
            Text(
                text = if (item.isDone) "Done" else "Pending",
                style = MaterialTheme.typography.bodySmall
            )

            Row (
                modifier = Modifier.padding(top = 8.dp)
            ) {
                Button(
                    onClick = { onEdit(item) },
                    modifier = Modifier.padding(end = 8.dp)
                ) {
                    Icon(Icons.Filled.Edit, contentDescription = "Edit To Do Item")
                }

                Button(
                    onClick = { onDelete(item) },
                    colors = ButtonDefaults.buttonColors(MaterialTheme.colorScheme.error)
                ) {
                    Icon(Icons.Filled.Delete, contentDescription = "Delete To Do Item")
                }
            }
        }
    }
}