package com.example.android_practice.presentation.ui.screens.movielist

import android.content.Context
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SearchBar
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.chuckerteam.chucker.api.Chucker
import com.example.android_practice.domain.model.Movie
import com.example.android_practice.presentation.state.UiState
import org.koin.androidx.compose.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MovieListScreen(
    onMovieClick: (Int) -> Unit,
    onFiltersClick: () -> Unit,
    viewModel: MovieListViewModel = koinViewModel(),
    context: Context = LocalContext.current
) {
    val uiState by viewModel.uiState.collectAsState()
    val searchQuery by viewModel.searchQuery.collectAsState()

    var showFavoriteDialog by remember { mutableStateOf(false) }
    var showRemoveFavoriteDialog by remember { mutableStateOf(false) }
    var selectedMovie by remember { mutableStateOf<Movie?>(null) }

    var favoriteAction by remember { mutableStateOf<Movie?>(null) }
    var removeFavoriteAction by remember { mutableStateOf<Movie?>(null) }

    LaunchedEffect(favoriteAction) {
        favoriteAction?.let { movie ->
            viewModel.addToFavorites(movie)
            favoriteAction = null
        }
    }

    LaunchedEffect(removeFavoriteAction) {
        removeFavoriteAction?.let { movie ->
            viewModel.removeFromFavorites(movie.id)
            removeFavoriteAction = null
        }
    }

    LaunchedEffect(Unit) {
        viewModel.loadPopularMovies()
    }

    val onFavoriteAction = { movie: Movie, isCurrentlyFavorite: Boolean ->
        selectedMovie = movie
        if (isCurrentlyFavorite) {
            showRemoveFavoriteDialog = true
        } else {
            showFavoriteDialog = true
        }
    }

    if (showFavoriteDialog && selectedMovie != null) {
        AlertDialog(
            onDismissRequest = { showFavoriteDialog = false },
            title = { Text("Добавить в избранное") },
            text = { Text("Добавить \"${selectedMovie!!.title}\" в избранное?") },
            confirmButton = {
                TextButton(
                    onClick = {
                        favoriteAction = selectedMovie
                        showFavoriteDialog = false
                        selectedMovie = null
                    }
                ) {
                    Text("Добавить")
                }
            },
            dismissButton = {
                TextButton(
                    onClick = {
                        showFavoriteDialog = false
                        selectedMovie = null
                    }
                ) {
                    Text("Отмена")
                }
            }
        )
    }

    if (showRemoveFavoriteDialog && selectedMovie != null) {
        AlertDialog(
            onDismissRequest = { showRemoveFavoriteDialog = false },
            title = { Text("Удалить из избранного") },
            text = { Text("Удалить \"${selectedMovie!!.title}\" из избранного?") },
            confirmButton = {
                TextButton(
                    onClick = {
                        removeFavoriteAction = selectedMovie
                        showRemoveFavoriteDialog = false
                        selectedMovie = null
                    }
                ) {
                    Text("Удалить")
                }
            },
            dismissButton = {
                TextButton(
                    onClick = {
                        showRemoveFavoriteDialog = false
                        selectedMovie = null
                    }
                ) {
                    Text("Отмена")
                }
            }
        )
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Фильмы") },
                actions = {
                    IconButton(onClick = onFiltersClick) {
                        Icon(Icons.Default.MoreVert, contentDescription = "Фильтры")
                    }
                    IconButton(
                        onClick = {
                            Chucker.getLaunchIntent(context)?.let { intent ->
                                context.startActivity(intent)
                            }
                        }
                    ) {
                        Icon(
                            imageVector = Icons.Default.Info,
                            contentDescription = "Сетевые запросы"
                        )
                    }
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            SearchBar(
                query = searchQuery,
                onQueryChange = viewModel::searchMovies,
                onSearch = { },
                active = false,
                onActiveChange = { },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                placeholder = { Text("Поиск фильмов...") },
                leadingIcon = {
                    Icon(Icons.Default.Search, contentDescription = "Поиск")
                }
            ) {}

            when (val state = uiState) {
                is UiState.Loading -> {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            CircularProgressIndicator()
                            Spacer(modifier = Modifier.height(16.dp))
                            Text("Загружаем фильмы...")
                        }
                    }
                }
                is UiState.Success -> {
                    if (state.data.isEmpty()) {
                        EmptyState(message = "Фильмы не найдены")
                    } else {
                        LazyColumn(
                            modifier = Modifier.fillMaxSize(),
                            contentPadding = PaddingValues(8.dp),
                            verticalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            items(
                                items = state.data,
                                key = { it.id }
                            ) { movie ->
                                MovieListItemWithFavorite(
                                    movie = movie,
                                    onItemClick = { onMovieClick(movie.id) },
                                    onFavoriteAction = onFavoriteAction,
                                    viewModel = viewModel
                                )
                            }
                        }
                    }
                }
                is UiState.Error -> {
                    ErrorState(
                        message = state.message,
                        onRetry = viewModel::retry
                    )
                }
            }
        }
    }
}

