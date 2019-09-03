package com.example.mvvm.injection.model.todo

import androidx.lifecycle.MutableLiveData

class FakeTodoModel: TodoModel {

    private var nextId = 2

    override val todoList = MutableLiveData<List<TodoItem>>().apply { value = listOf(
        TodoItem(0, "Write ProdTodoModel.", false),
        TodoItem(1, "Write article about injection", true)
    )}

    override fun newTodo(text: String) {
        todoList.value = todoList.value?.plus( listOf(TodoItem(nextId++, text, false)) )
    }

    override fun updateTodo(id: Int, text: String) {
        todoList.value = todoList.value?.map {
            if(it.id == id) {
                TodoItem(id, text, it.complete)
            } else {
                it
            }
        }
    }

    override fun toggleCompleted(id: Int) {
        todoList.value = todoList.value?.map {
            if(it.id == id) {
                TodoItem(id, it.text, !it.complete)
            } else {
                it
            }
        }
    }

    override fun deleteTodo(id: Int) {
        todoList.value = todoList.value?.filter { it.id != id }
    }

    override fun getTodo(id: Int): TodoItem? {
        return todoList.value?.find { it.id == id }
    }
}
