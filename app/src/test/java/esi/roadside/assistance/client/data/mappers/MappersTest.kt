package esi.roadside.assistance.client.data.mappers

import com.google.common.truth.Truth.assertThat
import esi.roadside.assistance.client.main.domain.models.LocationModel
import org.junit.Test

class MappersTest {
    @Test
    fun `test string to location`() {
        val location = "30.0444,31.2357"
        val locationModel = LocationModel.fromString(location)
        assertThat(locationModel.latitude).isEqualTo(30.0444)
        assertThat(locationModel.longitude).isEqualTo(31.2357)
    }

    @Test
    fun `test location to string`() {
        val locationModel = LocationModel(30.0444, 31.2357)
        val location = locationModel.toString()
        assertThat(location).isEqualTo("30.0444,31.2357")
    }

    @Test
    fun `test if wrong location string throws exception`() {
        val location = "30.0444,31.2357,31.2357"
        try {
            LocationModel.fromString(location)
        } catch (e: Exception) {
            assertThat(e).isInstanceOf(IllegalArgumentException::class.java)
        }
    }
}