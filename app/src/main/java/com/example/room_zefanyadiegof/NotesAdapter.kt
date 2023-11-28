// Deklarasi package, menentukan nama paket untuk file Kotlin ini.
package com.example.room_zefanyadiegof

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView



// Mendeklarasikan kelas untuk adapter RecyclerView yang menangani tampilan item catatan.
class NotesAdapter(private var notes: MutableList<Note>) : RecyclerView.Adapter<NotesAdapter.NoteViewHolder>() {

    // Interface untuk mendengarkan peristiwa penghapusan catatan.
    interface DeleteNoteListener {
        fun onNoteDeleted(note: Note)
    }

    // Variabel listener untuk mendengarkan peristiwa penghapusan catatan.
    private var deleteNoteListener: DeleteNoteListener? = null

    // Metode untuk menetapkan listener untuk peristiwa penghapusan catatan.
    fun setDeleteNoteListener(listener: DeleteNoteListener) {
        deleteNoteListener = listener
    }

    // Metode untuk menghapus catatan pada posisi tertentu tanpa menghapus data lokal.
    fun removeNoteAt(position: Int) {
        // Tidak melakukan penghapusan lokal karena data seharusnya diambil dari Firebase
        // Hanya memberi tahu adapter bahwa item dihapus
        notifyItemRemoved(position)
    }

    // Kelas ViewHolder untuk menyimpan referensi tampilan item catatan.
    class NoteViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val titleTextView: TextView = itemView.findViewById(R.id.title_Text)
        val contentTextView: TextView = itemView.findViewById(R.id.content_Text)
        val updateButton: ImageView = itemView.findViewById(R.id.update_Button)
        val deleteButton: ImageView = itemView.findViewById(R.id.delete_Button)
    }

    // Metode untuk membuat ViewHolder baru saat RecyclerView membutuhkannya.
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NoteViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.note_item, parent, false)
        return NoteViewHolder(view)
    }

    // Metode untuk mendapatkan jumlah total item dalam dataset.
    override fun getItemCount(): Int = notes.size

    // Metode untuk mengikat data dari dataset ke tampilan item dalam RecyclerView.
    override fun onBindViewHolder(holder: NoteViewHolder, position: Int) {
        val note = notes[position]

        // Menetapkan teks judul dan konten catatan ke tampilan TextView di ViewHolder.
        holder.titleTextView.text = note.title
        holder.contentTextView.text = note.content

        // Menyiapkan pendengar klik untuk tombol "Update".
        holder.updateButton.setOnClickListener {
            Log.d("NotesAdapter", "Update button clicked for note ID: ${note.id}")
            Toast.makeText(holder.itemView.context, "Update button clicked for note ID: ${note.id}", Toast.LENGTH_SHORT).show()

            // Membuat intent untuk membuka UpdateNoteActivity dan menambahkan data catatan ke intent.
            val intent = Intent(holder.itemView.context, UpdateNoteActivity::class.java).apply {
                putExtra("note_id", note.id)
                putExtra("note", note)
            }
            // Memulai aktivitas dengan intent.
            holder.itemView.context.startActivity(intent)
        }

        // Menyiapkan pendengar klik untuk tombol "Delete".
        holder.deleteButton.setOnClickListener {
            // Menampilkan dialog konfirmasi penghapusan catatan.
            showDeleteConfirmationDialog(holder.itemView.context, note)
        }
    }

    // Metode untuk menampilkan dialog konfirmasi penghapusan catatan.
    private fun showDeleteConfirmationDialog(context: Context, note: Note) {
        val builder = AlertDialog.Builder(context)
        builder.setTitle("Konfirmasi Hapus")
        builder.setMessage("Apakah Anda yakin ingin menghapus catatan ini?")

        // Menetapkan tindakan positif (ya) pada dialog untuk menghapus catatan.
        builder.setPositiveButton("Ya") { _, _ ->
            deleteNoteListener?.onNoteDeleted(note)
        }

        // Menetapkan tindakan negatif (tidak) pada dialog untuk membatalkan penghapusan.
        builder.setNegativeButton("Tidak") { _, _ ->
            // Tidak melakukan apa-apa jika pengguna memilih untuk tidak menghapus.
        }

        // Membuat dan menampilkan dialog konfirmasi.
        val dialog = builder.create()
        dialog.show()
    }

    // Metode untuk menambahkan catatan baru ke dataset dan memberi tahu adapter bahwa data telah berubah.
    fun addNote(note: Note) {
        notes.add(note)
        notifyDataSetChanged()
    }

    // Metode untuk memperbarui dataset dengan daftar catatan baru dari Firebase dan memberi tahu adapter.
    fun refreshData(newNotes: List<Note>) {
        notes.clear()
        notes.addAll(newNotes)
        notifyDataSetChanged()
    }
}
