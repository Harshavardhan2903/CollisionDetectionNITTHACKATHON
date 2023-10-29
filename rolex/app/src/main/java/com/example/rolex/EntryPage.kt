package com.example.rolex

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText

class EntryPage : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_entry_page)

        val button = findViewById<Button>(R.id.button2)
        val editText = findViewById<EditText>(R.id.editTextText)

        button.setOnClickListener {
            val textTonavigate = editText.text.toString()
            val navigate = Intent(this,MainActivity::class.java)
            navigate.putExtra("data",textTonavigate)
            startActivity(navigate)
        }
    }
}