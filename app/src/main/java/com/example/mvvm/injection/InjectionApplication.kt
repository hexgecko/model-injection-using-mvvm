package com.example.mvvm.injection

import android.app.Application
import com.example.mvvm.injection.model.todo.FakeTodoModel
import com.example.mvvm.injection.model.todo.ProdTodoModel
import com.example.mvvm.injection.model.todo.TodoModel
import org.koin.core.context.startKoin
import org.koin.dsl.module

class InjectionApplication: Application() {
    override fun onCreate() {
        super.onCreate()

        startKoin {
            modules(module {
                single<TodoModel> { ProdTodoModel(this@InjectionApplication) }
            })
        }
    }
}
