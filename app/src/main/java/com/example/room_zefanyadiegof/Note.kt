package com.example.room_zefanyadiegof
// Mendefinisikan paket untuk kelas Note

data class Note(
    val id: Int,
    val title: String,
    val content: String,
)
// Mendeklarasikan data class Note dengan tiga properti: id, title, dan content.
// Data class secara otomatis menghasilkan fungsi-fungsi standar seperti equals(), hashCode(), dan toString().
// Data class biasanya digunakan untuk merepresentasikan data tanpa adanya logika bisnis tambahan.
