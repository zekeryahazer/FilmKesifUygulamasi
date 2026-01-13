package com.example.filmkesifuygulamasi

import android.net.Uri
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.*
import androidx.navigation.navArgument
import coil.compose.AsyncImage
import com.google.gson.annotations.SerializedName
import kotlinx.coroutines.delay
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

const val API_KEY = ""//TMDB Api yi giriniz
const val IMAGE_BASE_URL = "https://image.tmdb.org/t/p/w500/"

data class SimpleMovieResponse(
    val results: List<SimpleMovie>?
)

data class SimpleMovie(
    val id: Int,
    val title: String?,
    val overview: String?,
    @SerializedName("poster_path") val posterPath: String?,
    @SerializedName("release_date") val releaseDate: String?,
    @SerializedName("vote_average") val voteAverage: Double?,
    @SerializedName("genre_ids") val genreIds: List<Int>?
)

interface SimpleTmdbApi {
    @GET("movie/now_playing")
    suspend fun getNowPlaying(@Query("api_key") apiKey: String): SimpleMovieResponse

    @GET("movie/popular")
    suspend fun getPopular(@Query("api_key") apiKey: String): SimpleMovieResponse

    @GET("movie/top_rated")
    suspend fun getTopRated(@Query("api_key") apiKey: String): SimpleMovieResponse

    @GET("movie/upcoming")
    suspend fun getUpcoming(@Query("api_key") apiKey: String): SimpleMovieResponse

    @GET("search/movie")
    suspend fun searchMovies(@Query("api_key") apiKey: String, @Query("query") query: String): SimpleMovieResponse
}

object SimpleRetrofitClient {
    private const val BASE_URL = "https://api.themoviedb.org/3/"
    val api: SimpleTmdbApi by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(SimpleTmdbApi::class.java)
    }
}

