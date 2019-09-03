package com.example.mvvm.injection

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.TextView
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.example.mvvm.injection.model.todo.TodoItem
import com.example.mvvm.injection.viewmodel.MainViewModel
import com.google.android.material.floatingactionbutton.FloatingActionButton

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val viewModel = ViewModelProviders.of(this).get(MainViewModel::class.java)

        bindTodoList(findViewById(R.id.todo_list), viewModel.todoList, viewModel::toggleComplete)
        bindNewTodo(findViewById(R.id.new_todo)) {
            startActivity(Intent(this, TodoDetailActivity::class.java))
        }
    }

    private fun bindTodoList(layout: ViewGroup, todoList: LiveData<List<TodoItem>>, onClick: (Int) -> Unit) {
        val inflater = LayoutInflater.from(this)

        todoList.observe(this, Observer { list ->
            layout.removeAllViews()
            list.forEach { item ->
                val panel = inflater.inflate(R.layout.panel_todo_item, layout, false)

                panel.findViewById<TextView>(R.id.text).text = item.text
                panel.findViewById<CheckBox>(R.id.complete).apply {
                    isChecked = item.complete
                    bindCheckBoxClicked(this, item.id, onClick)
                }
                panel.setOnClickListener {
                    startActivity(Intent(this, TodoDetailActivity::class.java).apply {
                        putExtra(TodoDetailActivity.KEY_TODO_ID, item.id)
                    })
                }

                layout.addView(panel)
            }
        })
    }

    private fun bindCheckBoxClicked(checkBox: CheckBox, id: Int, onClick: (Int) -> Unit) {
        checkBox.setOnClickListener { onClick(id) }
    }

    private fun bindNewTodo(button: FloatingActionButton, onClick: () -> Unit) {
        button.setOnClickListener { onClick() }
    }
}
