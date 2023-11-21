package com.example.room_zefanyadiegof
// Mendefinisikan paket untuk kelas UpdateNoteActivity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.room_zefanyadiegof.databinding.ActivityUpdateNoteBinding
// Mengimpor kelas-kelas yang diperlukan

class UpdateNoteActivity : AppCompatActivity() {
    // Mendeklarasikan kelas UpdateNoteActivity yang merupakan turunan dari AppCompatActivity

    private lateinit var binding: ActivityUpdateNoteBinding
    // Variabel untuk data binding

    private lateinit var db: NoteDatabaseHelper
    // Variabel untuk objek database

    private var noteId: Int = -1
    // Variabel untuk menyimpan ID catatan yang akan di-update

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUpdateNoteBinding.inflate(layoutInflater)
        // Menginisialisasi binding menggunakan metode inflate dari ActivityUpdateNoteBinding

        setContentView(binding.root)
        // Mengatur tata letak aktivitas menggunakan root dari binding

        db = NoteDatabaseHelper(this)
        // Menginisialisasi objek database menggunakan NoteDatabaseHelper

        noteId = intent.getIntExtra("note_id", -1)
        // Mendapatkan ID catatan dari intent yang dikirim oleh NotesAdapter
        if (noteId == -1){
            // Jika ID catatan tidak ditemukan, maka tutup aktivitas
            finish()
            return
        }

        val note = db.getNoteByID(noteId)
        // Mendapatkan data catatan dari database berdasarkan ID
        binding.updateTitle.setText(note.title)
        binding.updateContent.setText(note.content)
        // Menetapkan data catatan ke dalam elemen-elemen UI

        binding.updateButtonSave.setOnClickListener{
            // Menambahkan listener untuk tombol "Simpan"
            val newTitle = binding.updateTitle.text.toString()
            val newContent = binding.updateContent.text.toString()
            val updatedNote = Note(noteId, newTitle, newContent)
            // Membuat objek Note baru dengan data yang diperbarui
            db.updateNote(updatedNote)
            // Memperbarui catatan dalam database
            finish()
            // Menutup aktivitas UpdateNoteActivity
            Toast.makeText(this, "Note updated", Toast.LENGTH_SHORT).show()
            // Menampilkan pesan sukses bahwa catatan telah diperbarui
        }
    }
}
