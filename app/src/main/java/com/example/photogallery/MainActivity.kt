package com.example.photogallery

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Card
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.photogallery.flickr.Photo
import com.example.photogallery.ui.theme.PhotoGalleryTheme
import kotlin.getValue
import androidx.compose.runtime.livedata.observeAsState


class MainActivity : ComponentActivity() {
    private val viewModel by viewModels<PhotoViewModel>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            PhotoGalleryTheme {
                Scaffold(modifier = Modifier.fillMaxSize().background(color = Color.Gray)) { innerPadding ->
                    PhotoGalleryScreen(viewModel,innerPadding)
                }
            }
        }
    }
}

@Composable
fun PhotoGalleryScreen(viewModel: PhotoViewModel, padding: PaddingValues) {

    val photos by viewModel.searchResults.observeAsState(listOf())
    var showFavorites by remember { mutableStateOf(false) }
    LazyVerticalGrid(
        columns = GridCells.Adaptive(120.dp),
        contentPadding = padding
    ) {
        items(photos) { photo ->
            PhotoItem(photo) {
                viewModel.addToFavorites(it)
            }
        }
    }

}

    @Composable
    fun PhotoItem(photo: Photo, onClick: (Photo) -> Unit) {
        Card(
            modifier = Modifier
                .padding(4.dp)
                .fillMaxWidth()
                .clickable { onClick(photo) }
        ) {
            AsyncImage(
                model = photo.url,
                contentDescription = photo.title,
                modifier = Modifier
                    .aspectRatio(1f)
                    .fillMaxWidth()
            )
        }
    }

@Composable
fun FavoriteTitleRow(head1: String, head2: String) {
    Row (
        modifier = Modifier
            .background(MaterialTheme.colorScheme.primary)
            .fillMaxWidth()
            .padding(5.dp)
    ) {
        Text(head1, color = Color.White, modifier = Modifier.weight(0.3f))
        Text(head2, color = Color.White, modifier = Modifier.weight(0.7f))
    }
}
@Composable
fun FavoritePhotoRow(title: String, url: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(5.dp)
    ) {
        Text(title, modifier = Modifier.weight(0.3f))
        Text(url, modifier = Modifier.weight(0.7f))
    }
}
@Composable
fun FavoritesScreen(viewModel: PhotoViewModel, padding: PaddingValues) {
    val favorites by viewModel.allFavorites.observeAsState(emptyList())

    LazyColumn (
        modifier = Modifier
            .fillMaxWidth()
            .padding(padding)
    ) {
        item {
            FavoriteTitleRow(head1 = "Title", head2 = "URL")
        }
        items(favorites) { photo ->
            FavoritePhotoRow(title = photo.title, url = photo.url)
        }
    }
}

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    fun PhotoGalleryTopBar(
        onSearch: (String) -> Unit,
        onStartPolling: () -> Unit,
        onMenuAction1: () -> Unit,
        onMenuAction2: () -> Unit
    ) {
        var expanded by remember { mutableStateOf(false) }
        var isSearching by remember { mutableStateOf(false) }
        var searchText by remember { mutableStateOf("") }
        TopAppBar(title = {
            if (isSearching) {
                TextField(
                    value = searchText,
                    onValueChange = { searchText = it },
                    placeholder = { Text("Search photos") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )
            } else {
                Text("PhotoGallery", fontSize = 20.sp, color = Color.White)
            }
        }, actions = {
            if (!isSearching) {
                IconButton(onClick = { isSearching = true }) {
                    Icon(
                        Icons.Default.Search,
                        contentDescription = "Search",
                        tint = Color.White
                    )
                }
                TextButton(onClick = onStartPolling) {
                    Text(
                        "START POLLING",
                        color = Color.White
                    )
                }
                Box {
                    IconButton(onClick = { expanded = true }) {
                        Icon(
                            Icons.Default.MoreVert,
                            contentDescription = "Menu",
                            tint = Color.White
                        )
                    }
                    DropdownMenu(
                        expanded = expanded,
                        onDismissRequest = { expanded = false }
                    ) {
                        DropdownMenuItem(
                            text = { Text("Action 1") },
                            onClick = {
                                onMenuAction1()
                                expanded = false
                            })
                        DropdownMenuItem(
                            text =
                                { Text("Action 2") },
                            onClick = {
                                onMenuAction2()
                                expanded = false
                            })
                    }
                }
            } else {
                TextButton(onClick = {
                    onSearch(searchText)
                    searchText = ""
                    isSearching = false
                }) {
                    Text(
                        "Done",
                        color = Color.White
                    )
                }
            }
        }, colors = TopAppBarDefaults.topAppBarColors(containerColor = Color(0xFF6200EE)))
    }