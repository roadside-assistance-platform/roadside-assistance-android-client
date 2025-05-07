package esi.roadside.assistance.client.core.data

import android.content.Context
import android.content.SharedPreferences
import androidx.core.content.edit
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import esi.roadside.assistance.client.core.domain.model.ClientModel
import esi.roadside.assistance.client.main.domain.models.ServiceModel
import java.time.OffsetDateTime
import java.time.ZoneId

class AccountStorage(context: Context) {

    private companion object {
        private const val PREF_NAME = "client_preferences"
        private const val KEY_CLIENT = "client_data"
        private const val KEY_CLIENT_ID = "client_id"
        private const val KEY_CLIENT_FULL_NAME = "client_full_name"
        private const val KEY_CLIENT_EMAIL = "client_email"
        private const val KEY_CLIENT_PASSWORD = "client_password"
        private const val KEY_CLIENT_PHONE = "client_phone"
        private const val KEY_CLIENT_PHOTO = "client_photo"
        private const val KEY_CLIENT_SERVICES = "client_services"
        private const val KEY_CLIENT_CREATED_AT = "client_created_at"
        private const val KEY_CLIENT_UPDATED_AT = "client_updated_at"
    }

    private val preferences: SharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
    private val gson = Gson()

    /**
     * Saves a complete ClientModel object to SharedPreferences
     */
    fun saveClientModel(client: ClientModel) {
        preferences.edit() {
            val clientJson = gson.toJson(client)
            putString(KEY_CLIENT, clientJson)
        }
    }

    /**
     * Retrieves the complete ClientModel object from SharedPreferences
     * @return ClientModel object or null if not found
     */
    fun get(): ClientModel? {
        val clientJson = preferences.getString(KEY_CLIENT, null) ?: return null
        return gson.fromJson(clientJson, ClientModel::class.java)
    }

    /**
     * Saves individual client properties to SharedPreferences
     */
    fun saveClientModelProperties(client: ClientModel) {
        preferences.edit() {
            putString(KEY_CLIENT_ID, client.id)
            putString(KEY_CLIENT_FULL_NAME, client.fullName)
            putString(KEY_CLIENT_EMAIL, client.email)
            putString(KEY_CLIENT_PASSWORD, client.password)
            putString(KEY_CLIENT_PHONE, client.phone)
            putString(KEY_CLIENT_PHOTO, client.photo)
            putString(KEY_CLIENT_CREATED_AT, client.createdAt.toOffsetDateTime().toString())
            // Convert services list to JSON
            val servicesJson = gson.toJson(client.services)
            putString(KEY_CLIENT_SERVICES, servicesJson)
            putString(KEY_CLIENT_UPDATED_AT, client.updatedAt.toOffsetDateTime().toString())
        }
    }

    /**
     * Builds and returns a ClientModelModel object from individual properties stored in SharedPreferences
     */
    fun getClientModelModelFromProperties(): ClientModel {
        val id = preferences.getString(KEY_CLIENT_ID, "") ?: ""
        val fullName = preferences.getString(KEY_CLIENT_FULL_NAME, "") ?: ""
        val email = preferences.getString(KEY_CLIENT_EMAIL, "") ?: ""
        val password = preferences.getString(KEY_CLIENT_PASSWORD, "") ?: ""
        val phone = preferences.getString(KEY_CLIENT_PHONE, "") ?: ""
        val photo = preferences.getString(KEY_CLIENT_PHOTO, null)
        val createdAt = preferences.getString(KEY_CLIENT_CREATED_AT, "")
        val updatedAt = preferences.getString(KEY_CLIENT_UPDATED_AT, "") ?: ""

        // Get services list from JSON
        val servicesJson = preferences.getString(KEY_CLIENT_SERVICES, null)
        val services = if (servicesJson != null) {
            val type = object : TypeToken<List<ServiceModel>>() {}.type
            gson.fromJson<List<ServiceModel>>(servicesJson, type)
        } else {
            emptyList()
        }

        return ClientModel(
            id = id,
            fullName = fullName,
            email = email,
            password = password,
            phone = phone,
            photo = photo.toString(),
            services = services,
            createdAt = try {
                OffsetDateTime.parse(createdAt)
            } catch(_: Exception) {
                OffsetDateTime.now()
            }.toLocalDateTime().atZone(ZoneId.systemDefault()),
            updatedAt = try {
                OffsetDateTime.parse(updatedAt)
            } catch(_: Exception) {
                OffsetDateTime.now()
            }.toLocalDateTime().atZone(ZoneId.systemDefault())
        )
    }

    /**
     * Sets or updates individual client properties
     */
    fun setClientModelId(id: String) {
        preferences.edit() { putString(KEY_CLIENT_ID, id) }
    }

    fun setClientModelFullName(fullName: String) {
        preferences.edit() { putString(KEY_CLIENT_FULL_NAME, fullName) }
    }

    fun setClientModelEmail(email: String) {
        preferences.edit() { putString(KEY_CLIENT_EMAIL, email) }
    }

    fun setClientModelPassword(password: String) {
        preferences.edit() { putString(KEY_CLIENT_PASSWORD, password) }
    }

    fun setClientModelPhone(phone: String) {
        preferences.edit() { putString(KEY_CLIENT_PHONE, phone) }
    }

    fun setClientModelPhoto(photo: String?) {
        preferences.edit() { putString(KEY_CLIENT_PHOTO, photo) }
    }

    fun setClientModelServices(services: List<ServiceModel>) {
        val servicesJson = gson.toJson(services)
        preferences.edit() { putString(KEY_CLIENT_SERVICES, servicesJson)}
    }

    fun setClientModelCreatedAt(createdAt: String) {
        preferences.edit() { putString(KEY_CLIENT_CREATED_AT, createdAt) }
    }

    fun setClientModelUpdatedAt(updatedAt: String) {
        preferences.edit() { putString(KEY_CLIENT_UPDATED_AT, updatedAt) }
    }

    /**
     * Gets individual client properties
     */
    fun getClientModelId(): String = preferences.getString(KEY_CLIENT_ID, "") ?: ""

    fun getClientModelFullName(): String = preferences.getString(KEY_CLIENT_FULL_NAME, "") ?: ""

    fun getClientModelEmail(): String = preferences.getString(KEY_CLIENT_EMAIL, "") ?: ""

    fun getClientModelPassword(): String = preferences.getString(KEY_CLIENT_PASSWORD, "") ?: ""

    fun getClientModelPhone(): String = preferences.getString(KEY_CLIENT_PHONE, "") ?: ""

    fun getClientModelPhoto(): String? = preferences.getString(KEY_CLIENT_PHOTO, null)

    fun getClientModelServices(): List<ServiceModel> {
        val servicesJson = preferences.getString(KEY_CLIENT_SERVICES, null)
        return if (servicesJson != null) {
            val type = object : TypeToken<List<ServiceModel>>() {}.type
            gson.fromJson(servicesJson, type)
        } else {
            emptyList()
        }
    }

    fun getClientModelCreatedAt(): String = preferences.getString(KEY_CLIENT_CREATED_AT, "") ?: ""

    fun getClientModelUpdatedAt(): String = preferences.getString(KEY_CLIENT_UPDATED_AT, "") ?: ""

    /**
     * Clears all client data from SharedPreferences
     * call it when logout or deleting account
     */

    fun clearClientModelData() {
        preferences.edit() { clear() }
    }
}