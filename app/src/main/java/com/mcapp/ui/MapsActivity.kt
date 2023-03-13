import android.util.Log
import androidx.compose.runtime.*
import androidx.compose.ui.viewinterop.AndroidView
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.MapView
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.mcapp.util.rememberMapViewWithLifecycle
import com.google.maps.android.ktx.awaitMap
import kotlinx.coroutines.launch
import java.util.*

@Composable
fun ReminderLocation(onBack: (LatLng) -> Unit) {
    val mapView: MapView = rememberMapViewWithLifecycle()
    val coroutineScope = rememberCoroutineScope()
    val locationSelected = remember { mutableStateOf(false) }

    val selectedLocation = remember { mutableStateOf(LatLng(0.0, 0.0)) }

    AndroidView({mapView}) {
        coroutineScope.launch {
            val map = mapView.awaitMap()
            map.uiSettings.isZoomControlsEnabled = true
            map.uiSettings.isScrollGesturesEnabled = true
            val location = LatLng(65.06, 25.47)
            map.moveCamera(
                CameraUpdateFactory.newLatLngZoom(
                    LatLng(location.latitude, location.longitude),
                    10f
                )
            )
            setMapLongClick(map, selectedLocation, locationSelected)
        }
    }

    if (locationSelected.value) {
        onBack(selectedLocation.value)
    }
}

private fun setMapLongClick(
    map: GoogleMap,
    selectedLocation: MutableState<LatLng>,
    locationSelected: MutableState<Boolean>
) {
    map.setOnMapLongClickListener { lat_lng ->
        val snippet = String.format(
            Locale.getDefault(),
            "Lat: %1$.2f, Lng: %2$.2f",
            lat_lng.latitude,
            lat_lng.longitude
        )

        map.addMarker(
            MarkerOptions().position(lat_lng).title("Reminder location").snippet(snippet)
        )

        // Set the selected location and update the flag
        Log.d("LOCATION", "$lat_lng")
        selectedLocation.value = lat_lng
        locationSelected.value = true
    }
}
