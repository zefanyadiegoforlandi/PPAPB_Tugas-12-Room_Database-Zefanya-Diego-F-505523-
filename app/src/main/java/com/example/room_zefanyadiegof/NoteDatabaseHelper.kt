package com.example.room_zefanyadiegof
// Mendefinisikan paket untuk kelas NoteDatabaseHelper

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class NoteDatabaseHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {
    // Mendeklarasikan kelas NoteDatabaseHelper yang merupakan turunan dari SQLiteOpenHelper

    companion object {
        private const val DATABASE_NAME = "notes_app"
        private const val DATABASE_VERSION = 1
        private const val TABLE_NAME = "allnotes"
        private const val COLUMN_ID = "id"
        private const val COLUMN_TITLE = "title"
        private const val COLUMN_CONTENT = "content"
        // Mendeklarasikan konstanta-konstanta untuk nama database, versi database, nama tabel, dan kolom-kolom dalam tabel
    }

    override fun onCreate(db: SQLiteDatabase?) {
        val createTabQuery = "CREATE TABLE $TABLE_NAME ($COLUMN_ID INTEGER PRIMARY KEY, $COLUMN_TITLE TEXT, $COLUMN_CONTENT TEXT)"
        db?.execSQL(createTabQuery)
        // Membuat tabel database ketika pertama kali dibuat
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        val dropTabQuery = "DROP TABLE IF EXISTS $TABLE_NAME"
        db?.execSQL(dropTabQuery)
        onCreate(db)
        // Mengupgrade tabel database jika versi berubah
    }

    fun insertNote(note: Note) {
        if (note.title.isNotEmpty() && note.content.isNotEmpty()) {
            val db = writableDatabase
            val values = ContentValues().apply {
                put(COLUMN_TITLE, note.title)
                put(COLUMN_CONTENT, note.content)
            }
            db.insert(TABLE_NAME, null, values)
            db.close()
        } else {
            throw IllegalArgumentException("Judul dan konten catatan harus diisi.")
        }
        // Menyisipkan catatan baru ke dalam tabel database
    }

    fun getAllNotes(): List<Note> {
        val noteList = mutableListOf<Note>()
        val db = readableDatabase
        val query = "SELECT * FROM $TABLE_NAME"
        val cursor = db.rawQuery(query, null)

        while (cursor.moveToNext()) {
            val id = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_ID))
            val title = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_TITLE))
            val content = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_CONTENT))

            val note = Note(id, title, content)
            noteList.add(note)
        }
        cursor.close()
        db.close()
        return noteList
        // Mengambil semua catatan dari tabel database dan mengembalikannya sebagai daftar objek Note
    }

    fun updateNote(note: Note) {
        val db = writableDatabase
        val values = ContentValues().apply {
            put(COLUMN_TITLE, note.title)
            put(COLUMN_CONTENT, note.content)
        }
        val whereClause = "$COLUMN_ID = ?"
        val whereArgs = arrayOf(note.id.toString())
        db.update(TABLE_NAME, values, whereClause, whereArgs)
        db.close()
        // Mengupdate catatan dalam tabel database berdasarkan ID catatan
    }

    fun getNoteByID(noteId: Int): Note {
        val db = readableDatabase
        val query = "SELECT * FROM $TABLE_NAME WHERE $COLUMN_ID = $noteId"
        val cursor = db.rawQuery(query, null)
        cursor.moveToFirst()

        val id = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_ID))
        val title = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_TITLE))
        val content = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_CONTENT))

        cursor.close()
        db.close()
        return Note(id, title, content)
        // Mengambil catatan berdasarkan ID catatan dari tabel database
    }

    fun deleteNote(noteId: Int) {
        val db = writableDatabase
        val whereClause = "$COLUMN_ID = ?"
        val whereArgs = arrayOf(noteId.toString())
        db.delete(TABLE_NAME, whereClause, whereArgs)
        db.close()
        // Menghapus catatan dari tabel database berdasarkan ID catatan
    }
}
