package com.example.android_practice.presentation.ui.screens.profile

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import com.example.android_practice.data.manager.ImageManager
import org.koin.androidx.compose.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditProfileScreen(
    onBackClick: () -> Unit,
    onSaveClick: () -> Unit,
    viewModel: EditProfileViewModel = koinViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val showImagePickerDialog by viewModel.showImagePickerDialog.collectAsState()
    val context = LocalContext.current

    var requestGalleryPermission by remember { mutableStateOf(false) }
    var requestCameraPermission by remember { mutableStateOf(false) }

    val galleryLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == android.app.Activity.RESULT_OK) {
            result.data?.data?.let { uri ->
                viewModel.onImageSelected(uri)
            }
        }
    }

    val cameraLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == android.app.Activity.RESULT_OK || result.data != null) {
            viewModel.cameraImageUri?.let { uri ->
                viewModel.onImageSelected(uri)
            }
        }
    }

    val cameraPermissionLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) {
            try {
                cameraLauncher.launch(viewModel.getCameraIntent())
            } catch (e: Exception) {
                Toast.makeText(context, "Ошибка запуска камеры", Toast.LENGTH_SHORT).show()
                viewModel.hideImagePickerDialog()
            }
        } else {
            Toast.makeText(context, "Разрешение на камеру необходимо", Toast.LENGTH_LONG).show()
            viewModel.hideImagePickerDialog()
        }
    }

    val galleryPermissionLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) {
            galleryLauncher.launch(viewModel.getGalleryIntent())
        } else {
            Toast.makeText(context, "Разрешение на доступ к галерее необходимо", Toast.LENGTH_LONG).show()
            viewModel.hideImagePickerDialog()
        }
    }

    if (requestGalleryPermission) {
        LaunchedEffect(Unit) {
            val storagePermission = getStoragePermission()
            if (hasPermission(context, storagePermission)) {
                galleryLauncher.launch(viewModel.getGalleryIntent())
            } else {
                galleryPermissionLauncher.launch(storagePermission)
            }
            requestGalleryPermission = false
        }
    }

    if (requestCameraPermission) {
        LaunchedEffect(Unit) {
            if (hasPermission(context, Manifest.permission.CAMERA)) {
                try {
                    cameraLauncher.launch(viewModel.getCameraIntent())
                } catch (e: Exception) {
                    Toast.makeText(context, "Ошибка запуска камеры: ${e.message}", Toast.LENGTH_SHORT).show()
                    viewModel.hideImagePickerDialog()
                }
            } else {
                cameraPermissionLauncher.launch(Manifest.permission.CAMERA)
            }
            requestCameraPermission = false
        }
    }

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

        if (showImagePickerDialog) {
            ImagePickerDialog(
                onDismiss = viewModel::hideImagePickerDialog,
                onGallerySelected = {
                    val permission = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                        Manifest.permission.READ_MEDIA_IMAGES
                    } else {
                        Manifest.permission.READ_EXTERNAL_STORAGE
                    }
                    if (ContextCompat.checkSelfPermission(context, permission) == PackageManager.PERMISSION_GRANTED) {
                        galleryLauncher.launch(viewModel.getGalleryIntent())
                    } else {
                        galleryPermissionLauncher.launch(permission)
                    }
                },
                onCameraSelected = {
                    if (ContextCompat.checkSelfPermission(context, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
                        try {
                            cameraLauncher.launch(viewModel.getCameraIntent())
                        } catch (e: Exception) {
                            Toast.makeText(context, "Ошибка запуска камеры", Toast.LENGTH_SHORT).show()
                            viewModel.hideImagePickerDialog()
                        }
                    } else {
                        cameraPermissionLauncher.launch(Manifest.permission.CAMERA)
                    }
                },
                context = context
            )
        }
    }
}

private fun getStoragePermission(): String {
    return if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.TIRAMISU) {
        Manifest.permission.READ_MEDIA_IMAGES
    } else {
        Manifest.permission.READ_EXTERNAL_STORAGE
    }
}

private fun hasPermission(context: Context, permission: String): Boolean {
    return ContextCompat.checkSelfPermission(context, permission) == PackageManager.PERMISSION_GRANTED
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

        Text(
            text = "Нажмите на аватар для изменения",
            style = MaterialTheme.typography.bodySmall,
            modifier = Modifier.align(Alignment.CenterHorizontally)
        )

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

@Composable
fun ImagePickerDialog(
    onDismiss: () -> Unit,
    onGallerySelected: () -> Unit,
    onCameraSelected: () -> Unit,
    context: Context = LocalContext.current
) {
    val imageManager = remember { ImageManager(context) }
    val isCameraAvailable = remember { imageManager.isCameraAvailable() }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Выберите источник") },
        text = {
            Column {
                Text("Откуда хотите выбрать фото?")
                if (!isCameraAvailable) {
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        "Камера недоступна на этом устройстве",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.error
                    )
                }
            }
        },
        confirmButton = {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                Button(
                    onClick = onGallerySelected,
                    modifier = Modifier.weight(1f)
                ) {
                    Text("Галерея")
                }

                if (isCameraAvailable) {
                    Spacer(modifier = Modifier.width(16.dp))
                    Button(
                        onClick = onCameraSelected,
                        modifier = Modifier.weight(1f)
                    ) {
                        Text("Камера")
                    }
                }
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Отмена")
            }
        }
    )
}