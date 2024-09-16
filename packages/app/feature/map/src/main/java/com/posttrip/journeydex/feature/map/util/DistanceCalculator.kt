package com.posttrip.journeydex.feature.map.util

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
