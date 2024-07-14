package com.posttrip.journeydex.feature.map

import android.util.Log
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
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.LifecycleOwner
import com.kakao.vectormap.KakaoMap
import com.kakao.vectormap.KakaoMapReadyCallback
import com.kakao.vectormap.MapLifeCycleCallback
import com.posttrip.journeydex.feature.map.databinding.ViewKakaoMapBinding
import java.lang.Exception

@Composable
fun MapScreen() {
    var mapBiding by remember { mutableStateOf<ViewKakaoMapBinding?>(null) }

    ComposableLifecycle{ source, event ->
        when(event){
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
        ){
            mapBiding = this
            mapView.start(object : MapLifeCycleCallback(){
                override fun onMapDestroy() {
                    mapView.finish()
                }

                override fun onMapError(p0: Exception?) {
                    mapView.finish()
                }

            }, object : KakaoMapReadyCallback() {
                override fun onMapReady(p0: KakaoMap) {

                }

            })
        }
    }
}

@Composable
fun ComposableLifecycle(
    lifecycleOwner: LifecycleOwner = LocalLifecycleOwner.current,
    onEvent:(LifecycleOwner, Lifecycle.Event) ->Unit
) {

    DisposableEffect(lifecycleOwner){
        val observer = LifecycleEventObserver{source,event->
            onEvent(source,event)
        }
        lifecycleOwner.lifecycle.addObserver(observer)

        onDispose {
            lifecycleOwner.lifecycle.removeObserver(observer)
        }
    }
}