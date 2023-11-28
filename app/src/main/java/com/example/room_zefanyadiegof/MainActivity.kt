// Deklarasi package, menentukan nama paket untuk file Kotlin ini.
package com.example.room_zefanyadiegof

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.room_zefanyadiegof.databinding.ActivityMainBinding
import com.google.firebase.firestore.FirebaseFirestore

class MainActivity : AppCompatActivity(), NotesAdapter.DeleteNoteListener {

    // Variabel yang ditunda inisialisasinya untuk View Binding, Firebase Firestore, dan Adapter catatan.
    private lateinit var binding: ActivityMainBinding
    private lateinit var firestore: FirebaseFirestore
    private lateinit var notesAdapter: NotesAdapter

    // Konstanta untuk kode permintaan saat menambah catatan.
    private val ADD_NOTE_REQUEST_CODE = 1

    // Metode yang di-override yang dipanggil ketika aktivitas dibuat.
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Inflating layout menggunakan View Binding dan menetapkannya sebagai tampilan konten.
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Mendapatkan instance FirebaseFirestore.
        firestore = FirebaseFirestore.getInstance()

        // Inisialisasi adapter catatan dengan daftar kosong.
        notesAdapter = NotesAdapter(mutableListOf())
        notesAdapter.setDeleteNoteListener(this)

        // Menetapkan LinearLayoutManager dan adapter ke RecyclerView di layout.
        binding.notesRecyclerView.layoutManager = LinearLayoutManager(this)
        binding.notesRecyclerView.adapter = notesAdapter

        // Menyiapkan pendengar klik untuk tombol "Tambah Catatan".
        binding.addButton.setOnClickListener {
            // Membuat intent untuk membuka AddNoteActivity.
            val intent = Intent(this, AddNoteActivity::class.java)

            // Memulai aktivitas AddNoteActivity dengan permintaan kode.
            startActivityForResult(intent, ADD_NOTE_REQUEST_CODE)
        }

        // Memuat catatan dari Firestore saat aktivitas dibuat.
        loadNotesFromFirestore()
    }

    // Metode yang di-override yang dipanggil setiap kali aktivitas menjadi aktif lagi.
    override fun onResume() {
        super.onResume()

        // Memuat catatan dari Firestore saat aktivitas menjadi aktif lagi.
        loadNotesFromFirestore()
    }

    // Metode yang di-override untuk menangani hasil kembali dari AddNoteActivity.
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        // Memeriksa apakah permintaan kode dan hasil sesuai.
        if (requestCode == ADD_NOTE_REQUEST_CODE && resultCode == RESULT_OK) {

            // Mendapatkan objek catatan baru dari intent.
            val newNote = data?.getSerializableExtra("newNote") as? Note

            // Menambahkan catatan baru ke adapter dan memberi tahu adapter bahwa data telah berubah.
            newNote?.let {
                notesAdapter.addNote(it)
                notesAdapter.notifyDataSetChanged()
            }
        }
    }

    // Metode untuk memuat catatan dari Firestore.
    private fun loadNotesFromFirestore() {
        firestore.collection("notes")
            .get()
            .addOnSuccessListener { result ->
                // Daftar catatan yang akan diisi dengan data dari Firestore.
                val notesList = ArrayList<Note>()

                // Iterasi melalui hasil Firestore dan mengonversi dokumen ke objek catatan.
                for (document in result) {
                    val note = document.toObject(Note::class.java)
                    notesList.add(note)
                }

                // Memperbarui data adapter dengan daftar catatan dari Firestore.
                notesAdapter.refreshData(notesList)
            }
            .addOnFailureListener { exception ->
                // Menampilkan pesan kesalahan jika gagal mengambil catatan dari Firestore.
                Toast.makeText(this, "Gagal mengambil catatan dari Firestore", Toast.LENGTH_SHORT).show()

                // Log kesalahan untuk memeriksa detailnya.
                Log.e("MainActivity", "Error loading notes from Firestore", exception)
            }
    }

    // Metode yang di-override untuk menanggapi penghapusan catatan dari adapter.
    override fun onNoteDeleted(note: Note) {
        val documentId = note.id

        // Menghapus catatan dari Firestore menggunakan ID dokumen.
        firestore.collection("notes").document(documentId)
            .delete()
            .addOnSuccessListener {
                // Menampilkan pesan sukses jika penghapusan berhasil dan memuat ulang catatan.
                Toast.makeText(this, "Catatan dihapus", Toast.LENGTH_SHORT).show()
                loadNotesFromFirestore()
            }
            .addOnFailureListener { exception ->
                // Menampilkan pesan kesalahan jika gagal menghapus catatan.
                Toast.makeText(this, "Gagal menghapus catatan: ${exception.message}", Toast.LENGTH_SHORT).show()

                // Log kesalahan untuk memeriksa detailnya.
                Log.e("MainActivity", "Error deleting note in Firestore", exception)
            }
    }
}
