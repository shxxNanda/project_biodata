package com.example.biodata

import DataModel
import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.ContextMenu
import android.view.MenuItem
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.ListView
import android.widget.RadioGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.database.*
import com.google.firebase.database.DatabaseReference

class MainActivity : AppCompatActivity() {

    private lateinit var listView: ListView
    private lateinit var fabAdd: FloatingActionButton
    private lateinit var adapter: ArrayAdapter<String>
    private val dataList = mutableListOf<String>()
    private lateinit var database: DatabaseReference

    companion object {
        const val ADD_ACTIVITY_REQUEST_CODE = 1
        const val EDIT_ACTIVITY_REQUEST_CODE = 2
        const val MENU_ITEM_EDIT = 1
        const val MENU_ITEM_DELETE = 2
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Initialize Firebase Realtime Database
        database = FirebaseDatabase.getInstance().reference.child("biodata")

        // Initialize ListView and FloatingActionButton
        listView = findViewById(R.id.listView)
        fabAdd = findViewById(R.id.fab_add)

        // Initialize adapter for ListView
        adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, dataList)
        listView.adapter = adapter

        // Set listener for FloatingActionButton
        fabAdd.setOnClickListener {
            // When the FloatingActionButton is clicked, start the AddActivity
            val intent = Intent(this, AddActivity::class.java)
            startActivityForResult(intent, ADD_ACTIVITY_REQUEST_CODE)
        }
        // Register ListView for context menu
        registerForContextMenu(listView)

        // Set listener for item click to show context menu
        listView.onItemClickListener = AdapterView.OnItemClickListener { parent, view, position, id ->
            listView.showContextMenuForChild(view)
        }

        // Load existing data from Firebase Realtime Database
        loadDataFromFirebase()
    }

    private fun loadDataFromFirebase() {
        database.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                // Hapus semua data dari dataList
                dataList.clear()

                for (dataSnapshot in snapshot.children) {
                    val data = dataSnapshot.getValue(String::class.java)
//                    data?.documentId = dataSnapshot.key ?: ""
                    data?.let {
                        dataList.add(it)
                    }
                }

                // Notifikasi adapter tentang perubahan data
                adapter.notifyDataSetChanged()
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(this@MainActivity, "Failed to load data: ${error.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }


    // Method to add data to the list and update the ListView display
    private fun addData(data: String) {
        // Add data to the list
        dataList.add(data)
        // Update ListView display
        adapter.notifyDataSetChanged()

        // Upload data to Firebase Realtime Database
        uploadToFirebase(data)
    }

    // Handle result from AddActivity
    // Handle result from EditActivity
    // Override onActivityResult untuk menangani hasil dari AddActivity dan EditActivity
    override fun onContextItemSelected(item: MenuItem): Boolean {
        val info = item.menuInfo as AdapterView.AdapterContextMenuInfo
        val position = info.position
        val selectedItem = adapter.getItem(position)
        val dataKey = adapter.getItemId(position).toString() // Ambil kunci data

        return when (item.itemId) {
            R.id.menu_item_edit -> {
                // Handle "Edit Data"
                val intent = Intent(this, EditActivity::class.java)
                val dataToEdit = dataList[position]
                intent.putExtra("data_to_edit", dataToEdit)
                intent.putExtra("item_position", position) // Kirim posisi item yang akan diedit
                intent.putExtra("dataKey", dataKey) // Kirim kunci data
                startActivityForResult(intent, EDIT_ACTIVITY_REQUEST_CODE)
                true
            }
            R.id.menu_item_delete -> {
                // Handle "Hapus Data"
                dataList.removeAt(position)
                adapter.notifyDataSetChanged()
                deleteFromFirebase(dataKey) // Panggil metode deleteFromFirebase()
                true
            }
            else -> super.onContextItemSelected(item)
        }
    }

    override fun onCreateContextMenu(menu: ContextMenu?, v: View?, menuInfo: ContextMenu.ContextMenuInfo?) {
        super.onCreateContextMenu(menu, v, menuInfo)
        val inflater = menuInflater
        inflater.inflate(R.menu.menu_main, menu)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == ADD_ACTIVITY_REQUEST_CODE && resultCode == RESULT_OK) {
            val newData = data?.getStringExtra("newData")
            if (!newData.isNullOrEmpty()) {
                addData(newData)
            }
        }
        // Handle result from EditActivity here
        else if (requestCode == EDIT_ACTIVITY_REQUEST_CODE && resultCode == RESULT_OK) {
            val editedData = data?.getStringExtra("editedData")
            val editedDataPosition = data?.getIntExtra("editedDataPosition", -1) ?: -1
            val dataKey = data?.getStringExtra("dataKey") // Ambil kunci data dari EditActivity
            if (editedData != null && editedDataPosition != -1 && dataKey != null) {
                // Update data di dataList dengan data yang telah diedit
                dataList[editedDataPosition] = editedData

                // Notify adapter tentang perubahan data
                adapter.notifyDataSetChanged()

                updateFirebase(editedData, dataKey)
            }
        }
    }

    private fun uploadToFirebase(data: String) {
        val key = database.push().key
        key?.let {
            database.child(it).setValue(data)
                .addOnSuccessListener {
                    Toast.makeText(this, "Data berhasil diunggah dengan ID: $key", Toast.LENGTH_SHORT).show()
                }
                .addOnFailureListener { e ->
                    Toast.makeText(this, "Error: ${e.message}", Toast.LENGTH_SHORT).show()
                }
        }
    }

    private fun deleteFromFirebase(dataKey: String) {
        database.child(dataKey).removeValue()
            .addOnSuccessListener {
                Toast.makeText(this, "Data berhasil dihapus dari Firebase", Toast.LENGTH_SHORT).show()
            }
            .addOnFailureListener { e ->
                Toast.makeText(this, "Gagal menghapus data dari Firebase: ${e.message}", Toast.LENGTH_SHORT).show()
            }
    }

    private fun updateFirebase(data: String, dataKey: String) { // Perubahan pada parameter
        database.child(dataKey).setValue(data) // Perubahan pada penggunaan dataKey
            .addOnSuccessListener {
                Toast.makeText(this, "Data berhasil diupdate", Toast.LENGTH_SHORT).show()
            }
            .addOnFailureListener { e ->
                Toast.makeText(this, "Error: ${e.message}", Toast.LENGTH_SHORT).show()
            }
    }
}