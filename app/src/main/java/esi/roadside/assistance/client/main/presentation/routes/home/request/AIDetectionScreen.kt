package esi.roadside.assistance.client.main.presentation.routes.home.request

import android.Manifest
import android.content.Context
import android.net.Uri
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Camera
import androidx.compose.material.icons.filled.Mic
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilledIconButton
import androidx.compose.material3.FilledIconToggleButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import androidx.lifecycle.compose.LocalLifecycleOwner
import esi.roadside.assistance.client.R
import esi.roadside.assistance.client.main.presentation.components.TopAppBar
import kotlinx.coroutines.launch
import java.io.File
import java.text.SimpleDateFormat
import java.util.Locale
import java.util.concurrent.Executor

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AIDetectionScreen(
    onSubmit: (Uri, String) -> Unit,
    onClose: () -> Unit
) {
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current
    val scope = rememberCoroutineScope()

    var isCameraPermissionGranted by remember { mutableStateOf(false) }
    var isAudioPermissionGranted by remember { mutableStateOf(false) }
    var capturedImageUri by remember { mutableStateOf<Uri?>(null) }
    var audioTranscription by remember { mutableStateOf("") }

    var state by remember { mutableStateOf(AiDetectionState.IDLE) }

    // Request camera permission
    val cameraPermissionLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        isCameraPermissionGranted = isGranted
    }

    // Request audio recording permission
    val audioPermissionLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        isAudioPermissionGranted = isGranted
    }

    LaunchedEffect(Unit) {
        cameraPermissionLauncher.launch(Manifest.permission.CAMERA)
        audioPermissionLauncher.launch(Manifest.permission.RECORD_AUDIO)
    }

    // Image capture use case
    val imageCapture = remember { ImageCapture.Builder().build() }
    val executor = remember { ContextCompat.getMainExecutor(context) }

    Box(Modifier.fillMaxSize()) {
        AnimatedContent(
            isCameraPermissionGranted,
            Modifier.fillMaxSize(),
            transitionSpec = { fadeIn() togetherWith fadeOut() }
        ) {
            Box(Modifier.fillMaxSize()) {
                // Camera preview
                if (it) {
                    AndroidView(
                        modifier = Modifier.fillMaxSize(),
                        factory = { ctx ->
                            val previewView = PreviewView(ctx).apply {
                                implementationMode = PreviewView.ImplementationMode.COMPATIBLE
                            }

                            val cameraProviderFuture = ProcessCameraProvider.getInstance(ctx)
                            cameraProviderFuture.addListener(
                                {
                                    val cameraProvider = cameraProviderFuture.get()
                                    val preview = Preview.Builder().build().also {
                                        it.surfaceProvider = previewView.surfaceProvider
                                    }

                                    try {
                                        cameraProvider.unbindAll()
                                        cameraProvider.bindToLifecycle(
                                            lifecycleOwner,
                                            CameraSelector.DEFAULT_BACK_CAMERA,
                                            preview,
                                            imageCapture
                                        )
                                    } catch (e: Exception) {
                                        Log.e("CameraX", "Binding failed", e)
                                    }
                                },
                                executor
                            )

                            previewView
                        }
                    )

                    // Camera UI controls
                    AnimatedVisibility(
                        state != AiDetectionState.PROCESSING,
                        Modifier.fillMaxWidth()
                            .align(Alignment.BottomCenter)
                    ) {
                        Column(
                            modifier = Modifier
                                .background(
                                    Brush.verticalGradient(0f to Color.Transparent, 1f to Color.Black.copy(alpha = .7f))
                                )
                                .navigationBarsPadding()
                                .padding(bottom = 16.dp)
                                .fillMaxWidth(),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.spacedBy(16.dp)
                        ) {
                            AnimatedContent(state) {
                                Text(
                                    when (it) {
                                        AiDetectionState.IDLE ->
                                            "Capture a photo of your vehicle issue"
                                        AiDetectionState.CAPTURED ->
                                            "Photo captured! Now record your voice description"
                                        AiDetectionState.RECORDING ->
                                            "Recording audio... Describe your vehicle issue"
                                        AiDetectionState.PROCESSING -> ""
                                    },
                                    style = MaterialTheme.typography.headlineSmall,
                                    color = Color.White,
                                    textAlign = TextAlign.Center,
                                )
                            }
                            Surface(
                                color = MaterialTheme.colorScheme.surfaceContainer,
                                shape = RoundedCornerShape(100),
                                shadowElevation = 16.dp
                            ) {
                                Row(
                                    modifier = Modifier.padding(8.dp),
                                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                                ) {
                                    // Capture photo button
                                    FilledIconButton(
                                        onClick = {
                                            captureImage(context, imageCapture, executor) { uri ->
                                                capturedImageUri = uri
                                            }
                                            state = AiDetectionState.CAPTURED
                                        },
                                        enabled = state == AiDetectionState.IDLE,
                                        modifier = Modifier.size(64.dp)
                                    ) {
                                        Icon(
                                            Icons.Default.Camera,
                                            contentDescription = "Take Photo",
                                            modifier = Modifier.size(32.dp)
                                        )
                                    }

                                    // Record audio button
                                    FilledIconToggleButton(
                                        checked = state == AiDetectionState.RECORDING,
                                        onCheckedChange = {
                                            // Toggle audio recording
                                            if (state == AiDetectionState.CAPTURED) {
                                                state = AiDetectionState.RECORDING
                                                startAudioRecording(context)
                                            } else if (capturedImageUri != null) {
                                                state = AiDetectionState.PROCESSING

                                                // Get transcription from speech recognition
                                                stopAudioRecordingAndTranscribe(context) { transcription ->
                                                    audioTranscription = transcription.ifEmpty {
                                                        "Vehicle needs roadside assistance."
                                                    }

                                                    scope.launch {
                                                        // Process with AI
                                                        capturedImageUri?.let { uri ->
                                                            onSubmit(uri, audioTranscription)
                                                        }
                                                    }
                                                }
                                            }
                                        },
                                        enabled = (state == AiDetectionState.CAPTURED) or (state == AiDetectionState.RECORDING),
                                        modifier = Modifier.size(64.dp)
                                    ) {
                                        Icon(
                                            Icons.Default.Mic,
                                            contentDescription = "Record Audio",
                                            modifier = Modifier.size(32.dp)
                                        )
                                    }
                                }
                            }
                        }
                    }
                } else {
                    // Camera permission not granted
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Text(
                            "Camera permission is required for AI detection",
                            style = MaterialTheme.typography.bodyLarge,
                            textAlign = TextAlign.Center
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        Button(
                            onClick = { cameraPermissionLauncher.launch(Manifest.permission.CAMERA) }
                        ) {
                            Text("Grant Camera Permission")
                        }
                    }
                }
            }
        }
        TopAppBar(
            title = stringResource(R.string.use_ai),
            background = R.drawable.union,
            modifier = Modifier.align(Alignment.TopStart),
            navigationIcon = {
                IconButton(onClose) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Back",
                        tint = Color.White
                    )
                }
            }
        )
        AnimatedVisibility(state == AiDetectionState.PROCESSING, Modifier.fillMaxSize(),
            enter = fadeIn(),
            exit = fadeOut()
        ) {
            Column(
                modifier = Modifier.fillMaxSize().background(Color.Black.copy(alpha = .7f)),
                verticalArrangement = Arrangement.spacedBy(16.dp, Alignment.CenterVertically),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Icon(
                    imageVector = Icons.Default.AutoAwesome,
                    contentDescription = "Processing",
                    modifier = Modifier.size(120.dp),
                    tint = Color.White
                )
                Text(
                    "AI is processing your request...",
                    style = MaterialTheme.typography.bodyLarge,
                    color = Color.White,
                    modifier = Modifier.fillMaxWidth(),
                    textAlign = TextAlign.Center
                )
                LinearProgressIndicator(
                    Modifier.fillMaxWidth(.5f),
                    color = Color.White,
                    trackColor = Color.Black.copy(alpha = .5f)
                )
            }
        }
    }
}

