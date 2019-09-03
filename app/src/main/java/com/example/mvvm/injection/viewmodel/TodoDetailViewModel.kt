package com.example.mvvm.injection.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.mvvm.injection.model.todo.TodoModel
import org.koin.core.KoinComponent
import org.koin.core.inject

class TodoDetailViewModel: ViewModel(), KoinComponent {

    private val todoModel by inject<TodoModel>()

    val text = MutableLiveData<String>().apply { value = "" }

    fun setCurrentTodoText(id: Int) {
        text.value = todoModel.getTodo(id)?.text ?: ""
    }

    fun newTodoItem() {
        if((text.value ?: "") != "") {
            todoModel.newTodo(text.value ?: "")
        }
    }

    fun updateTodoItem(id: Int) {
        if((text.value ?: "") != "") {
            todoModel.updateTodo(id, text.value ?: "")
        }
    }

    fun deleteTodo(id: Int) = todoModel.deleteTodo(id)
}
