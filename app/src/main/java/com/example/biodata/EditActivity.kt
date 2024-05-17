package com.example.biodata

import DataModel
import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.biodata.databinding.ActivityEditBinding

class EditActivity : AppCompatActivity() {

    private lateinit var binding: ActivityEditBinding
    private lateinit var dataToEdit: DataModel
    private var itemPosition: Int = -1 // Simpan informasi posisi item yang akan diedit

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit)

        binding = ActivityEditBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Tangkap data yang dikirim dari AddActivity
        val name = intent.getStringExtra("name")
        val gender = intent.getStringExtra("gender")
        val placeOfBirth = intent.getStringExtra("placeOfBirth")
        val dateOfBirth = intent.getStringExtra("dateOfBirth")
        val address = intent.getStringExtra("address")

        // Gunakan data untuk menginisialisasi tampilan pengeditan
        binding.apply {
            editTextName.setText(name)
            if (gender == "Laki-laki") {
                radioButtonMale.isChecked = true
            } else {
                radioButtonFemale.isChecked = true
            }
            editTextPlaceOfBirth.setText(placeOfBirth)
            editTextDateOfBirth.setText(dateOfBirth)
            editTextAddress.setText(address)

            buttonSave.setOnClickListener {
                saveEditedData()
            }
        }
    }

    private fun saveEditedData() {
        // Mendapatkan data yang diedit dari formulir
        val editedName = binding.editTextName.text.toString()
        val editedGender = if (binding.radioButtonMale.isChecked) "Laki-laki" else "Perempuan"
        val editedPlaceOfBirth = binding.editTextPlaceOfBirth.text.toString()
        val editedDateOfBirth = binding.editTextDateOfBirth.text.toString()
        val editedAddress = binding.editTextAddress.text.toString()

        // Membuat objek DataModel dari data yang diedit
        val editedData = DataModel(editedName, editedGender, editedPlaceOfBirth, editedDateOfBirth, editedAddress)

        // Mengembalikan data yang diedit beserta posisi item ke MainActivity
        val returnIntent = Intent()
        returnIntent.putExtra("editedData", editedData)
        returnIntent.putExtra("editedDataPosition", itemPosition)

        val dataKey = intent.getStringExtra("dataKey")
        returnIntent.putExtra("dataKey", dataKey) // Kirim kembali kunci data

        setResult(Activity.RESULT_OK, returnIntent)
        finish()
    }
}
