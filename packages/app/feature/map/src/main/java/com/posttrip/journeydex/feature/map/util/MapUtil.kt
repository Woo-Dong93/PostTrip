package com.posttrip.journeydex.feature.map.util

import android.location.Location
import android.os.Looper
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.kakao.vectormap.KakaoMap
import com.kakao.vectormap.LatLng
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
import com.posttrip.journeydex.core.data.model.response.CourseList
import com.posttrip.journeydex.feature.map.MapViewModel
import com.posttrip.journeydex.feature.map.R

object MapUtil {

    var lastLat = 0.toDouble()
    var lastLng = 0.toDouble()


    fun setLocationByPoints(
        x: Double,
        y: Double,
        id : String,
        labelId : String,
        kakaoMap: KakaoMap?
    ) {
        kakaoMap?.let { kakaoMap ->
            val layer = LabelLayerOptions.from(id)
            val styles =
                kakaoMap.labelManager?.addLabelStyles(LabelStyles.from(LabelStyle.from(R.drawable.ic_pin).setAnchorPoint(0.5f,0.5f)))

            val options = LabelOptions.from(labelId,LatLng.from(y, x)).setStyles(styles)


            layer.setZOrder(1100)
            kakaoMap.labelManager?.addLayer(layer)
            kakaoMap.labelManager?.getLayer(id)?.addLabel(options)
        }
    }

    fun setCollectingLocationByPoints(
        x: Double,
        y: Double,
        id : String,
        collected : Boolean,
        enabledToCollect : Boolean,
        labelId : String,
        kakaoMap: KakaoMap?
    ) {
        kakaoMap?.let { kakaoMap ->
            val styles =
                kakaoMap.labelManager?.addLabelStyles(LabelStyles.from(
                    LabelStyle.from(
                        if(collected) R.drawable.ic_char1_finished
                        else if(enabledToCollect) R.drawable.ic_char1_enable
                        else  R.drawable.ic_char1_pending
                    ).setAnchorPoint(0.5f,0.5f)))

            val options = LabelOptions.from(labelId,LatLng.from(y, x)).setStyles(styles)
            val layer = LabelLayerOptions.from(id)
            layer.setZOrder(1400)
            kakaoMap.labelManager?.addLayer(layer)
            kakaoMap.labelManager?.getLayer(id)?.addLabel(options)
        }
    }

    fun setLine(
        startX: Double,
        startY: Double,
        endX : Double,
        endY : Double,
        kakaoMap: KakaoMap?
    ) {
        kakaoMap?.let { kakaoMap ->
            val layer = kakaoMap.routeLineManager?.layer
            val styles = RouteLineStylesSet.from("redStyle",
                RouteLineStyles.from(RouteLineStyle.from(4f, Color(0xFF4B7CFD).toArgb())));

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

    fun setLabelClickEvent(
        kakaoMap: KakaoMap?,
        courseList : CourseList?,
        onShowBottomSheet : () -> Unit
    ) {
        kakaoMap?.let { kakaoMap ->
            kakaoMap.setOnViewportClickListener { map, latLng, pointF ->
                courseList?.courses?.find {
                    DistanceCalculator.isWithin1Km(
                        lat1 = latLng.latitude, lat2 = it.y.toDouble(),
                        lon1 = latLng.longitude, lon2 = it.x.toDouble()
                    )
                }?.let {
                    onShowBottomSheet()
                }

            }
        }
    }

    fun setCollectingLabelClickEvent(
        kakaoMap: KakaoMap?,
        courseList : CourseList?,
        onShowBottomSheet : () -> Unit
    ) {
        kakaoMap?.let { kakaoMap ->
//            kakaoMap.setOnViewportClickListener { map, latLng, pointF ->
//                courseList?.courses?.find {
//                    DistanceCalculator.isWithin1Km(
//                        lat1 = latLng.latitude, lat2 = it.y.toDouble(),
//                        lon1 = latLng.longitude, lon2 = it.x.toDouble()
//                    )
//                }?.let {
//                    onShowBottomSheet()
//                }
//
//            }
        }
    }



    fun stopLocationUpdates(
        fusedLocationClient : FusedLocationProviderClient,
        locationCallback : LocationCallback
    ) {
        fusedLocationClient.removeLocationUpdates(locationCallback)
    }

    fun updateUIWithLocation(
        location: Location,
        kakaoMap: KakaoMap?,
        isInit : Boolean,
        viewModel: MapViewModel,
        onInit : () -> Unit
    ) {
        // 실시간 위치 정보를 UI에 업데이트합니다.
        val latitude = location.latitude
        val longitude = location.longitude

        val deletedLayer = kakaoMap?.labelManager?.getLayer("my")
        deletedLayer?.let {
            kakaoMap?.labelManager?.remove(deletedLayer)
        }


        val styles =
            kakaoMap?.labelManager?.addLabelStyles(
                LabelStyles.from(LabelStyle.from(R.drawable.ic_my))
            )


        val options = LabelOptions.from(LatLng.from(latitude, longitude)).setStyles(styles)
        val layer = LabelLayerOptions.from("my")
        kakaoMap?.labelManager?.addLayer(layer)
        kakaoMap?.labelManager?.getLayer("my")?.addLabel(options)

        if(isInit) return
        if(viewModel.contentId != null && viewModel.contentId != "-1") return

        lastLat = latitude
        lastLng = longitude
        viewModel.updateMyPoint(latitude,longitude)
        kakaoMap?.moveCamera(
            CameraUpdateFactory.newCenterPosition(
                LatLng.from(
                    latitude,
                    longitude
                )
            )
        )
        onInit()
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

    fun calculateZoomLevel(minLat: Double, maxLat: Double, minLng: Double, maxLng: Double): Float {
        // 좌표 간 거리 계산
        val latRange = maxLat - minLat
        val lngRange = maxLng - minLng

        // 줌 레벨 계산을 위한 기본 값 설정 (예시 값이며, 조정 필요)
        val worldLatRange = 180 // 전 세계 위도 범위
        val worldLngRange = 360 // 전 세계 경도 범위

        // 지도의 크기(해상도)에 맞춰 줌 레벨을 계산
        // 화면 크기나 필요에 따라 조정 가능한 값
        val latZoom = (worldLatRange / latRange).let { log2(it.toFloat()) }
        val lngZoom = (worldLngRange / lngRange).let { log2(it.toFloat()) }

        // 최종 줌 레벨은 위도와 경도 줌 레벨 중 최소값 선택
        val calculatedZoom = minOf(latZoom, lngZoom)

        // 줌 레벨의 최대 최소값을 제한 (1 - 21 사이에서 선택, 카카오맵 기준)
        return calculatedZoom.coerceIn(1.0f, 21.0f)
    }

    private fun log2(value: Float): Float = (Math.log(value.toDouble()) / Math.log(2.0)).toFloat()


}
