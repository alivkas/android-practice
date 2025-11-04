package com.example.android_practice.presentation.ui.screens.profile

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import org.koin.androidx.compose.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditProfileScreen(
    onBackClick: () -> Unit,
    onSaveClick: () -> Unit,
    viewModel: EditProfileViewModel = koinViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.loadProfile()
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Редактирование профиля") },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Назад")
                    }
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    viewModel.saveProfile()
                    onSaveClick()
                },
                modifier = Modifier.padding(16.dp)
            ) {
                Icon(Icons.Default.Check, contentDescription = "Сохранить")
            }
        }
    ) { paddingValues ->
        EditProfileContent(
            uiState = uiState,
            onAvatarClick = viewModel::selectAvatar,
            onFullNameChange = viewModel::updateFullName,
            onPositionChange = viewModel::updatePosition,
            onEmailChange = viewModel::updateEmail,
            onResumeUrlChange = viewModel::updateResumeUrl,
            modifier = Modifier.padding(paddingValues)
        )
    }
}

@Composable
fun EditProfileContent(
    uiState: EditProfileUiState,
    onAvatarClick: () -> Unit,
    onFullNameChange: (String) -> Unit,
    onPositionChange: (String) -> Unit,
    onEmailChange: (String) -> Unit,
    onResumeUrlChange: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Box(
            modifier = Modifier
                .size(120.dp)
                .align(Alignment.CenterHorizontally)
        ) {
            if (uiState.profile.avatarUri.isNotEmpty()) {
                Image(
                    painter = rememberAsyncImagePainter(
                        ImageRequest.Builder(LocalContext.current)
                            .data(uiState.profile.avatarUri)
                            .build()
                    ),
                    contentDescription = "Аватар",
                    modifier = Modifier
                        .fillMaxSize()
                        .clip(CircleShape)
                        .clickable { onAvatarClick() },
                    contentScale = ContentScale.Crop
                )
            } else {
                Icon(
                    imageVector = Icons.Default.Person,
                    contentDescription = "Аватар",
                    modifier = Modifier
                        .fillMaxSize()
                        .clickable { onAvatarClick() },
                    tint = MaterialTheme.colorScheme.primary
                )
            }
        }
        OutlinedTextField(
            value = uiState.profile.fullName,
            onValueChange = onFullNameChange,
            label = { Text("ФИО") },
            modifier = Modifier.fillMaxWidth()
        )

        OutlinedTextField(
            value = uiState.profile.position,
            onValueChange = onPositionChange,
            label = { Text("Должность") },
            modifier = Modifier.fillMaxWidth()
        )

        OutlinedTextField(
            value = uiState.profile.email,
            onValueChange = onEmailChange,
            label = { Text("Email") },
            modifier = Modifier.fillMaxWidth()
        )

        OutlinedTextField(
            value = uiState.profile.resumeUrl,
            onValueChange = onResumeUrlChange,
            label = { Text("Ссылка на резюме (PDF)") },
            placeholder = { Text("https://example.com/resume.pdf") },
            modifier = Modifier.fillMaxWidth()
        )
    }
}