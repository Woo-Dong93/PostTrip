package com.posttrip.journeydex.feature.map

import android.os.Looper
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
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
import com.kakao.vectormap.camera.CameraUpdateFactory
import com.kakao.vectormap.route.RouteLineLayer
import com.posttrip.journeydex.core.data.model.travel.Course
import com.posttrip.journeydex.feature.home.component.CourseDetailBottomSheet
import com.posttrip.journeydex.feature.map.databinding.ViewKakaoMapBinding
import com.posttrip.journeydex.feature.map.util.MapUtil
import com.posttrip.journeydex.feature.map.util.MapUtil.calculateCentroid
import com.posttrip.journeydex.feature.map.util.MapUtil.calculateZoomLevel
import com.posttrip.journeydex.feature.map.util.MapUtil.lastLat
import com.posttrip.journeydex.feature.map.util.MapUtil.lastLng
import com.posttrip.journeydex.feature.map.util.MapUtil.setLine
import kotlinx.coroutines.delay
import kotlin.math.roundToInt


@Composable
fun MapScreen(
    onDetail : (Course) -> Unit,
    onLoadingShow : (Boolean) -> Unit,
    viewModel: MapViewModel = hiltViewModel()
) {
    val context = LocalContext.current
    lateinit var fusedLocationClient: FusedLocationProviderClient
    lateinit var locationCallback: LocationCallback

    var mapBiding by remember { mutableStateOf<ViewKakaoMapBinding?>(null) }
    var kakaoMap by remember { mutableStateOf<KakaoMap?>(null) }
    var lines by remember {
        mutableStateOf<RouteLineLayer?>(null)
    }
    var isSearchMode by remember {
        mutableStateOf(false)
    }
    var isInit by remember {
        mutableStateOf(false)
    }
    val course by viewModel.courses.collectAsStateWithLifecycle()
    val normalDetailCourses by viewModel.normalDetailCourses.collectAsStateWithLifecycle()
    val collectingDetailCourses by viewModel.collectingDetailCourses.collectAsStateWithLifecycle()
    val lineDetailCourses by viewModel.lineDetailCourses.collectAsStateWithLifecycle()

    var shownBottomSheet by remember {
        mutableStateOf(false)
    }

    if (shownBottomSheet) {
        lineDetailCourses?.let {
            CourseDetailBottomSheet(
                onDetail = onDetail,
                courseList = lineDetailCourses!!,
                onDismiss = {
                    shownBottomSheet = false
                }
            )
        }

    }

    LaunchedEffect(key1 = Unit) {
        viewModel.initCourseDetail()
        viewModel.getCourse("1")
    }

    LaunchedEffect(key1 = Unit) {
        viewModel.shownLoading.collect {
            onLoadingShow(it)
        }
    }

    LaunchedEffect(key1 = lineDetailCourses) {
        if (lineDetailCourses != null) {
            kakaoMap?.routeLineManager?.layer?.removeAll()
            delay(1000)
            var minLatitude = Double.MAX_VALUE
            var maxLatitude = Double.MIN_VALUE
            var minLongitude = Double.MAX_VALUE
            var maxLongitude = Double.MIN_VALUE
            lineDetailCourses!!.courses.forEachIndexed { index, course ->
                minLatitude = minOf(minLatitude, course.y.toDouble())
                maxLatitude = maxOf(maxLatitude, course.y.toDouble())
                minLongitude = minOf(minLongitude, course.x.toDouble())
                maxLongitude = maxOf(maxLongitude, course.x.toDouble())
                if (index == lineDetailCourses!!.courses.size - 1) return@forEachIndexed
                val start = lineDetailCourses!!.courses[index]
                val end = lineDetailCourses!!.courses[index + 1]
                setLine(
                    startX = start.x.toDouble(),
                    startY = start.y.toDouble(),
                    endX = end.x.toDouble(),
                    endY = end.y.toDouble(),
                    kakaoMap = kakaoMap
                )
            }
            val center = calculateCentroid(
                lineDetailCourses!!.courses.map {
                    MapUtil.Point(
                        x = it.x.toDouble(),
                        y = it.y.toDouble()
                    )
                }
            )
            val zoomLevel = calculateZoomLevel(minLatitude, maxLatitude, minLongitude, maxLongitude)
            viewModel.updateLoading(false)
            kakaoMap?.moveCamera(
                CameraUpdateFactory.newCenterPosition(
                    LatLng.from(
                        center.y,
                        center.x
                    ),
                    zoomLevel.roundToInt()
                )
            )
        }
    }

    LaunchedEffect(key1 = normalDetailCourses) {
        if (course.isNotEmpty() && normalDetailCourses != null) {
//             MapUtil.setLabelClickEvent(
//                 courseList = normalDetailCourses,
//                 kakaoMap = kakaoMap,
//                 onShowBottomSheet = {
//                     shownBottomSheet = true
//                 }
//             )
            var x = 0.0
            var y = 0.0
            val layer = kakaoMap?.labelManager?.getLayer("normalCourses")
            layer?.let {
                kakaoMap?.labelManager?.remove(layer)
            }
            //  kakaoMap?.labelManager?.clearAll()


            normalDetailCourses!!.courses.forEachIndexed { index, course ->
                x += course.x.toDouble()
                y += course.y.toDouble()
                MapUtil.setLocationByPoints(
                    x = course.x.toDouble(),
                    y = course.y.toDouble(),
                    kakaoMap = kakaoMap,
                    id = "normalCourses",
                    labelId = "normalCourses,${course.contentId}"
                )
            }
            kakaoMap?.setOnLabelClickListener { kakaoMap, labelLayer, label ->
                val (type, id) = label.labelId.split(",")
                if (type == "normalCourses") {
                    shownBottomSheet = true
                } else {
                    lineDetailCourses?.let {
                        viewModel.refresh(it)
                        Toast.makeText(context, "캐릭터 수집 액션", Toast.LENGTH_SHORT).show()

                    }
                }

            }

        }
    }

    LaunchedEffect(key1 = collectingDetailCourses) {
        if (collectingDetailCourses != null) {
//            MapUtil.setCollectingLabelClickEvent(
//                courseList = collectingDetailCourses,
//                kakaoMap = kakaoMap,
//                onShowBottomSheet = {
//                    shownBottomSheet = true
//                }
//            )
            var x = 0.0
            var y = 0.0
            val layer = kakaoMap?.labelManager?.getLayer("collectingCourses")
            layer?.let {
                kakaoMap?.labelManager?.remove(layer)
            }
            //  kakaoMap?.labelManager?.clearAll()
            collectingDetailCourses!!.courses.forEachIndexed { index, course ->
                x += course.x.toDouble()
                y += course.y.toDouble()
                MapUtil.setCollectingLocationByPoints(
                    x = course.x.toDouble(),
                    y = course.y.toDouble(),
                    kakaoMap = kakaoMap,
                    id = "collectingCourses",
                    labelId = "collectingCourses,${course.contentId}"
                )

            }
            kakaoMap?.setOnLabelClickListener { kakaoMap, labelLayer, label ->
                val (type, id) = label.labelId.split(",")
                if (type == "normalCourses") {
                    shownBottomSheet = true

                } else {
                    lineDetailCourses?.let {
                        viewModel.refresh(it)
                        Toast.makeText(context, "캐릭터 수집 액션", Toast.LENGTH_SHORT).show()

                    }
                }

            }
        }
    }

    ComposableLifecycle { source, event ->
        when (event) {
            Lifecycle.Event.ON_RESUME -> {
                mapBiding?.mapView?.resume()
                fusedLocationClient = LocationServices.getFusedLocationProviderClient(context)
                val locationRequest = LocationRequest.create().apply {
                    interval = 10000 // 위치 업데이트 간격 (10초)
                    fastestInterval = 5000 // 가장 빠른 업데이트 간격 (5초)
                    priority = LocationRequest.PRIORITY_HIGH_ACCURACY // 높은 정확도
                }

                locationCallback = object : LocationCallback() {
                    override fun onLocationResult(locationResult: LocationResult) {
                        for (location in locationResult.locations) {
                            // 위치 업데이트를 처리합니다.
                            MapUtil.updateUIWithLocation(
                                location,
                                kakaoMap,
                                isInit = isInit,
                                viewModel = viewModel,
                                onInit = {
                                    isInit = true
                                }
                            )
                        }
                    }
                }


                fusedLocationClient.requestLocationUpdates(
                    locationRequest,
                    locationCallback,
                    Looper.getMainLooper()
                )
            }

            Lifecycle.Event.ON_PAUSE -> {
                mapBiding?.mapView?.pause()
                MapUtil.stopLocationUpdates(
                    fusedLocationClient, locationCallback
                )
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
                    if (p0.hashCode() != kakaoMap?.hashCode()) {
                        kakaoMap = p0
                        if (lastLat != 0.toDouble())
                            kakaoMap?.moveCamera(
                                CameraUpdateFactory.newCenterPosition(
                                    LatLng.from(
                                        lastLat,
                                        lastLng
                                    ),
                                    12
                                )
                            )
                    }

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
