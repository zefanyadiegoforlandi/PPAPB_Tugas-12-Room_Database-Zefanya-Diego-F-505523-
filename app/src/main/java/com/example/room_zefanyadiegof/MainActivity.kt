package com.example.room_zefanyadiegof
// Mendefinisikan paket untuk kelas MainActivity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.room_zefanyadiegof.databinding.ActivityMainBinding
// Mengimpor kelas-kelas yang diperlukan

class MainActivity : AppCompatActivity() {
    // Mendeklarasikan kelas MainActivity yang merupakan turunan dari AppCompatActivity

    private lateinit var binding: ActivityMainBinding
    // Variabel untuk data binding

    private lateinit var db: NoteDatabaseHelper
    // Variabel untuk objek database

    private lateinit var notesAdapter: NotesAdapter
    // Variabel untuk adapter recyclerview

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        // Menginisialisasi binding menggunakan metode inflate dari ActivityMainBinding

        setContentView(binding.root)
        // Mengatur tata letak aktivitas menggunakan root dari binding

        db = NoteDatabaseHelper(this)
        // Menginisialisasi objek database menggunakan NoteDatabaseHelper

        notesAdapter = NotesAdapter(db.getAllNotes(), this)
        // Menginisialisasi adapter recyclerview dengan data dari database

        binding.notesRecyclerView.layoutManager = LinearLayoutManager(this)
        // Mengatur layout manager recyclerview agar menggunakan LinearLayoutManager

        binding.notesRecyclerView.adapter = notesAdapter
        // Mengatur adapter recyclerview dengan adapter yang telah diinisialisasi

        binding.addButton.setOnClickListener{
            // Menambahkan listener untuk button "Tambahkan"
            val intent = Intent(this, AddNoteActivity::class.java)
            // Membuat intent untuk berpindah ke AddNoteActivity
            startActivity(intent)
            // Memulai aktivitas AddNoteActivity
        }
    }

    override fun onResume() {
        super.onResume()
        notesAdapter.refreshData(db.getAllNotes())
        // Memperbarui data pada adapter recyclerview saat aktivitas di-resume
    }
}
