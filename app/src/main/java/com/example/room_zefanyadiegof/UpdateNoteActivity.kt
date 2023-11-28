// Deklarasi package, menentukan nama paket untuk file Kotlin ini.
package com.example.room_zefanyadiegof

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.firestore.FirebaseFirestore
import com.example.room_zefanyadiegof.databinding.ActivityUpdateNoteBinding

// Mendeklarasikan kelas untuk UpdateNoteActivity, yang mengembangkan AppCompatActivity.
class UpdateNoteActivity : AppCompatActivity() {

    // Variabel yang ditunda inisialisasinya untuk View Binding dan Firebase Firestore.
    private lateinit var binding: ActivityUpdateNoteBinding
    private lateinit var firestore: FirebaseFirestore

    // Variabel untuk menyimpan ID catatan yang akan diperbarui.
    private var noteId: String = ""

    // Metode yang dipanggil saat aktivitas dibuat.
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Inflating layout menggunakan View Binding dan menetapkannya sebagai tampilan konten.
        binding = ActivityUpdateNoteBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Mendapatkan instance FirebaseFirestore.
        firestore = FirebaseFirestore.getInstance()

        // Mengambil noteId dari intent.
        noteId = intent.getStringExtra("note_id") ?: ""
        Log.d("UpdateNoteActivity", "Received note ID: $noteId")

        // Mengambil objek catatan dari intent.
        val note = intent.getSerializableExtra("note") as? Note

        // Memeriksa apakah objek catatan tidak null.
        if (note == null) {
            Log.e("UpdateNoteActivity", "Note is null. Finishing activity.")
            finish()
            return
        }

        // Log informasi tentang objek catatan yang diterima.
        Log.d("UpdateNoteActivity", "Received note: $note")
        Log.d("UpdateNoteActivity", "Note title: ${note.title}, Note content: ${note.content}")

        // Menetapkan teks judul dan konten catatan ke EditText di layout.
        binding.updateTitle.setText(note.title)
        binding.updateContent.setText(note.content)

        // Menyiapkan pendengar klik untuk tombol "Simpan Perubahan".
        binding.updateButtonSave.setOnClickListener {
            // Mendapatkan judul dan konten yang baru dari EditText.
            val newTitle = binding.updateTitle.text.toString()
            val newContent = binding.updateContent.text.toString()

            // Membuat objek catatan baru dengan ID yang sama dan judul serta konten yang baru.
            val updatedNote = Note(noteId, newTitle, newContent)

            // Memperbarui catatan di Firestore.
            updateNoteInFirestore(updatedNote)
        }
    }

    // Metode untuk memperbarui catatan di Firebase Firestore.
    private fun updateNoteInFirestore(updatedNote: Note) {
        // Mendapatkan referensi dokumen catatan berdasarkan ID.
        val noteRef = firestore.collection("notes").document(updatedNote.id ?: "")

        // Menggunakan metode update untuk memperbarui judul dan konten catatan di Firestore.
        noteRef.update(
            mapOf(
                "title" to updatedNote.title,
                "content" to updatedNote.content
            )
        )
            .addOnSuccessListener {
                // Menampilkan pesan sukses jika pembaruan berhasil dan mengembalikan hasil ke MainActivity.
                Toast.makeText(this, "Catatan diperbarui", Toast.LENGTH_SHORT).show()
                returnResultToMainActivity(updatedNote)
            }
            .addOnFailureListener { exception ->
                // Menampilkan pesan kesalahan jika pembaruan gagal.
                Toast.makeText(
                    this,
                    "Gagal memperbarui catatan: ${exception.message}",
                    Toast.LENGTH_SHORT
                ).show()
                // Log kesalahan untuk memeriksa detailnya.
                Log.e("UpdateNoteActivity", "Error updating note in Firestore", exception)
            }
    }

    // Metode untuk mengembalikan hasil ke MainActivity dengan membawa objek catatan yang diperbarui.
    private fun returnResultToMainActivity(updatedNote: Note) {
        // Membuat intent dan menambahkan objek catatan yang diperbarui.
        val resultIntent = intent
        resultIntent.putExtra("updatedNote", updatedNote)

        // Menetapkan hasil RESULT_OK dan mengakhiri aktivitas.
        setResult(RESULT_OK, resultIntent)
        finish()
    }
}
