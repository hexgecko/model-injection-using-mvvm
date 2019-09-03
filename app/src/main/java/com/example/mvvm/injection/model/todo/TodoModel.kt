package com.example.mvvm.injection.model.todo

import androidx.lifecycle.LiveData

interface TodoModel {
    // List the current, with the oldest first
    val todoList: LiveData<List<TodoItem>>

    // Add a new
    fun newTodo(text: String)

    // Update the text
    fun updateTodo(id: Int, text: String)

    // toggle completed status
    fun toggleCompleted(id: Int)

    // Delete
    fun deleteTodo(id: Int)

    // Get the item from an id
    fun getTodo(id: Int): TodoItem?
}
