package com.posttrip.journeydex.feature.map

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
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidViewBinding
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.kakao.vectormap.KakaoMap
import com.kakao.vectormap.KakaoMapReadyCallback
import com.kakao.vectormap.LatLng
import com.kakao.vectormap.MapLifeCycleCallback
import com.kakao.vectormap.camera.CameraUpdateFactory
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
import com.posttrip.journeydex.feature.map.util.DistanceCalculator
import com.posttrip.journeydex.feature.map.util.MapUtil
import com.posttrip.journeydex.feature.map.util.MapUtil.calculateCentroid
import com.posttrip.journeydex.feature.map.util.MapUtil.calculateZoomLevel
import kotlinx.coroutines.delay
import kotlin.math.roundToInt


@Composable
fun MapScreen(
    viewModel: MapViewModel = hiltViewModel()
) {
    var mapBiding by remember { mutableStateOf<ViewKakaoMapBinding?>(null) }
    var kakaoMap by remember { mutableStateOf<KakaoMap?>(null) }
    var isSearchMode by remember {
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

            kakaoMap.labelManager?.layer?.addLabel(options)
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
            kakaoMap.setOnLabelClickListener { map, labelLayer, label ->

            }
        }
    }


    LaunchedEffect(key1 = Unit) {
        viewModel.getCourse("1")
    }

    LaunchedEffect(key1 = detailCourses) {
        if (course.isNotEmpty() && detailCourses != null) {
             setLabelClickEvent()
            var minLatitude = Double.MAX_VALUE
            var maxLatitude = Double.MIN_VALUE
            var minLongitude = Double.MAX_VALUE
            var maxLongitude = Double.MIN_VALUE
            var x = 0.0
            var y = 0.0
            kakaoMap?.labelManager?.clearAll()
            detailCourses!!.courses.forEachIndexed { index, course ->
                x += course.x.toDouble()
                y += course.y.toDouble()
                setLocationByPoints(
                    x = course.x.toDouble(),
                    y = course.y.toDouble()
                )
                minLatitude = minOf(minLatitude, course.y.toDouble())
                maxLatitude = maxOf(maxLatitude, course.y.toDouble())
                minLongitude = minOf(minLongitude, course.x.toDouble())
                maxLongitude = maxOf(maxLongitude, course.x.toDouble())
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
                    MapUtil.Point(
                        x = it.x.toDouble(),
                        y = it.y.toDouble()
                    )
                }
            )
            val zoomLevel = calculateZoomLevel(minLatitude, maxLatitude, minLongitude, maxLongitude)

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


//    LaunchedEffect(kakaoMap) {
//        if(kakaoMap != null) {
//            delay(500)
//            viewModel.initCourseDetail()
//        }
//    }

    ComposableLifecycle { source, event ->
        when (event) {
            Lifecycle.Event.ON_RESUME -> {
                mapBiding?.mapView?.resume()
            }

            Lifecycle.Event.ON_PAUSE -> {
                mapBiding?.mapView?.pause()
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
                    if(kakaoMap == null){
                        kakaoMap = p0
                        kakaoMap?.moveCamera(
                            CameraUpdateFactory.zoomTo(12)
                        )
                        viewModel.initCourseDetail()
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
