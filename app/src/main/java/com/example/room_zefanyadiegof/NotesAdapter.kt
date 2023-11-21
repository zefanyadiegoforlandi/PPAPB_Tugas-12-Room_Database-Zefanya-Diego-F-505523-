package com.example.room_zefanyadiegof
// Mendefinisikan paket untuk kelas NotesAdapter

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.room_zefanyadiegof.R
// Mengimpor kelas-kelas yang diperlukan

class NotesAdapter(private var notes: List<Note>, context: Context) :
    RecyclerView.Adapter<NotesAdapter.NoteViewHolder>() {
    // Mendeklarasikan kelas NotesAdapter yang merupakan turunan dari RecyclerView.Adapter

    private val db: NoteDatabaseHelper = NoteDatabaseHelper(context)
    // Membuat objek NoteDatabaseHelper untuk interaksi dengan database

    class NoteViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        // Mendeklarasikan kelas NoteViewHolder yang merupakan turunan dari RecyclerView.ViewHolder
        val titleTextView: TextView = itemView.findViewById(R.id.title_Text)
        val contentTextView: TextView = itemView.findViewById(R.id.content_Text)
        val updateButton: ImageView = itemView.findViewById(R.id.update_Button)
        val deleteButton: ImageView = itemView.findViewById(R.id.delete_Button)
        // Mendeklarasikan elemen-elemen UI yang akan ditampilkan di setiap item recyclerview
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NoteViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.note_item, parent, false)
        return NoteViewHolder(view)
        // Meng-inflate layout item recyclerview dan mengembalikan instance NoteViewHolder
    }

    override fun getItemCount(): Int = notes.size
    // Mengembalikan jumlah item dalam recyclerview, yaitu jumlah catatan dalam daftar

    override fun onBindViewHolder(holder: NoteViewHolder, position: Int) {
        val note = notes[position]
        holder.titleTextView.text = note.title
        holder.contentTextView.text = note.content
        // Mengisi data catatan ke dalam elemen-elemen UI di setiap item recyclerview

        holder.updateButton.setOnClickListener{
            // Menambahkan listener untuk tombol "Update"
            val intent = Intent(holder.itemView.context, UpdateNoteActivity::class.java).apply {
                putExtra("note_id", note.id)
            }
            holder.itemView.context.startActivity(intent)
            // Membuat intent untuk berpindah ke UpdateNoteActivity dengan membawa ID catatan
        }

        holder.deleteButton.setOnClickListener{
            // Menambahkan listener untuk tombol "Delete"
            // Menampilkan dialog konfirmasi sebelum menghapus
            showDeleteConfirmationDialog(holder.itemView.context, note)
        }
    }

    private fun showDeleteConfirmationDialog(context: Context, note: Note) {
        // Menampilkan dialog konfirmasi sebelum menghapus catatan
        val builder = AlertDialog.Builder(context)
        builder.setTitle("Konfirmasi Hapus")
        builder.setMessage("Apakah Anda yakin ingin menghapus catatan ini?")

        builder.setPositiveButton("Ya") { _, _ ->
            // Hapus catatan jika pengguna menekan tombol "Ya"
            db.deleteNote(note.id)
            refreshData(db.getAllNotes())
            Toast.makeText(context, "Catatan dihapus", Toast.LENGTH_SHORT).show()
        }

        builder.setNegativeButton("Tidak") { _, _ ->
            // Tidak melakukan apa-apa jika pengguna memilih untuk tidak menghapus
        }

        val dialog = builder.create()
        dialog.show()
    }

    fun refreshData(newNotes: List<Note>) {
        // Memperbarui data dalam adapter dan memberitahu recyclerview bahwa data telah berubah
        notes = newNotes
        notifyDataSetChanged()
    }
}
