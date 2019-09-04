package com.example.mvvm.injection

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.example.mvvm.injection.model.todo.FakeTodoModel
import com.example.mvvm.injection.model.todo.TodoModel
import com.example.mvvm.injection.viewmodel.TodoDetailViewModel
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.koin.core.context.startKoin
import org.koin.core.context.stopKoin
import org.koin.dsl.module
import org.koin.test.KoinTest
import org.koin.test.inject

class TodoDetailViewModelUnitTest: KoinTest {
    // Rule to just execute the live data and ignore any life cycles.
    @Rule
    @JvmField
    val rule = InstantTaskExecutorRule()

    private val todoModel by inject<TodoModel>()

    // Called before a test is running.
    @Before
    fun before() {
        startKoin {
            modules(module {
                single<TodoModel> { FakeTodoModel() }
            })
        }
    }

    // Called after a test is running
    @After
    fun after() {
        stopKoin()
    }

    // Test newTodo
    @Test
    fun testNewTodo() {
        val viewModel = TodoDetailViewModel()

        todoModel.todoList.observeForever { }

        viewModel.text.value = "New Todo!"
        viewModel.newTodoItem()

        todoModel.todoList.value?.let {
            assert(it.size == 3)
            assert(it[2].id == 2)
            assert(it[2].text == "New Todo!")
            assert(!it[2].complete)
        }
    }

    // Test updateTodo
    @Test
    fun testUpdateTodo() {
        val viewModel = TodoDetailViewModel()

        todoModel.todoList.observeForever { }

        viewModel.text.value = "Update Todo!"
        viewModel.updateTodoItem(1)

        todoModel.todoList.value?.let {
            assert(it[1].id == 1)
            assert(it[1].text == "Update Todo!")
            assert(it[1].complete)
        }
    }

    // Test set setTextWithTodoText
    @Test
    fun testSetTextWithTodoText() {
        val viewModel = TodoDetailViewModel()

        viewModel.text.observeForever { }

        viewModel.setTextWithTodoText(0)
        assert(viewModel.text.value!! == "Write ProdTodoModel.")
    }

    // Test deleteTodo
    @Test
    fun testDeleteTodo() {
        val viewModel = TodoDetailViewModel()

        todoModel.todoList.observeForever { }

        viewModel.deleteTodo(0)

        todoModel.todoList.value?.let {
            assert(it.size == 1)
        }
    }
}