fun getSafeGenreNames(ids: List<Int>?): String {
    if (ids == null) return "Genel"
    val genres = mapOf(
        28 to "Aksiyon", 12 to "Macera", 16 to "Animasyon", 35 to "Komedi",
        80 to "Suç", 99 to "Belgesel", 18 to "Dram", 10751 to "Aile",
        14 to "Fantastik", 36 to "Tarih", 27 to "Korku", 10402 to "Müzik",
        9648 to "Gizem", 10749 to "Romantik", 878 to "Bilim Kurgu",
        10770 to "TV Filmi", 53 to "Gerilim", 10752 to "Savaş", 37 to "Vahşi Batı"
    )
    return ids.mapNotNull { genres[it] }.joinToString(", ").ifEmpty { "Genel" }
}

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MaterialTheme(
                colorScheme = darkColorScheme(
                    primary = Color(0xFFE91E63),
                    background = Color(0xFF121212),
                    surface = Color(0xFF1E1E1E)
                )
            ) {
                val navController = rememberNavController()
                Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background) {
                    NavHost(navController = navController, startDestination = "main_list") {
                        composable("main_list") { MovieHomeScreen(navController) }
                        composable("profile") { ProfileScreen(navController) }
                        composable(
                            "detail/{title}/{overview}/{vote}/{date}/{genres}/{posterPath}",
                            arguments = listOf(
                                navArgument("title") { type = NavType.StringType },
                                navArgument("overview") { type = NavType.StringType },
                                navArgument("vote") { type = NavType.StringType },
                                navArgument("date") { type = NavType.StringType },
                                navArgument("genres") { type = NavType.StringType },
                                navArgument("posterPath") { type = NavType.StringType }
                            )
                        ) { backStack ->
                            MovieDetailScreen(
                                title = backStack.arguments?.getString("title") ?: "",
                                overview = backStack.arguments?.getString("overview") ?: "",
                                vote = backStack.arguments?.getString("vote") ?: "",
                                date = backStack.arguments?.getString("date") ?: "",
                                genres = backStack.arguments?.getString("genres") ?: "",
                                posterPath = backStack.arguments?.getString("posterPath") ?: "",
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
    var searchResults by remember { mutableStateOf(listOf<SimpleMovie>()) }

    var nowPlaying by remember { mutableStateOf(listOf<SimpleMovie>()) }
    var popular by remember { mutableStateOf(listOf<SimpleMovie>()) }
    var topRated by remember { mutableStateOf(listOf<SimpleMovie>()) }
    var upcoming by remember { mutableStateOf(listOf<SimpleMovie>()) }

    var errorMessage by remember { mutableStateOf<String?>(null) }
    var searchError by remember { mutableStateOf<String?>(null) }
    var isSearching by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        try {
            nowPlaying = SimpleRetrofitClient.api.getNowPlaying(API_KEY).results ?: emptyList()
            popular = SimpleRetrofitClient.api.getPopular(API_KEY).results ?: emptyList()
            topRated = SimpleRetrofitClient.api.getTopRated(API_KEY).results ?: emptyList()
            upcoming = SimpleRetrofitClient.api.getUpcoming(API_KEY).results ?: emptyList()
        } catch (e: Exception) {
            errorMessage = "Bağlantı hatası: ${e.localizedMessage}"
        }
    }

    LaunchedEffect(searchQuery) {
        if (searchQuery.length > 2) {
            isSearching = true
            searchError = null
            delay(800)
            try {
                val response = SimpleRetrofitClient.api.searchMovies(API_KEY, searchQuery)
                searchResults = response.results ?: emptyList()
                if (searchResults.isEmpty()) {
                    searchError = "Sonuç bulunamadı."
                }
            } catch (e: Exception) {
                searchError = "Arama hatası: ${e.localizedMessage}"
                e.printStackTrace()
            } finally {
                isSearching = false
            }
        } else {
            searchResults = emptyList()
            searchError = null
            isSearching = false
        }
    }

    Column(modifier = Modifier.fillMaxSize()) {
        Row(
            modifier = Modifier.fillMaxWidth().padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text("CineTrack", style = MaterialTheme.typography.displaySmall, fontWeight = FontWeight.Bold, color = Color(0xFFE91E63))
            IconButton(onClick = { navController.navigate("profile") }) {
                Icon(Icons.Default.AccountCircle, contentDescription = "Profil", tint = Color.White, modifier = Modifier.size(36.dp))
            }
        }

        errorMessage?.let {
            Text(it, color = Color.Red, modifier = Modifier.padding(16.dp), fontSize = 12.sp)
        }

        OutlinedTextField(
            value = searchQuery,
            onValueChange = { searchQuery = it },
            modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp),
            placeholder = { Text("Film ara... (en az 3 harf)") },
            leadingIcon = { Icon(Icons.Default.Search, contentDescription = null, tint = Color.Gray) },
            shape = RoundedCornerShape(12.dp),
            singleLine = true,
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = Color(0xFFE91E63),
                unfocusedBorderColor = Color.Gray
            )
        )

        Spacer(modifier = Modifier.height(8.dp))

        if (searchQuery.length > 2) {
            if (isSearching) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator(color = Color(0xFFE91E63))
                }
            } else if (!searchError.isNullOrEmpty()) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Icon(Icons.Default.Warning, contentDescription = null, tint = Color.Gray)
                        Text(searchError!!, color = Color.Gray, modifier = Modifier.padding(top = 8.dp))
                    }
                }
            } else {
                LazyVerticalGrid(columns = GridCells.Fixed(2), contentPadding = PaddingValues(16.dp), modifier = Modifier.fillMaxSize()) {
                    items(searchResults) { movie ->
                        MovieCard(movie) { navigateToDetail(navController, movie) }
                    }
                }
            }
        } else {
            Column(modifier = Modifier.verticalScroll(rememberScrollState())) {
                MovieCategoryRow("Vizyondakiler", nowPlaying, navController)
                MovieCategoryRow("Popüler Filmler", popular, navController)
                MovieCategoryRow("En Çok Oy Alanlar", topRated, navController)
                MovieCategoryRow("Yakında Gelecekler", upcoming, navController)
                Spacer(modifier = Modifier.height(24.dp))
            }
        }
    }
}

fun navigateToDetail(navController: NavHostController, movie: SimpleMovie) {
    val genres = getSafeGenreNames(movie.genreIds)
    val cleanDesc = if (movie.overview.isNullOrBlank()) "Özet bulunmuyor." else Uri.encode(movie.overview)
    val safeDate = movie.releaseDate ?: "Tarih Yok"
    val safeTitle = movie.title ?: "İsimsiz Film"
    val safeVote = movie.voteAverage?.toString() ?: "0.0"
    val safePoster = movie.posterPath?.removePrefix("/") ?: "null"

    navController.navigate("detail/$safeTitle/$cleanDesc/$safeVote/$safeDate/$genres/$safePoster")
}

