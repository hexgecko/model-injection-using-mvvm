package com.example.mvvm.injection.model.todo

import android.app.Application
import android.util.Log
import androidx.lifecycle.MutableLiveData
import org.json.JSONArray
import org.json.JSONObject
import java.io.File
import java.io.FileNotFoundException

class ProdTodoModel(private val application: Application): TodoModel {
    companion object {
        private const val TAG = "ProdTodoModel"
        private const val FILENAME = "todo_list.json"
    }

    private var nextId = 0

    override val todoList = MutableLiveData<List<TodoItem>>()

    override fun newTodo(text: String) {
        todoList.value = todoList.value?.plus( listOf(TodoItem(nextId++, text, false)) )
        save()
    }

    override fun updateTodo(id: Int, text: String) {
        todoList.value = todoList.value?.map {
            if(it.id == id) {
                TodoItem(id, text, it.complete)
            } else {
                it
            }
        }
        save()
    }

    override fun toggleCompleted(id: Int) {
        todoList.value = todoList.value?.map {
            if(it.id == id) {
                TodoItem(id, it.text, !it.complete)
            } else {
                it
            }
        }
        save()
    }

    override fun deleteTodo(id: Int) {
        todoList.value = todoList.value?.filter { it.id != id }
        save()
    }

    override fun getTodo(id: Int): TodoItem? {
        return todoList.value?.find { it.id == id }
    }

    init {
        load()
    }

    private fun save() {
        todoList.value?.let { list ->
            try {
                File(application.filesDir, FILENAME).printWriter().use { file ->
                    var count = list.size

                    file.println('{')
                    file.println("  \"nextId\": $nextId,")
                    file.println("  \"todoList\": [")
                    list.forEach {
                        file.print("    {\"id\": ${it.id}, \"text\": \"${it.text}\", \"complete\": ${it.complete}}")
                        if (count-- > 1) {
                            file.println(",")
                        } else {
                            file.println()
                        }
                    }
                    file.println("  ]")
                    file.println("}")
                }
            } catch (e: Exception) {
                Log.e(TAG, e.message)
            }
        }
    }

    @Suppress("UNCHECKED_CAST")
    private operator fun <T> JSONArray.iterator(): Iterator<T> = (0 until length()).asSequence().map{get(it) as T}.iterator()

    private fun load() {
        val list = mutableListOf<TodoItem>()
        try {
            val inputStream = File(application.filesDir, FILENAME).inputStream()
            val json = inputStream.bufferedReader().use { it.readText() }

            JSONObject(json).let {
                nextId = it.getInt("nextId")
                it.getJSONArray("todoList").iterator<JSONObject>().forEach { item ->
                    val id = item.getInt("id")
                    val text = item.getString("text")
                    val complete = item.getBoolean("complete")

                    list.add(TodoItem(id, text, complete))
                }
            }
        } catch(e: FileNotFoundException) {
            /* Do nothing */
        } catch (e: Exception) {
            Log.e(TAG, e.message)
        } finally {
            todoList.value = list
        }
    }
}
