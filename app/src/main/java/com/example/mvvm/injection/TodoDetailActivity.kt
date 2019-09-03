package com.example.mvvm.injection

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.Menu
import android.view.MenuItem
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.example.mvvm.injection.viewmodel.TodoDetailViewModel

class TodoDetailActivity: AppCompatActivity() {
    companion object {
        const val KEY_TODO_ID = "key_todo_id"
    }

    private lateinit var viewModel: TodoDetailViewModel
    private var todoId: Int = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_todo_detail)

        viewModel = ViewModelProviders.of(this).get(TodoDetailViewModel::class.java)

        // If a id was given as an argument, the item is updated.
        todoId = intent.getIntExtra(KEY_TODO_ID, -1)
        if(todoId >= 0) {
            setTitle(R.string.title_edit_todo)
            viewModel.setCurrentTodoText(todoId)
        } else {
            setTitle(R.string.title_new_todo)
        }

        actionBar?.setDisplayHomeAsUpEnabled(true)
        bindEditText(findViewById(R.id.text), viewModel.text)
    }

    override fun onBackPressed() {
        super.onBackPressed()

        if(todoId >= 0) {
            viewModel.updateTodoItem(todoId)
        } else {
            viewModel.newTodoItem()
        }
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when(item?.itemId) {
            android.R.id.home -> {
                if(todoId >= 0) {
                    viewModel.updateTodoItem(todoId)
                } else {
                    viewModel.newTodoItem()
                }
            }

            R.id.delete -> {
                viewModel.deleteTodo(todoId)
                finish()
            }
        }

        return super.onOptionsItemSelected(item)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        return if(todoId >= 0) {
            menuInflater.inflate(R.menu.menu_edit_todo, menu)
            true
        } else {
            super.onCreateOptionsMenu(menu)
        }
    }

    private fun bindEditText(editText: EditText, text: MutableLiveData<String>) {

        text.observe(this, Observer {
            if(haveContentsChanged(editText.text, text.value)) {
                editText.setText(text.value)
            }
        })

        editText.addTextChangedListener(object: TextWatcher {
            override fun afterTextChanged(s: Editable?) {}
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) { text.value = s.toString() }
        })
    }

    private fun haveContentsChanged(str1: CharSequence?, str2: CharSequence?): Boolean {

        if (str1 == null != (str2 == null)) {
            return true
        } else if (str1 == null) {
            return false
        }

        val length = str1.length
        if(length != str2!!.length) {
            return true
        }

        for(i in 0 until length) {
            if(str1[i] != str2[i]) {
                return true
            }
        }

        return false
    }
}
