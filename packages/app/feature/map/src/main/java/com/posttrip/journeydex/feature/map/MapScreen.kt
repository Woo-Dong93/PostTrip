package com.posttrip.journeydex.feature.map

import android.location.Location
import android.os.Looper
import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidViewBinding
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.kakao.vectormap.KakaoMap
import com.kakao.vectormap.KakaoMapReadyCallback
import com.kakao.vectormap.LatLng
import com.kakao.vectormap.MapLifeCycleCallback
import com.kakao.vectormap.camera.CameraUpdate
import com.kakao.vectormap.camera.CameraUpdateFactory
import com.kakao.vectormap.label.LabelLayerOptions
import com.kakao.vectormap.label.LabelOptions
import com.kakao.vectormap.label.LabelStyle
import com.kakao.vectormap.label.LabelStyles
import com.kakao.vectormap.route.RouteLineOptions
import com.kakao.vectormap.route.RouteLineSegment
import com.kakao.vectormap.route.RouteLineStyle
import com.kakao.vectormap.route.RouteLineStyles
import com.kakao.vectormap.route.RouteLineStylesSet
import com.posttrip.journeydex.feature.home.component.CourseDetailBottomSheet
import com.posttrip.journeydex.feature.map.databinding.ViewKakaoMapBinding


@Composable
fun MapScreen(
    viewModel: MapViewModel = hiltViewModel()
) {
    val context = LocalContext.current
    lateinit var fusedLocationClient: FusedLocationProviderClient
    lateinit var locationCallback: LocationCallback

    var mapBiding by remember { mutableStateOf<ViewKakaoMapBinding?>(null) }
    var kakaoMap by remember { mutableStateOf<KakaoMap?>(null) }
    var isSearchMode by remember {
        mutableStateOf(false)
    }
    var isInit by remember {
        mutableStateOf(false)
    }
    val course by viewModel.courses.collectAsStateWithLifecycle()
    val detailCourses by viewModel.detailCourses.collectAsStateWithLifecycle()


    var shownBottomSheet by remember {
        mutableStateOf(false)
    }

    if (shownBottomSheet) {
        detailCourses?.let {
            CourseDetailBottomSheet(
                courseList = detailCourses!!,
                onDismiss = {
                    shownBottomSheet = false
                }
            )
        }

    }

    fun setLocationByPoints(
        x: Double,
        y: Double
    ) {
        kakaoMap?.let { kakaoMap ->
            val styles =
                kakaoMap.labelManager?.addLabelStyles(LabelStyles.from(LabelStyle.from(R.drawable.ic_pin)))
            val options = LabelOptions.from(LatLng.from(y, x)).setStyles(styles)
            val layer = LabelLayerOptions.from("courses")
            kakaoMap.labelManager?.addLayer(layer)
            kakaoMap.labelManager?.getLayer("courses")?.addLabel(options)
        }
    }

    fun setLine(
        startX: Double,
        startY: Double,
        endX : Double,
        endY : Double
    ) {
        kakaoMap?.let { kakaoMap ->
            val layer = kakaoMap.routeLineManager?.layer
            val styles = RouteLineStylesSet.from("redStyle",
                RouteLineStyles.from(RouteLineStyle.from(4f, Color.Red.toArgb())));

            val segment = RouteLineSegment.from(
                listOf(
                    LatLng.from(startY,startX),
                    LatLng.from(endY,endX)
                )
            ).setStyles(styles.getStyles(0))
            val option = RouteLineOptions.from(segment).setStylesSet(styles)
            layer?.addRouteLine(option)
        }
    }

    fun setLabelClickEvent() {
        kakaoMap?.let { kakaoMap ->
            kakaoMap.setOnViewportClickListener { map, latLng, pointF ->
                detailCourses?.courses?.find {
                    DistanceCalculator.isWithin1Km(
                        lat1 = latLng.latitude, lat2 = it.y.toDouble(),
                        lon1 = latLng.longitude, lon2 = it.x.toDouble()
                    )
                }?.let {
                    shownBottomSheet = true
                }

            }
        }
    }



    fun stopLocationUpdates() {
        fusedLocationClient.removeLocationUpdates(locationCallback)
    }

    fun updateUIWithLocation(location: Location) {
        // 실시간 위치 정보를 UI에 업데이트합니다.
        val latitude = location.latitude
        val longitude = location.longitude

        val deletedLayer = kakaoMap?.labelManager?.getLayer("my")
        deletedLayer?.let {
            kakaoMap?.labelManager?.remove(deletedLayer)
        }


        val styles =
            kakaoMap?.labelManager?.addLabelStyles(
                LabelStyles.from(LabelStyle.from(R.drawable.ic_circle_red))
            )


        val options = LabelOptions.from(LatLng.from(latitude, longitude)).setStyles(styles)
        val layer = LabelLayerOptions.from("my")
        kakaoMap?.labelManager?.addLayer(layer)
        kakaoMap?.labelManager?.getLayer("my")?.addLabel(options)

        if(isInit) return
        if(viewModel.contentId != null && viewModel.contentId != "-1") return
        kakaoMap?.moveCamera(
            CameraUpdateFactory.newCenterPosition(
                LatLng.from(
                    latitude,
                    longitude
                )
            )
        )
        isInit = true
    }

    fun startLocationUpdates() {
        val locationRequest = LocationRequest.create().apply {
            interval = 10000 // 위치 업데이트 간격 (10초)
            fastestInterval = 5000 // 가장 빠른 업데이트 간격 (5초)
            priority = LocationRequest.PRIORITY_HIGH_ACCURACY // 높은 정확도
        }

        locationCallback = object : LocationCallback() {
            override fun onLocationResult(locationResult: LocationResult) {
                for (location in locationResult.locations) {
                    // 위치 업데이트를 처리합니다.
                    updateUIWithLocation(location)
                }
            }
        }


        fusedLocationClient.requestLocationUpdates(
            locationRequest,
            locationCallback,
            Looper.getMainLooper()
        )
    }

    LaunchedEffect(key1 = Unit) {
        viewModel.initCourseDetail()
        viewModel.getCourse("1")
    }

    LaunchedEffect(key1 = detailCourses) {
        if (course.isNotEmpty() && detailCourses != null) {
             setLabelClickEvent()

            var x = 0.0
            var y = 0.0
            val layer = kakaoMap?.labelManager?.getLayer("courses")
            layer?.let {
                kakaoMap?.labelManager?.remove(layer)
            }

          //  kakaoMap?.labelManager?.clearAll()
            detailCourses!!.courses.forEachIndexed { index, course ->
                x += course.x.toDouble()
                y += course.y.toDouble()
                setLocationByPoints(
                    x = course.x.toDouble(),
                    y = course.y.toDouble()
                )
            }
            detailCourses!!.courses.forEachIndexed { index, course ->
                if(index == detailCourses!!.courses.size - 1 ) return@forEachIndexed
                val start = detailCourses!!.courses[index]
                val end = detailCourses!!.courses[index + 1]
                setLine(
                    startX = start.x.toDouble(),
                    startY = start.y.toDouble(),
                    endX = end.x.toDouble(),
                    endY = end.y.toDouble()
                )
            }
            val center = calculateCentroid(
                detailCourses!!.courses.map {
                    Point(
                        x = it.x.toDouble(),
                        y = it.y.toDouble()
                    )
                }
            )
            kakaoMap?.moveCamera(
                CameraUpdateFactory.newCenterPosition(
                    LatLng.from(
                        center.y,
                        center.x
                    )
                )
            )
        }
    }

    ComposableLifecycle { source, event ->
        when (event) {
            Lifecycle.Event.ON_RESUME -> {
                mapBiding?.mapView?.resume()
                fusedLocationClient = LocationServices.getFusedLocationProviderClient(context)
                startLocationUpdates()
            }

            Lifecycle.Event.ON_PAUSE -> {
                mapBiding?.mapView?.pause()
                stopLocationUpdates()
            }

            Lifecycle.Event.ON_DESTROY -> {
                mapBiding?.mapView?.finish()
                mapBiding = null
            }

            else -> {}
        }

    }

    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        AndroidViewBinding(
            modifier = Modifier.fillMaxSize(),
            factory = ViewKakaoMapBinding::inflate
        ) {
            mapBiding = this
            mapView.start(object : MapLifeCycleCallback() {
                override fun onMapDestroy() {
                    mapView.finish()
                }

                override fun onMapError(p0: Exception?) {
                    mapView.finish()
                }

            }, object : KakaoMapReadyCallback() {
                override fun onMapReady(p0: KakaoMap) {
                    kakaoMap = p0
                    kakaoMap?.moveCamera(
                        CameraUpdateFactory.zoomTo(12)
                    )
                }

            })
        }
        SearchText(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp)
                .padding(top = 22.dp)
                .height(56.dp)
                .align(Alignment.TopCenter),
            onClick = {
                isSearchMode = true
            }
        )

    }
    if (isSearchMode) {
        SearchScreen(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFFFAFAFA)),
            courseList = course,
            onDismiss = {
                viewModel.getCourseDetail(it)
                isSearchMode = false
            }
        )
    }
}