@Composable
fun MovieListItemWithFavorite(
    movie: Movie,
    onItemClick: () -> Unit,
    onFavoriteAction: (Movie, Boolean) -> Unit,
    viewModel: MovieListViewModel
) {
    val isFavorite by viewModel.isFavoriteFlow(movie.id).collectAsState(initial = false)

    MovieListItem(
        movie = movie,
        onItemClick = onItemClick,
        onAddToFavorites = { onFavoriteAction(movie, isFavorite) },
        isFavorite = isFavorite
    )
}

@Composable
fun MovieListItem(
    movie: Movie,
    onItemClick: () -> Unit,
    onAddToFavorites: (Movie) -> Unit,
    isFavorite: Boolean = false
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .combinedClickable(
                onClick = onItemClick,
                onLongClick = { onAddToFavorites(movie) }
            ),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp)
        ) {
            AsyncImage(
                model = movie.posterUrl,
                contentDescription = "Постер ${movie.title}",
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .size(80.dp)
                    .clip(RoundedCornerShape(8.dp)),
            )

            Spacer(modifier = Modifier.width(12.dp))

            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = movie.title,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = "${movie.year} • ${movie.genre}",
                    style = MaterialTheme.typography.bodySmall,
                    color = Color.Gray
                )

                Spacer(modifier = Modifier.height(4.dp))

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.Star,
                        contentDescription = "Рейтинг",
                        tint = Color.Yellow,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = movie.rating.toString(),
                        style = MaterialTheme.typography.bodySmall,
                        fontWeight = FontWeight.Medium
                    )

                    if (isFavorite) {
                        Spacer(modifier = Modifier.width(8.dp))
                        Icon(
                            imageVector = Icons.Default.Favorite,
                            contentDescription = "В избранном",
                            tint = Color.Red,
                            modifier = Modifier.size(16.dp)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = movie.description.take(80) + if (movie.description.length > 80) "..." else "",
                    style = MaterialTheme.typography.bodySmall,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
            }
        }
    }
}

@Composable
fun EmptyState(message: String) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Icon(
                imageVector = Icons.Default.Search,
                contentDescription = "Не найдено",
                tint = Color.Gray,
                modifier = Modifier.size(64.dp)
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = message,
                style = MaterialTheme.typography.bodyMedium,
                color = Color.Gray
            )
        }
    }
}

@Composable
fun ErrorState(
    message: String,
    onRetry: () -> Unit
) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.padding(16.dp)
        ) {
            Icon(
                imageVector = Icons.Default.Warning,
                contentDescription = "Ошибка",
                tint = Color.Red,
                modifier = Modifier.size(64.dp)
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = "Ошибка",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = message,
                style = MaterialTheme.typography.bodyMedium,
                textAlign = TextAlign.Center,
                color = Color.Gray
            )
            Spacer(modifier = Modifier.height(24.dp))
            Button(
                onClick = onRetry,
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.primary
                )
            ) {
                Text("Повторить")
            }
        }
    }
}