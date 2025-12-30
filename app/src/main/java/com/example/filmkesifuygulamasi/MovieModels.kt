package com.example.filmkesifuygulamasi

import com.google.gson.annotations.SerializedName

data class MovieResponse(
    val results: List<Movie>
)

data class Movie(
    val id: Int,
    val title: String,
    val overview: String,
    @SerializedName("poster_path") val posterPath: String?,
    @SerializedName("vote_average") val voteAverage: Double,
    @SerializedName("release_date") val releaseDate: String,
    @SerializedName("genre_ids") val genreIds: List<Int>? = emptyList()
)


fun getGenreNames(genreIds: List<Int>?): String {
    val genreMap = mapOf(
        28 to "Aksiyon", 12 to "Macera", 16 to "Animasyon", 35 to "Komedi",
        80 to "Suç", 99 to "Belgesel", 18 to "Dram", 10751 to "Aile",
        14 to "Fantastik", 36 to "Tarih", 27 to "Korku", 10402 to "Müzik",
        9648 to "Gizem", 10749 to "Romantik", 878 to "Bilim Kurgu",
        10770 to "TV Film", 53 to "Gerilim", 10752 to "Savaş", 37 to "Vahşi Batı"
    )
    return genreIds?.map { genreMap[it] ?: "Diğer" }?.joinToString(", ") ?: "Bilinmiyor"
}