package com.example.filmkesifuygulamasi

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.*
import coil.compose.AsyncImage
import kotlinx.coroutines.delay

const val API_KEY = "178af4cbdd0eeb18d9799ce47d2ae238"
const val IMAGE_URL = "https://image.tmdb.org/t/p/w500/"

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MaterialTheme(colorScheme = darkColorScheme(primary = Color(0xFFE91E63))) {
                val navController = rememberNavController()
                Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background) {
                    NavHost(navController = navController, startDestination = "main_list") {
                        composable("main_list") { MovieHomeScreen(navController) }
                        composable("detail/{title}/{overview}/{vote}/{date}/{genres}") { backStack ->
                            MovieDetailScreen(
                                title = backStack.arguments?.getString("title") ?: "",
                                overview = backStack.arguments?.getString("overview") ?: "",
                                vote = backStack.arguments?.getString("vote") ?: "",
                                date = backStack.arguments?.getString("date") ?: "",
                                genres = backStack.arguments?.getString("genres") ?: "",
                                onBack = { navController.popBackStack() }
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun MovieHomeScreen(navController: NavHostController) {
    var searchQuery by remember { mutableStateOf("") }
    var searchResults by remember { mutableStateOf(listOf<Movie>()) }
    var nowPlaying by remember { mutableStateOf(listOf<Movie>()) }
    var popular by remember { mutableStateOf(listOf<Movie>()) }
    var topRated by remember { mutableStateOf(listOf<Movie>()) }
    var upcoming by remember { mutableStateOf(listOf<Movie>()) }
    var errorMessage by remember { mutableStateOf<String?>(null) }
    var isSearching by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        try {
            nowPlaying = RetrofitClient.instance.getNowPlaying(API_KEY).results
            popular = RetrofitClient.instance.getPopular(API_KEY).results
            topRated = RetrofitClient.instance.getTopRated(API_KEY).results
            upcoming = RetrofitClient.instance.getUpcoming(API_KEY).results
            errorMessage = null
        } catch (e: Exception) {
            errorMessage = "İnternet bağlantınızı kontrol edin."
        }
    }

    LaunchedEffect(searchQuery) {
        if (searchQuery.length > 2) {
            isSearching = true
            delay(500)
            try {
                searchResults = RetrofitClient.instance.searchMovies(API_KEY, searchQuery).results
            } catch (e: Exception) { e.printStackTrace() }
            isSearching = false
        } else {
            searchResults = emptyList()
        }
    }

    Column(modifier = Modifier.fillMaxSize()) {
        Text("CineTrack", style = MaterialTheme.typography.displaySmall, modifier = Modifier.padding(16.dp), fontWeight = FontWeight.Bold, color = Color(0xFFE91E63))

        errorMessage?.let {
            Card(colors = CardDefaults.cardColors(containerColor = Color.Red.copy(alpha = 0.1f)), modifier = Modifier.padding(16.dp).fillMaxWidth()) {
                Text(it, color = Color.Red, modifier = Modifier.padding(16.dp))
            }
        }

        OutlinedTextField(
            value = searchQuery,
            onValueChange = { searchQuery = it },
            modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp),
            placeholder = { Text("Film ara...") },
            leadingIcon = { Icon(Icons.Default.Search, contentDescription = null) },
            shape = RoundedCornerShape(12.dp),
            singleLine = true
        )

        Spacer(modifier = Modifier.height(8.dp))

        if (searchQuery.length > 2) {
            if (searchResults.isEmpty() && !isSearching) {
                // SONUÇ BULUNAMADI UYARISI
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text("Aradığınız kriterde film bulunamadı.", textAlign = TextAlign.Center, color = Color.Gray)
                }
            } else {
                LazyVerticalGrid(columns = GridCells.Fixed(2), contentPadding = PaddingValues(16.dp), modifier = Modifier.fillMaxSize()) {
                    items(searchResults) { movie ->
                        MovieCard(movie) {
                            val genres = getGenreNames(movie.genreIds)
                            val cleanDesc = movie.overview.replace("/", "-").ifEmpty { "Özet yok." }
                            navController.navigate("detail/${movie.title}/$cleanDesc/${movie.voteAverage}/${movie.releaseDate}/$genres")
                        }
                    }
                }
            }
        } else {
            Column(modifier = Modifier.verticalScroll(rememberScrollState())) {
                MovieCategoryRow("Vizyondakiler", nowPlaying, navController)
                MovieCategoryRow("Popüler Filmler", popular, navController)
                MovieCategoryRow("En Çok Oy Alanlar", topRated, navController)
                MovieCategoryRow("Yakında Gelecekler", upcoming, navController)
            }
        }
    }
}

@Composable
fun MovieCategoryRow(title: String, movies: List<Movie>, navController: NavHostController) {
    Column {
        Text(title, style = MaterialTheme.typography.titleLarge, modifier = Modifier.padding(16.dp, 16.dp, 8.dp, 8.dp), fontWeight = FontWeight.SemiBold)
        LazyRow(contentPadding = PaddingValues(horizontal = 8.dp)) {
            items(movies) { movie ->
                MovieCard(movie) {
                    val genres = getGenreNames(movie.genreIds)
                    val cleanDesc = movie.overview.replace("/", "-").ifEmpty { "Özet yok." }
                    navController.navigate("detail/${movie.title}/$cleanDesc/${movie.voteAverage}/${movie.releaseDate}/$genres")
                }
            }
        }
    }
}

@Composable
fun MovieCard(movie: Movie, onClick: () -> Unit) {
    Card(modifier = Modifier.padding(8.dp).width(140.dp).clickable { onClick() }, elevation = CardDefaults.cardElevation(4.dp)) {
        Column {
            AsyncImage(model = IMAGE_URL + movie.posterPath, contentDescription = null, modifier = Modifier.height(200.dp).fillMaxWidth(), contentScale = ContentScale.Crop)
            Text(movie.title, modifier = Modifier.padding(8.dp), maxLines = 1, overflow = TextOverflow.Ellipsis)
        }
    }
}

@Composable
fun MovieDetailScreen(title: String, overview: String, vote: String, date: String, genres: String, onBack: () -> Unit) {
    Column(modifier = Modifier.fillMaxSize().padding(16.dp).verticalScroll(rememberScrollState())) {
        Button(onClick = onBack) { Text("← Geri") }
        Spacer(modifier = Modifier.height(16.dp))
        Text(title, style = MaterialTheme.typography.headlineLarge, fontWeight = FontWeight.Bold)
        Text("🎭 $genres", color = Color(0xFFE91E63), style = MaterialTheme.typography.bodyMedium)
        Row(modifier = Modifier.padding(vertical = 8.dp)) {
            Text("⭐ $vote", color = Color(0xFFFFC107), fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.width(16.dp))
            Text("📅 $date", color = Color.Gray)
        }
        Text("Özet", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
        Text(overview, style = MaterialTheme.typography.bodyLarge, modifier = Modifier.padding(top = 8.dp))
    }
}