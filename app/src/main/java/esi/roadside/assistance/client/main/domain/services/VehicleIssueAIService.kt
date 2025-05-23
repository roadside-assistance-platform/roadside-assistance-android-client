package esi.roadside.assistance.client.main.domain.services

import android.content.Context
import android.graphics.Bitmap
import android.net.Uri
import android.util.Log
import androidx.annotation.OptIn
import androidx.camera.core.ExperimentalGetImage
import androidx.camera.core.ImageProxy
import com.google.ai.client.generativeai.GenerativeModel
import com.google.ai.client.generativeai.type.BlockThreshold
import com.google.ai.client.generativeai.type.Content
import com.google.ai.client.generativeai.type.HarmCategory
import com.google.ai.client.generativeai.type.SafetySetting
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.label.ImageLabeler
import com.google.mlkit.vision.label.ImageLabeling
import com.google.mlkit.vision.label.defaults.ImageLabelerOptions
import esi.roadside.assistance.client.BuildConfig
import esi.roadside.assistance.client.main.domain.Categories
import esi.roadside.assistance.client.settings.util.toBitmap
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlinx.coroutines.withContext
import kotlin.coroutines.resume

/**
 * Service for AI-powered detection of vehicle issues using camera and microphone
 */
class VehicleIssueAIService(
    internal val context: Context
) {
    private val imageLabeler: ImageLabeler = ImageLabeling.getClient(
        ImageLabelerOptions.Builder()
            .setConfidenceThreshold(0.7f)
            .build()
    )

    /**
     * Detect vehicle issue category from an image
     */
    @OptIn(ExperimentalGetImage::class)
    suspend fun detectCategoryFromImage(imageProxy: ImageProxy): Categories? =
        withContext(Dispatchers.IO) {
            return@withContext suspendCancellableCoroutine { continuation ->
                val mediaImage = imageProxy.image ?: run {
                    continuation.resume(null)
                    return@suspendCancellableCoroutine
                }

                val image =
                    InputImage.fromMediaImage(mediaImage, imageProxy.imageInfo.rotationDegrees)

                imageLabeler.process(image)
                    .addOnSuccessListener { labels ->
                        imageProxy.close()

                        // Map image labels to service categories
                        val category = when {
                            labels.any {
                                it.text.contains("tow", ignoreCase = true) ||
                                        it.text.contains("broken down", ignoreCase = true)
                            } ->
                                Categories.TOWING

                            labels.any {
                                it.text.contains("tire", ignoreCase = true) ||
                                        it.text.contains("wheel", ignoreCase = true) ||
                                        it.text.contains("flat", ignoreCase = true)
                            } ->
                                Categories.FLAT_TIRE

                            labels.any {
                                it.text.contains("fuel", ignoreCase = true) ||
                                        it.text.contains("gas", ignoreCase = true) ||
                                        it.text.contains("petrol", ignoreCase = true)
                            } ->
                                Categories.FUEL_DELIVERY

                            labels.any {
                                it.text.contains("lock", ignoreCase = true) ||
                                        it.text.contains("key", ignoreCase = true) ||
                                        it.text.contains("door", ignoreCase = true)
                            } ->
                                Categories.LOCKOUT

                            labels.any {
                                it.text.contains("crash", ignoreCase = true) ||
                                        it.text.contains("accident", ignoreCase = true) ||
                                        it.text.contains("emergency", ignoreCase = true)
                            } ->
                                Categories.EMERGENCY

                            else -> Categories.OTHER
                        }

                        // Log detected labels for debugging
                        Log.d(
                            "AIDetection",
                            "Detected labels: ${labels.joinToString { "${it.text} (${it.confidence})" }}"
                        )
                        Log.d("AIDetection", "Selected category: $category")

                        continuation.resume(category)
                    }
                    .addOnFailureListener {
                        imageProxy.close()
                        continuation.resume(null)
                    }
            }
        }

    /**
     * Detect vehicle issue category from an image URI
     * This is an alternative to detectCategoryFromImage that works with URI instead of ImageProxy
     */
    suspend fun detectCategoryFromImageUri(imageUri: Uri): Categories? =
        withContext(Dispatchers.IO) {
            try {
                val bitmap = imageUri.toBitmap(context) ?: return@withContext null

                // Create an InputImage from the bitmap for ML Kit processing
                val image = InputImage.fromBitmap(bitmap, 0)

                // Use ML Kit's image labeler to identify objects in the image
                val labels =
                    suspendCancellableCoroutine<List<com.google.mlkit.vision.label.ImageLabel>> { continuation ->
                        imageLabeler.process(image)
                            .addOnSuccessListener { labels ->
                                continuation.resume(labels)
                            }
                            .addOnFailureListener { e ->
                                Log.e("AIDetection", "Image labeling failed: ${e.message}", e)
                                continuation.resume(emptyList())
                            }
                    }

                // Map detected labels to vehicle issue categories
                val category = when {
                    // Check for flat tire related terms
                    labels.any {
                        it.text.contains("tire", ignoreCase = true) ||
                                it.text.contains("wheel", ignoreCase = true) ||
                                it.text.contains("flat", ignoreCase = true)
                    } -> Categories.FLAT_TIRE

                    // Check for towing related terms
                    labels.any {
                        it.text.contains("tow", ignoreCase = true) ||
                                it.text.contains("truck", ignoreCase = true) ||
                                it.text.contains("broken down", ignoreCase = true) ||
                                it.text.contains("accident", ignoreCase = true) ||
                                it.text.contains("collision", ignoreCase = true)
                    } -> Categories.TOWING

                    // Check for fuel related terms
                    labels.any {
                        it.text.contains("fuel", ignoreCase = true) ||
                                it.text.contains("gas", ignoreCase = true) ||
                                it.text.contains("petrol", ignoreCase = true) ||
                                it.text.contains("pump", ignoreCase = true)
                    } -> Categories.FUEL_DELIVERY

                    // Check for lockout related terms
                    labels.any {
                        it.text.contains("lock", ignoreCase = true) ||
                                it.text.contains("key", ignoreCase = true) ||
                                it.text.contains("door", ignoreCase = true)
                    } -> Categories.LOCKOUT

                    // Check for emergency related terms
                    labels.any {
                        it.text.contains("emergency", ignoreCase = true) ||
                                it.text.contains("urgent", ignoreCase = true) ||
                                it.text.contains("fire", ignoreCase = true) ||
                                it.text.contains("smoke", ignoreCase = true) ||
                                it.text.contains("danger", ignoreCase = true)
                    } -> Categories.EMERGENCY

                    // Default to OTHER if we can't confidently determine the category
                    else -> Categories.OTHER
                }

                // Log detected labels for debugging
                Log.d(
                    "AIDetection",
                    "Detected labels: ${labels.joinToString { "${it.text} (${it.confidence})" }}"
                )
                Log.d("AIDetection", "Selected category: $category")

                return@withContext category
            } catch (e: Exception) {
                Log.e("AIDetection", "Error analyzing image from URI: ${e.message}", e)
                return@withContext null
            }
        }

    /**
     * Generate description for vehicle issue using ML Kit analysis and Templates
     * This avoids using Gemini API to prevent dependency issues
     */
    suspend fun generateDescriptionFromImageAndAudio(
        imageUri: Uri,
        audioTranscription: String
    ): String = withContext(Dispatchers.IO) {
        try {
            // Try to use Gemini AI for the description, with robust fallbacks
            val generatedDescription = try {
                val bitmap = imageUri.toBitmap(context)
                if (bitmap != null) {
                    generateDescriptionWithGemini(bitmap, audioTranscription)
                } else null
            } catch (e: Exception) {
                Log.e("AIDetection", "Gemini generation failed: ${e.message}", e)
                null
            }

            // Return the AI-generated description or fall back to templates
            if (!generatedDescription.isNullOrBlank()) {
                return@withContext generatedDescription
            }

            // If Gemini fails, detect the category with ML Kit and use templates
            val detectedCategory = detectCategoryFromImageUri(imageUri)
            val templateDescription =
                generateTemplateDescription(detectedCategory, audioTranscription)

            return@withContext templateDescription
        } catch (e: Exception) {
            // Ultimate fallback if everything else fails
            Log.e("AIDetection", "Description generation failed: ${e.message}", e)
            return@withContext audioTranscription.takeIf { it.isNotBlank() }
                ?: "Roadside assistance needed."
        }
    }

    /**
     * Attempt to generate a description using Gemini AI
     * This function is isolated to contain any exceptions
     */
    private suspend fun generateDescriptionWithGemini(
        bitmap: Bitmap,
        audioTranscription: String
    ): String? {
        return try {
            val generativeModel = GenerativeModel(
                modelName = "gemini-2.5-flash",
                apiKey = BuildConfig.GEMINI,
                safetySettings = listOf(
                    SafetySetting(HarmCategory.HARASSMENT, BlockThreshold.MEDIUM_AND_ABOVE),
                    SafetySetting(HarmCategory.HATE_SPEECH, BlockThreshold.MEDIUM_AND_ABOVE),
                    SafetySetting(HarmCategory.SEXUALLY_EXPLICIT, BlockThreshold.MEDIUM_AND_ABOVE),
                    SafetySetting(HarmCategory.DANGEROUS_CONTENT, BlockThreshold.MEDIUM_AND_ABOVE),
                )
            )

            val prompt = """
                I need a concise description of a roadside assistance issue.
                
                Based on the image and the user's audio description: "$audioTranscription"
                
                Please describe the vehicle issue in 2-3 sentences, focusing on:
                1. What type of vehicle problem it appears to be
                2. Any visible damage or issues
                3. Possible assistance needed
                
                Keep the description professional and focused on the roadside assistance needs.
            """.trimIndent()

            val response = generativeModel.generateContent(
                Content.Builder().text(prompt).build(),
                Content.Builder().image(bitmap).build()
            )

            response.text
        } catch (e: Exception) {
            Log.e("AIDetection", "Gemini API error: ${e.message}", e)
            null
        }
    }

    /**
     * Generate a template-based description based on the detected category
     */
    private fun generateTemplateDescription(
        category: Categories?,
        userDescription: String
    ): String {
        // Use the user's audio description as a base if available
        val baseDescription = if (userDescription.isNotBlank()) {
            "$userDescription "
        } else ""

        // Add template text based on the detected category
        return when (category) {
            Categories.TOWING ->
                "${baseDescription}Vehicle appears to require towing service. The vehicle cannot be driven safely and needs to be transported to a service location."

            Categories.FLAT_TIRE ->
                "${baseDescription}Vehicle has a flat tire that needs replacement. Assistance with changing to the spare tire or providing a temporary tire repair is required."

            Categories.FUEL_DELIVERY ->
                "${baseDescription}Vehicle has run out of fuel and requires fuel delivery service to continue the journey."

            Categories.LOCKOUT ->
                "${baseDescription}Driver is locked out of the vehicle and requires assistance with regaining access to the vehicle."

            Categories.EMERGENCY ->
                "${baseDescription}Emergency assistance is required. The situation may involve an accident or other urgent vehicle issue that needs immediate attention."

            Categories.OTHER, null ->
                if (userDescription.isNotBlank()) {
                    userDescription
                } else {
                    "Roadside assistance needed. Please send help to assess and resolve the vehicle issue."
                }
        }
    }
}
