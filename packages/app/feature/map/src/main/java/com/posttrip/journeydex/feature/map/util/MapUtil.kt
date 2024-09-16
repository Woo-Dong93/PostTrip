package com.posttrip.journeydex.feature.map.util

object MapUtil {



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