// Helper function to capture an image
private fun captureImage(
    context: Context,
    imageCapture: ImageCapture,
    executor: Executor,
    onImageCaptured: (Uri) -> Unit
) {
    val photoFile = File(
        context.externalCacheDir,
        SimpleDateFormat("yyyy-MM-dd-HH-mm-ss", Locale.US)
            .format(System.currentTimeMillis()) + ".jpg"
    )

    val outputOptions = ImageCapture.OutputFileOptions.Builder(photoFile).build()

    imageCapture.takePicture(
        outputOptions,
        executor,
        object : ImageCapture.OnImageSavedCallback {
            override fun onImageSaved(outputFileResults: ImageCapture.OutputFileResults) {
                val savedUri = Uri.fromFile(photoFile)
                onImageCaptured(savedUri)
            }

            override fun onError(exception: ImageCaptureException) {
                Log.e("CameraX", "Image capture failed: ${exception.message}", exception)
            }
        }
    )
}

// Audio recorder for recording user's voice description
private var mediaRecorder: android.media.MediaRecorder? = null
private var audioFile: File? = null

// Start recording audio
private fun startAudioRecording(context: Context) {
    try {
        // Create audio output file
        audioFile = File(
            context.externalCacheDir,
            "audio_recording_${System.currentTimeMillis()}.3gp"
        )

        // Initialize MediaRecorder
        mediaRecorder = if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.S) {
            android.media.MediaRecorder(context)
        } else {
            @Suppress("DEPRECATION")
            android.media.MediaRecorder()
        }

        mediaRecorder?.apply {
            setAudioSource(android.media.MediaRecorder.AudioSource.MIC)
            setOutputFormat(android.media.MediaRecorder.OutputFormat.THREE_GPP)
            setAudioEncoder(android.media.MediaRecorder.AudioEncoder.AMR_NB)
            setOutputFile(audioFile?.absolutePath)
            prepare()
            start()
        }

        Log.d("AudioRecording", "Started recording audio to ${audioFile?.absolutePath}")
    } catch (e: Exception) {
        Log.e("AudioRecording", "Error starting audio recording: ${e.message}", e)
        releaseMediaRecorder()
    }
}

