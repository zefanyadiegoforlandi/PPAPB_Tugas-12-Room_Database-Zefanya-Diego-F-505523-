package com.example.room_zefanyadiegof
// Mendefinisikan paket untuk kelas AddNoteActivity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.room_zefanyadiegof.databinding.ActivityAddNoteBinding
// Mengimpor kelas-kelas yang diperlukan

class AddNoteActivity : AppCompatActivity() {
    // Mendeklarasikan kelas AddNoteActivity yang merupakan turunan dari AppCompatActivity

    private lateinit var binding: ActivityAddNoteBinding
    // Variabel untuk data binding

    private lateinit var db: NoteDatabaseHelper
    // Variabel untuk objek database

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddNoteBinding.inflate(layoutInflater)
        // Menginisialisasi binding menggunakan metode inflate dari ActivityAddNoteBinding

        setContentView(binding.root)
        // Mengatur tata letak aktivitas menggunakan root dari binding

        db = NoteDatabaseHelper(this)
        // Menginisialisasi objek database menggunakan NoteDatabaseHelper

        binding.ButtonBack.setOnClickListener {
            // Menambahkan listener untuk button "Kembali"
            // Panggil onBackPressed() atau finish() untuk menutup aktivitas
            onBackPressed()
        }

        binding.ButtonSave.setOnClickListener {
            // Menambahkan listener untuk button "Simpan"
            val title = binding.titleEditText.text.toString()
            val content = binding.contentEditText.text.toString()

            if (title.isNotEmpty() && content.isNotEmpty()) {
                // Memeriksa apakah judul dan konten catatan tidak kosong
                val note = Note(0, title, content)
                // Membuat objek Note dengan data dari input pengguna
                db.insertNote(note)
                // Menyimpan catatan ke database
                finish()
                // Menutup aktivitas AddNoteActivity
                Toast.makeText(this, "Note saved", Toast.LENGTH_SHORT).show()
                // Menampilkan pesan sukses bahwa catatan telah disimpan
            } else {
                // Menampilkan pesan kesalahan jika EditText tidak diisi
                Toast.makeText(this, "Judul dan konten catatan harus diisi", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
