package ru.vsu.ofood.ui.info

import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.viewinterop.AndroidView
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.MapView
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.ktx.addMarker
import com.google.maps.android.ktx.awaitMap
import kotlinx.coroutines.launch
import ru.vsu.ofood.entities.Coordinate
import ru.vsu.ofood.ui.NavigationOperation
import ru.vsu.ofood.ui.utils.rememberMapViewWithLifecycle
import ru.vsu.ofood.ui.utils.setZoom

@Composable
fun CityMapView(
    coordinate: Coordinate,
    navigate: (NavigationOperation) -> Unit
) {
    val mapView = rememberMapViewWithLifecycle()
    MapViewContent(
        map = mapView,
        latitude = coordinate.latitude.toString(),
        longitude = coordinate.longitude.toString()
    ) { navigate(NavigationOperation.Back) }
}

@Composable
fun MapViewContent(
    map: MapView,
    latitude: String,
    longitude: String,
    navigateClick : () -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Контакты") },
                navigationIcon = {
                    IconButton(onClick = { navigateClick() }) {
                        Icon(Icons.Filled.ArrowBack, contentDescription = null)
                    }
                }
            )
        }
    ) {
        val mapInitialized = remember(map) { mutableStateOf(false) }
        LaunchedEffect(map, mapInitialized) {
            if (!mapInitialized.value) {
                val googleMap = map.awaitMap()
                val position = LatLng(latitude.toDouble(), longitude.toDouble())
                googleMap.addMarker {
                    position(position)
                }
                googleMap.moveCamera(CameraUpdateFactory.newLatLng(position))
                mapInitialized.value = true
            }
        }
        val zoom = rememberSaveable(map) { mutableStateOf(15f) }
        val coroutineScope = rememberCoroutineScope()
        val mapZoom = zoom

        AndroidView({ map }) { mapView ->
            coroutineScope.launch {
                val googleMap = mapView.awaitMap()
                googleMap.setZoom(mapZoom.value)
            }
        }
    }
}