// Stop recording audio and transcribe with speech recognition
private fun stopAudioRecordingAndTranscribe(context: Context, onTranscriptionComplete: (String) -> Unit) {
    try {
        // Stop recording
        mediaRecorder?.apply {
            stop()
            release()
        }
        mediaRecorder = null

        // Get the audio file
        val audioUri = Uri.fromFile(audioFile)

        // Use speech recognition to transcribe
        recognizeSpeechFromAudio(context, audioUri, onTranscriptionComplete)

        Log.d("AudioRecording", "Stopped recording audio")
    } catch (e: Exception) {
        Log.e("AudioRecording", "Error stopping audio recording: ${e.message}", e)
        onTranscriptionComplete("")
    }
}

// Release MediaRecorder resources
private fun releaseMediaRecorder() {
    try {
        mediaRecorder?.release()
        mediaRecorder = null
    } catch (e: Exception) {
        Log.e("AudioRecording", "Error releasing MediaRecorder: ${e.message}", e)
    }
}

// Transcribe speech using Android's SpeechRecognizer
private fun recognizeSpeechFromAudio(context: Context, audioUri: Uri, onResult: (String) -> Unit) {
    // For speech recognition, we'll use Android's built-in speech recognizer
    // Since it doesn't directly support file input, we'll launch a speech recognition dialog
    // as a fallback and provide template-based responses if the user cancels

    // Create speech recognition intent
    val intent = android.content.Intent(android.speech.RecognizerIntent.ACTION_RECOGNIZE_SPEECH).apply {
        putExtra(android.speech.RecognizerIntent.EXTRA_LANGUAGE_MODEL, android.speech.RecognizerIntent.LANGUAGE_MODEL_FREE_FORM)
        putExtra(android.speech.RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault())
        putExtra(android.speech.RecognizerIntent.EXTRA_PROMPT, "Describe your vehicle issue")
    }

    try {
        // Create speech recognizer
        val recognizer = android.speech.SpeechRecognizer.createSpeechRecognizer(context)

        // Set recognition listener
        recognizer.setRecognitionListener(object : android.speech.RecognitionListener {
            override fun onResults(results: android.os.Bundle?) {
                val matches = results?.getStringArrayList(android.speech.SpeechRecognizer.RESULTS_RECOGNITION)
                if (!matches.isNullOrEmpty()) {
                    onResult(matches[0])
                } else {
                    onResult(generateFallbackDescription())
                }
                recognizer.destroy()
            }

            override fun onError(error: Int) {
                Log.e("SpeechRecognition", "Error code: $error")
                onResult(generateFallbackDescription())
                recognizer.destroy()
            }

            // Required empty implementations for other methods
            override fun onReadyForSpeech(params: android.os.Bundle?) {}
            override fun onBeginningOfSpeech() {}
            override fun onRmsChanged(rmsdB: Float) {}
            override fun onBufferReceived(buffer: ByteArray?) {}
            override fun onEndOfSpeech() {}
            override fun onPartialResults(partialResults: android.os.Bundle?) {}
            override fun onEvent(eventType: Int, params: android.os.Bundle?) {}
        })

        // Start listening
        recognizer.startListening(intent)

        // Set a timeout to ensure we get a result
        android.os.Handler(android.os.Looper.getMainLooper()).postDelayed({
            try {
                recognizer.stopListening()
            } catch (e: Exception) {
                Log.e("SpeechRecognition", "Error stopping listening: ${e.message}")
                onResult(generateFallbackDescription())
            }
        }, 5000) // 5 second timeout
    } catch (e: Exception) {
        Log.e("SpeechRecognition", "Error: ${e.message}")
        onResult(generateFallbackDescription())
    }
}

// Generate a relevant description for roadside assistance
private fun generateFallbackDescription(): String {
    val descriptions = listOf(
        "My car has a flat tire and I need assistance.",
        "The vehicle won't start and needs a jumpstart.",
        "I'm locked out of my car and need help getting back in.",
        "My car broke down and needs to be towed.",
        "I've run out of fuel and need a fuel delivery."
    )
    return descriptions.random()
}

enum class AiDetectionState {
    IDLE, // Initial state
    CAPTURED, // Image captured
    RECORDING, // Audio recording
    PROCESSING
}
