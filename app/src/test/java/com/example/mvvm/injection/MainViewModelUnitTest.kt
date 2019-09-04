package com.example.mvvm.injection

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.example.mvvm.injection.model.todo.FakeTodoModel
import com.example.mvvm.injection.model.todo.TodoModel
import com.example.mvvm.injection.viewmodel.MainViewModel
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.koin.core.context.startKoin
import org.koin.core.context.stopKoin
import org.koin.dsl.module
import org.koin.test.KoinTest
import org.koin.test.inject

class MainViewModelUnitTest: KoinTest {
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

    // Test fake todoList is correct.
    @Test
    fun testDefaultTodoList() {
        val viewModel = MainViewModel()

        viewModel.todoList.observeForever { }

        viewModel.todoList.value?.let {
            assert(it.size == 2)
            assert(it[0].id == 0)
            assert(it[0].text == "Write ProdTodoModel.")
            assert(!it[0].complete)

            assert(it[1].id == 1)
            assert(it[1].text == "Write article about injection")
            assert(it[1].complete)
        }
    }

    // Test the todoList is updated after a toggle
    @Test
    fun testToggleTodo() {
        val viewModel = MainViewModel()

        todoModel.todoList.observeForever { }

        viewModel.toggleComplete(0)
        assert(todoModel.todoList.value!![0].complete)

        viewModel.toggleComplete(1)
        assert(!todoModel.todoList.value!![1].complete)
    }
}