@Composable
fun MovieCategoryRow(title: String, movies: List<SimpleMovie>, navController: NavHostController) {
    if (movies.isNotEmpty()) {
        Column {
            Text(title, style = MaterialTheme.typography.titleLarge, modifier = Modifier.padding(16.dp, 16.dp, 8.dp, 8.dp), fontWeight = FontWeight.SemiBold, color = Color.White)
            LazyRow(contentPadding = PaddingValues(horizontal = 8.dp)) {
                items(movies) { movie ->
                    MovieCard(movie) { navigateToDetail(navController, movie) }
                }
            }
        }
    }
}

@Composable
fun MovieCard(movie: SimpleMovie, onClick: () -> Unit) {
    Card(
        modifier = Modifier.padding(8.dp).width(140.dp).clickable { onClick() },
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFF2A2A2A)),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Column {
            if (movie.posterPath != null) {
                AsyncImage(
                    model = IMAGE_BASE_URL + movie.posterPath,
                    contentDescription = null,
                    modifier = Modifier.height(200.dp).fillMaxWidth(),
                    contentScale = ContentScale.Crop
                )
            } else {
                Box(modifier = Modifier.height(200.dp).fillMaxWidth().background(Color.DarkGray), contentAlignment = Alignment.Center) {
                    Text("Resim Yok", color = Color.White, fontSize = 12.sp)
                }
            }
            Text(
                text = movie.title ?: "Bilinmiyor",
                modifier = Modifier.padding(8.dp),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                style = MaterialTheme.typography.bodyMedium,
                color = Color.White
            )
        }
    }
}

@Composable
fun MovieDetailScreen(title: String, overview: String, vote: String, date: String, genres: String, posterPath: String, onBack: () -> Unit) {
    val decodedOverview = Uri.decode(overview)

    Column(modifier = Modifier.fillMaxSize().background(Color(0xFF121212)).verticalScroll(rememberScrollState())) {
        Box(modifier = Modifier.fillMaxWidth().height(450.dp)) {
            if (posterPath != "null") {
                AsyncImage(
                    model = IMAGE_BASE_URL + posterPath,
                    contentDescription = null,
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop
                )
            } else {
                Box(modifier = Modifier.fillMaxSize().background(Color.DarkGray))
            }
            IconButton(onClick = onBack, modifier = Modifier.padding(16.dp).background(Color.Black.copy(0.6f), CircleShape)) {
                Icon(Icons.Default.ArrowBack, contentDescription = "Geri", tint = Color.White)
            }
            Box(modifier = Modifier.fillMaxSize().background(Brush.verticalGradient(colors = listOf(Color.Transparent, Color(0xFF121212)), startY = 300f)))
        }

        Column(modifier = Modifier.padding(16.dp)) {
            Text(title, style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.Bold, color = Color.White)
            Spacer(modifier = Modifier.height(8.dp))
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text("⭐ $vote", color = Color(0xFFFFC107), fontWeight = FontWeight.Bold, fontSize = 18.sp)
                Spacer(modifier = Modifier.width(16.dp))
                Text("📅 $date", color = Color.LightGray)
            }
            Spacer(modifier = Modifier.height(8.dp))
            Text("🎭 $genres", color = Color(0xFFE91E63), style = MaterialTheme.typography.bodyLarge, fontWeight = FontWeight.Medium)
            Spacer(modifier = Modifier.height(24.dp))
            Text("Özet", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold, color = Color.White)
            Text(decodedOverview, style = MaterialTheme.typography.bodyLarge, color = Color.LightGray, modifier = Modifier.padding(top = 8.dp), lineHeight = 24.sp)
        }
    }
}

@Composable
fun ProfileScreen(navController: NavHostController) {
    Column(modifier = Modifier.fillMaxSize().background(Color(0xFF121212)).padding(16.dp), horizontalAlignment = Alignment.CenterHorizontally) {
        Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.CenterStart) {
            IconButton(onClick = { navController.popBackStack() }) {
                Icon(Icons.Default.ArrowBack, contentDescription = "Geri", tint = Color.White)
            }
        }
        Spacer(modifier = Modifier.height(40.dp))
        Box(modifier = Modifier.size(120.dp).background(Color(0xFFE91E63), CircleShape), contentAlignment = Alignment.Center) {
            Icon(Icons.Default.AccountCircle, contentDescription = null, tint = Color.White, modifier = Modifier.size(100.dp))
        }
        Spacer(modifier = Modifier.height(24.dp))
        Text("CineTrack Kullanıcısı", style = MaterialTheme.typography.headlineMedium, color = Color.White, fontWeight = FontWeight.Bold)
        Text("Premium Üye", style = MaterialTheme.typography.bodyLarge, color = Color.Gray)
    }
}
