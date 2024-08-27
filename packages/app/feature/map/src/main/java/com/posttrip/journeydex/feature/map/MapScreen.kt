package com.posttrip.journeydex.feature.map

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalLifecycleOwner
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
import com.kakao.vectormap.camera.CameraUpdate
import com.kakao.vectormap.camera.CameraUpdateFactory
import com.kakao.vectormap.label.LabelOptions
import com.kakao.vectormap.label.LabelStyle
import com.kakao.vectormap.label.LabelStyles
import com.posttrip.journeydex.feature.map.databinding.ViewKakaoMapBinding


@Composable
fun MapScreen(
    viewModel: MapViewModel = hiltViewModel()
) {
    var mapBiding by remember { mutableStateOf<ViewKakaoMapBinding?>(null) }
    var kakaoMap by remember { mutableStateOf<KakaoMap?>(null) }
    val course by viewModel.courses.collectAsStateWithLifecycle()

    fun setLocationByPoints(
        x: Double,
        y: Double
    ) {
        kakaoMap?.let { kakaoMap ->
//            val styles =
//                kakaoMap.labelManager?.addLabelStyles(LabelStyles.from(LabelStyle.from(R.drawable.pin)))
//            val options = LabelOptions.from(LatLng.from(y,x)).setStyles(styles)
//            kakaoMap.labelManager?.layer?.addLabel(options)

        }
    }

    fun setLabelClickEvent(){
        kakaoMap?.let { kakaoMap ->
            kakaoMap.setOnViewportClickListener { map, latLng, pointF ->
                course.find {
                    DistanceCalculator.isWithin1Km(
                        lat1 = latLng.latitude, lat2 = it.y.toDouble(),
                        lon1 = latLng.longitude, lon2 = it.x.toDouble()
                    )
                }?.let {
                    if(!it.isDetail) viewModel.getCourseDetail(it.contentId)
                }

            }
        }
    }

    LaunchedEffect(key1 = Unit) {
        viewModel.getCourse("")
    }

    LaunchedEffect(key1 = course) {
        if (course.isNotEmpty()) {
            setLabelClickEvent()

            kakaoMap?.labelManager?.clearAll()
            course.forEachIndexed { index, course ->
                setLocationByPoints(
                    x = course.x.toDouble(),
                    y = course.y.toDouble()
                )
                if(index == 30) return@LaunchedEffect
            }
        }
    }

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
                    kakaoMap = p0
//                    kakaoMap?.moveCamera(
//                        CameraUpdateFactory.newCenterPosition(LatLng.from(37.102005, 127.108621))
//
//                    )
                }

            })
        }
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