package com.example.biodata

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.RadioButton
import androidx.appcompat.app.AppCompatActivity

class AddActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add)
        setupViews()
    }

    private fun setupViews() {
        val buttonAdd = findViewById<Button>(R.id.buttonAdd)
        val editTextName = findViewById<EditText>(R.id.editTextName)
        val radioButtonMale = findViewById<RadioButton>(R.id.radioButtonMale)
        val editTextPlaceOfBirth = findViewById<EditText>(R.id.editTextPlaceOfBirth)
        val editTextDateOfBirth = findViewById<EditText>(R.id.editTextDateOfBirth)
        val editTextAddress = findViewById<EditText>(R.id.editTextAddress)

        buttonAdd.setOnClickListener {
            // Retrieve values from fields
            val name = editTextName.text.toString()
            val gender = if (radioButtonMale.isChecked) "Laki-laki" else "Perempuan"
            val placeOfBirth = editTextPlaceOfBirth.text.toString()
            val dateOfBirth = editTextDateOfBirth.text.toString()
            val address = editTextAddress.text.toString()

            // Create an Intent to return the data to MainActivity
            val resultIntent = Intent()
            resultIntent.putExtra("newData", "$name,$gender,$placeOfBirth,$dateOfBirth,$address")

            // Set the result to RESULT_OK and attach the Intent containing the data
            setResult(Activity.RESULT_OK, resultIntent)

            // Finish AddActivity and return to MainActivity
            finish()
        }
    }
}