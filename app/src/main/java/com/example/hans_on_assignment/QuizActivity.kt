package com.example.hans_on_assignment

import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.hans_on_assignment.data.AppDatabase
import kotlinx.coroutines.launch

class QuizActivity : AppCompatActivity() {

    private val db by lazy { AppDatabase.getInstance(this) }
    private val dao by lazy { db.flashcardDao() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.quiz_screen)
        val btnDeleteAll = findViewById<Button>(R.id.button_delete_all)
        val spinnerCategory = findViewById<Spinner>(R.id.spinnerCategory)
        val txtQuestion = findViewById<TextView>(R.id.tvQuestion)
        val txtAnswer = findViewById<TextView>(R.id.tvAnswer)
        val btnRandom = findViewById<Button>(R.id.btnShowRandomQuestion)
        val btnShowAnswer = findViewById<Button>(R.id.btnShowAnswer)

        lifecycleScope.launch {
            val categories = dao.getDistinctCategories()

            runOnUiThread {
                if (categories.isEmpty()) {
                    Toast.makeText(this@QuizActivity, "No categories found", Toast.LENGTH_SHORT).show()
                    return@runOnUiThread
                }

                val adapter = ArrayAdapter(
                    this@QuizActivity,
                    android.R.layout.simple_spinner_item,
                    categories
                )
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                spinnerCategory.adapter = adapter
            }
        }

        btnRandom.setOnClickListener {
            val selectedCategory = spinnerCategory.selectedItem?.toString()

            if (selectedCategory.isNullOrEmpty()) {
                Toast.makeText(this, "Select a category first", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            lifecycleScope.launch {
                val list = dao.getByCategory(selectedCategory)

                runOnUiThread {
                    if (list.isEmpty()) {
                        txtQuestion.text = "No card found in this category"
                        txtAnswer.text = ""
                        txtAnswer.visibility = View.GONE
                    } else {
                        val flashcard = list.random()

                        txtQuestion.text = flashcard.question
                        txtAnswer.text = flashcard.answer
                        txtAnswer.visibility = View.GONE
                    }
                }
            }
        }
        btnDeleteAll.setOnClickListener {
            lifecycleScope.launch {
                dao.deleteAll()
                runOnUiThread {
                    Toast.makeText(this@QuizActivity, "All flashcards deleted!", Toast.LENGTH_SHORT).show()
                }
            }
        }


        btnShowAnswer.setOnClickListener {
            txtAnswer.visibility = View.VISIBLE
        }
    }
}
