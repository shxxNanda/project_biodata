@file:Suppress("DEPRECATION")

package com.example.biodata

import DataModel
import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.biodata.databinding.ActivityEditBinding

class EditActivity : AppCompatActivity() {

    private lateinit var binding: ActivityEditBinding
    private lateinit var dataToEdit: DataModel
    private var itemPosition: Int = -1 // Simpan informasi posisi item yang akan diedit

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEditBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Tangkap data yang dikirim dari Menu Utama
        dataToEdit = intent.getParcelableExtra("data_to_edit") ?: DataModel("", "", "", "", "","")
        itemPosition = intent.getIntExtra("item_position", -1) // Ambil posisi item
        val dataKey = intent.getStringExtra("dataKey") // Ambil dataKey

        // Toast data
        val dataToast = "Data yang akan diedit: ${dataToEdit.name}" // Ubah ini sesuai kebutuhan Anda
        Toast.makeText(this, dataToast, Toast.LENGTH_SHORT).show()

        // Gunakan data untuk menginisialisasi tampilan pengeditan
        binding.apply {
            editTextName.setText(dataToEdit.name)
            if (dataToEdit.gender == "Laki-laki") {
                radioButtonMale.isChecked = true
            } else {
                radioButtonFemale.isChecked = true
            }
            editTextPlaceOfBirth.setText(dataToEdit.placeOfBirth)
            editTextDateOfBirth.setText(dataToEdit.dateOfBirth)
            editTextAddress.setText(dataToEdit.address)

            buttonSave.setOnClickListener {
                saveEditedData(dataKey)
            }
        }
    }

    private fun saveEditedData(dataKey: String?) {
        // Mendapatkan data yang diedit dari formulir
        val editedName = binding.editTextName.text.toString()
        val editedGender = if (binding.radioButtonMale.isChecked) "Laki-laki" else "Perempuan"
        val editedPlaceOfBirth = binding.editTextPlaceOfBirth.text.toString()
        val editedDateOfBirth = binding.editTextDateOfBirth.text.toString()
        val editedAddress = binding.editTextAddress.text.toString()

        // Membuat objek DataModel dari data yang diedit
        val editedData = DataModel(dataToEdit.id, editedName, editedGender, editedPlaceOfBirth, editedDateOfBirth, editedAddress)

        // Mengembalikan data yang diedit beserta posisi item ke MainActivity
        val returnIntent = Intent()
        returnIntent.putExtra("editedData", editedData)
        returnIntent.putExtra("editedDataPosition", itemPosition)
        dataKey?.let { returnIntent.putExtra("dataKey", it) } // Kirim kembali kunci data jika tidak null

        setResult(Activity.RESULT_OK, returnIntent)
        finish()
    }
}