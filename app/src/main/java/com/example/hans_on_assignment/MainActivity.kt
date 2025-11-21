package com.example.hans_on_assignment

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import com.example.hans_on_assignment.data.AppDatabase
import com.example.hans_on_assignment.data.Flashcard
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    private val db by lazy { AppDatabase.getInstance(this) }
    private val dao by lazy { db.flashcardDao() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        val quesEdit = findViewById<EditText>(R.id.edit_text_question)
        val ansEdit = findViewById<EditText>(R.id.edit_text_answer)
        val categoryEdit = findViewById<EditText>(R.id.edit_text_category)

        val btnAdd = findViewById<Button>(R.id.button_add_flashcard)
        val btnGoToQuiz = findViewById<Button>(R.id.button_go_to_quiz)

        btnGoToQuiz.setOnClickListener {
            val intent = Intent(this, QuizActivity::class.java)
            startActivity(intent)
        }

        btnAdd.setOnClickListener {
            val question = quesEdit.text.toString().trim()
            val answer = ansEdit.text.toString().trim()
            val categoryText = categoryEdit.text.toString().trim()

            if (question.isEmpty() || answer.isEmpty() || categoryText.isEmpty()) {
                Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val card = Flashcard(
                question = question,
                answer = answer,
                category = categoryText
            )

            lifecycleScope.launch {
                dao.insert(card)
                runOnUiThread {
                    Toast.makeText(this@MainActivity, "Flashcard added", Toast.LENGTH_SHORT).show()
                    quesEdit.text.clear()
                    ansEdit.text.clear()
                    categoryEdit.text.clear()
                }
            }
        }

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }
}