@Composable
fun ComposableLifecycle(
    lifecycleOwner: LifecycleOwner = LocalLifecycleOwner.current,
    onEvent: (LifecycleOwner, Lifecycle.Event) -> Unit
) {

    DisposableEffect(lifecycleOwner) {
        val observer = LifecycleEventObserver { source, event ->
            onEvent(source, event)
        }
        lifecycleOwner.lifecycle.addObserver(observer)

        onDispose {
            lifecycleOwner.lifecycle.removeObserver(observer)
        }
    }
}

object DistanceCalculator {

    private const val EARTH_RADIUS = 6371.0 // 지구 반경 (단위: km)

    fun isWithin1Km(lat1: Double, lon1: Double, lat2: Double, lon2: Double): Boolean {
        // 위도와 경도를 라디안으로 변환
        val lat1Rad = Math.toRadians(lat1)
        val lon1Rad = Math.toRadians(lon1)
        val lat2Rad = Math.toRadians(lat2)
        val lon2Rad = Math.toRadians(lon2)

        // 하버사인 공식에 필요한 각도 차이 계산
        val deltaLat = lat2Rad - lat1Rad
        val deltaLon = lon2Rad - lon1Rad

        // 하버사인 공식 계산
        val a = Math.sin(deltaLat / 2) * Math.sin(deltaLat / 2) +
                Math.cos(lat1Rad) * Math.cos(lat2Rad) *
                Math.sin(deltaLon / 2) * Math.sin(deltaLon / 2)
        val c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a))

        // 거리 계산 (단위: km)
        val distance = EARTH_RADIUS * c

        // 거리 비교 (1km 이하인지 확인)
        return distance <= 0.5
    }
}

fun calculateCentroid(points: List<Point>): Point {
    try {
        var area = 0.0
        var centroidX = 0.0
        var centroidY = 0.0

        // Loop through all edges of the polygon
        for (i in points.indices) {
            val current = points[i]
            val next = points[(i + 1) % points.size]

            // Calculate the area contribution of this edge
            val crossProduct = current.x * next.y - next.x * current.y
            area += crossProduct

            // Calculate the centroid contribution of this edge
            centroidX += (current.x + next.x) * crossProduct
            centroidY += (current.y + next.y) * crossProduct
        }

        area *= 0.5
        centroidX /= (6 * area)
        centroidY /= (6 * area)

        return Point(centroidX, centroidY)
    }catch (e : Exception){
        return Point(0.0,0.0)
    }

}

data class Point(val x: Double, val y: Double)

