package com.example.mvvm.injection.viewmodel

import androidx.lifecycle.ViewModel
import com.example.mvvm.injection.model.todo.TodoModel
import org.koin.core.KoinComponent
import org.koin.core.inject

class MainViewModel: ViewModel(), KoinComponent {

    private val todoModel by inject<TodoModel>()

    val todoList = todoModel.todoList

    fun toggleComplete(id: Int) = todoModel.toggleCompleted(id)
}
