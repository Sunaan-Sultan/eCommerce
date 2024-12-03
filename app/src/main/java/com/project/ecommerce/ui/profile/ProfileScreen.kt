package com.project.ecommerce.ui.profile

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.io.InputStream

@Composable
fun ProfileScreen() {
    val context = LocalContext.current
    var imageUri by remember { mutableStateOf<Uri?>(null) }
    val bitmapImage = remember { mutableStateOf<Bitmap?>(null) }
    var showDialog by remember { mutableStateOf(false) }
    var profileImagePath by remember { mutableStateOf<String?>(null) }

    // Load the saved profile image if it exists
    LaunchedEffect(Unit) {
        profileImagePath = loadProfileImage(context)
        if (profileImagePath != null) {
            bitmapImage.value = BitmapFactory.decodeFile(profileImagePath)
        }
    }

    val galleryLauncher =
        rememberLauncherForActivityResult(contract = ActivityResultContracts.GetContent()) { uri ->
            imageUri = uri
            bitmapImage.value = null
            uri?.let {
                // Convert Uri to Bitmap and save it
                val bitmap = getBitmapFromUri(context, it)
                bitmap?.let { saveProfileImage(context, it) }
            }
        }


    val cameraLauncher =
        rememberLauncherForActivityResult(
            contract = ActivityResultContracts.TakePicturePreview(),
            onResult = { bitmap ->
                bitmapImage.value = bitmap
                imageUri = null
                bitmap?.let { saveProfileImage(context, it) }
            })

    val camPermission =
        rememberLauncherForActivityResult(
            contract = ActivityResultContracts.RequestPermission()
        ) { isGranted: Boolean ->
            if (isGranted) {
                cameraLauncher.launch(null)
            }
        }

    val storagePermission =
        rememberLauncherForActivityResult(
            contract = ActivityResultContracts.RequestPermission()
        ) { isGranted: Boolean ->
            if (isGranted) {
                galleryLauncher.launch("image/*")
            }
        }

    Scaffold(modifier = Modifier.fillMaxSize()) { paddingValues ->

        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(if (isSystemInDarkTheme()) Color.Black else Color.White)
                .padding(paddingValues),
            contentAlignment = Alignment.TopCenter
        ) {
            Column(
                modifier = Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Top
            ) {
                Spacer(modifier = Modifier.height(16.dp))
                Box(
                    modifier = Modifier
                        .size(150.dp)
                        .clip(CircleShape)
                        .border(2.dp, Color.Gray, CircleShape)
                        .clickable { showDialog = true },
                    contentAlignment = Alignment.Center
                ) {
                    if (bitmapImage.value != null || imageUri != null) {
                        if (imageUri != null) {
                            AsyncImage(
                                model = ImageRequest.Builder(context)
                                    .data(imageUri)
                                    .build(),
                                contentDescription = "Profile Image",
                                contentScale = ContentScale.Crop,
                                modifier = Modifier.fillMaxSize()
                            )
                        } else if (bitmapImage.value != null) {
                            Image(
                                bitmap = bitmapImage.value!!.asImageBitmap(),
                                contentDescription = "Profile Image",
                                contentScale = ContentScale.Crop,
                                modifier = Modifier.fillMaxSize()
                            )
                        }
                    } else {
                        Icon(
                            imageVector = Icons.Default.Person,
                            contentDescription = "Placeholder Profile Image",
                            modifier = Modifier
                                .size(72.dp),
                            tint = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
                Spacer(modifier = Modifier.height(8.dp))
                Text(text = "Guest", style = MaterialTheme.typography.headlineLarge)
            }

            if (showDialog) {
                AlertDialog(
                    onDismissRequest = { showDialog = false },
                    title = { Text(text = "Upload Profile Picture") },
                    text = { Text(text = "Choose an option to upload your profile picture.") },
                    confirmButton = {
                        TextButton(onClick = {
                            camPermission.launch(android.Manifest.permission.CAMERA)
                            showDialog = false
                        }) {
                            Text(text = "Open Camera")
                        }
                    },
                    dismissButton = {
                        TextButton(onClick = {
                            storagePermission.launch(android.Manifest.permission.READ_EXTERNAL_STORAGE)
                            showDialog = false
                        }) {
                            Text(text = "Open Gallery")
                        }
                    }
                )
            }
        }
    }
}

fun getBitmapFromUri(context: Context, uri: Uri): Bitmap? {
    return try {
        // Open an input stream for the Uri and decode it into a Bitmap
        val inputStream: InputStream? = context.contentResolver.openInputStream(uri)
        BitmapFactory.decodeStream(inputStream)
    } catch (e: Exception) {
        e.printStackTrace()
        null
    }
}

fun saveProfileImage(context: Context, bitmap: Bitmap) {
    try {
        val file = File(context.filesDir, "profile_image.jpg")
        val outputStream = FileOutputStream(file)
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream)
        outputStream.flush()
        outputStream.close()
    } catch (e: IOException) {
        Log.e("ProfileScreen", "Error saving profile image", e)
    }
}

fun loadProfileImage(context: Context): String? {
    val file = File(context.filesDir, "profile_image.jpg")
    return if (file.exists()) file.absolutePath else null
}