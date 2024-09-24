package com.posttrip.journeydex.feature.map

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kakao.vectormap.LatLng
import com.posttrip.journeydex.core.data.model.request.SearchCourse
import com.posttrip.journeydex.core.data.model.response.CourseList
import com.posttrip.journeydex.core.data.model.travel.Course
import com.posttrip.journeydex.core.data.repository.TravelRepository
import com.posttrip.journeydex.core.data.util.LoginCached
import com.posttrip.journeydex.feature.map.util.DistanceCalculator.calculateDistance
import com.posttrip.journeydex.feature.map.util.MapUtil
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.mapNotNull
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MapViewModel @Inject constructor(
    private val travelRepository: TravelRepository,
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {
    val contentId: String? = savedStateHandle.get<String>("contentId")
//    var cachedContentId : String? = ""
//    init {
//        cachedContentId = contentId
//    }

    private val _courses = MutableStateFlow<List<Course>>(emptyList())
    val courses : StateFlow<List<Course>> = _courses.asStateFlow()

    private val _myPoint = MutableStateFlow<MapUtil.Point?>(null)
    val myPoint : StateFlow<MapUtil.Point?> = _myPoint.asStateFlow()

//    var query by mutableStateOf("")
//        private set
//
//    val debouncedSearchQuery: Flow<String> = snapshotFlow {
//        query
//    }.debounce(1000)
//        .filter { it.isNotEmpty() }
//        .distinctUntilChanged().stateIn(viewModelScope, SharingStarted.Eagerly,"")

    private val _lineDetailCourses = MutableStateFlow<CourseList?>(null)
    val lineDetailCourses : StateFlow<CourseList?> = _lineDetailCourses.asStateFlow()

    private val _normalDetailCourses = MutableStateFlow<CourseList?>(null)
    val normalDetailCourses : StateFlow<CourseList?> = _normalDetailCourses.asStateFlow()

    private val _collectingDetailCourses = MutableStateFlow<CourseList?>(null)
    val collectingDetailCourses : StateFlow<CourseList?> = _collectingDetailCourses.asStateFlow()

    private val _finishDetailCourses = MutableStateFlow<CourseList?>(null)
    val finishDetailCourses : StateFlow<CourseList?> = _finishDetailCourses.asStateFlow()

    private var _shownLoading = MutableSharedFlow<Boolean>()
    val shownLoading : SharedFlow<Boolean> = _shownLoading.asSharedFlow()

    private val _showErrorToast = MutableSharedFlow<String>(extraBufferCapacity = 1)
    val showErrorToast: SharedFlow<String> = _showErrorToast.asSharedFlow()


    var cachecd : List<Course> = emptyList()

    val realCollectingDetailCourses : StateFlow<CourseList?> =
        combine(
            myPoint,
            collectingDetailCourses
        ){ my, courses ->
            val mCourses = collectingDetailCourses.value?.courses ?: emptyList()
            courses?.copy(
                courses = mCourses.map {
                    it.copy(
                        enabledToCollect = calculateDistance(my?.x ?: 0.0,my?.y  ?: 0.0,it.y.toDouble(),it.x.toDouble()) <= 1
                    )
                }
            )
        }.stateIn(
            viewModelScope,
            SharingStarted.Eagerly,
            null
        )

    fun clearStateHandle() {
       // savedStateHandle.remove<String>("contentId")
    }

    fun initCourseDetail(){
      //  val contentId = savedStateHandle.get<String>("contentId")
        if(contentId != null && contentId != "-1"){
            viewModelScope.launch {
                delay(300)
                _shownLoading.emit(true)
                val courses =travelRepository.getCachedCourse(contentId!!)
                if(courses == null){
                    delay(2700)
                    travelRepository.getCourseDetail(contentId!!).catch {
                        _shownLoading.emit(false)
                    }.collect {

                        emitDetails(CourseList(
                            course = it.data,
                            courses = it.data.courseList.map {
                                it.copy(isDetail = true)
                            }
                        ))
                        _shownLoading.emit(false)
                    }
                }else {
                    delay(2700)
                    _shownLoading.emit(false)
                    emitDetails(courses)
                }

            }
        }else {
            _lineDetailCourses.value = null
            _collectingDetailCourses.value = null
            _normalDetailCourses.value = null
        }
    }

    fun getCourse(id : String) {
        viewModelScope.launch {
            travelRepository.getCourse(LoginCached.kakaoId)
                .catch {

                }.collect { course ->
                    _courses.emit(course.courses)
                    cachecd = course.courses
                }
        }
    }

    fun getCourseDetail(course : Course) {
        viewModelScope.launch {
            _shownLoading.emit(true)
            travelRepository.getCourseDetail(course.contentId)
                .catch {

                }.collect {
                    delay(1000)
                    val courseList = CourseList(
                        course = it.data,
                        courses = it.data.courseList.map {
                            it.copy(isDetail = true)
                        }
                    )
                    emitDetails(courseList)
                }
        }
    }

    fun emitDetails(courseList : CourseList) {
        viewModelScope.launch {
            val collecting = courseList.courses.filter {
                it.characterInfo.id.isNotEmpty()
            }
            val normal = courseList.courses.filter {
                it.characterInfo.id.isEmpty()
            }
            _normalDetailCourses.emit(
                courseList.copy(
                    courses = normal
                )
            )
            _collectingDetailCourses.emit(
                courseList.copy(
                    courses = collecting
                )
            )
//            _finishDetailCourses.emit()


            _lineDetailCourses.emit(courseList)
        }
    }

    fun collectCharacter(
        id : String
    ) {
        viewModelScope.launch {
            travelRepository.collectCharacter(
                id
            ).catch {

            }.collect {
                val course = normalDetailCourses.value?.course!!
                _shownLoading.emit(true)
                travelRepository.getCourseDetail(course.contentId)
                    .catch {

                    }.collect {
                        delay(1000)
                        val courseList = CourseList(
                            course = it.data,
                            courses = it.data.courseList.map {
                                it.copy(isDetail = true)
                            }
                        )
                        travelRepository.cacheCourse(contentId = course.contentId,
                            courseList)
                        emitDetails(courseList)
                    }
            }
        }
    }

    fun refresh(courseList : CourseList){
        viewModelScope.launch {
            _normalDetailCourses.emit(
                courseList
            )
            _collectingDetailCourses.emit(
                CourseList(
                    emptyList()
                )
            )
        }
    }

    fun updateLoading(boolean: Boolean){
        viewModelScope.launch {
            _shownLoading.emit(boolean)
        }
    }

    fun cacheDetail(course: Course){
        viewModelScope.launch {
            travelRepository.cacheCourseDetail(course.contentId,course)
        }
    }
//    fun updateQuery(input: String) {
//        query = input
//    }

    fun searchCourseList(
        keyword : String,
        style : String,
        dest : String,
        type : String
    ){
        viewModelScope.launch {
            travelRepository.searchCourse(
                SearchCourse(
                    area = keyword,
                    travelStyleKeyword = style,
                    destinationTypeKeyword = dest,
                    travelTypeKeyword = type
                )
            ).catch {

                }.collect { course ->
                    val courseList = course.courses
                    _courses.emit(if(courseList.size >= 52) courseList.subList(0,51) else courseList)
                    //cachecd = course.courses
                }
        }
    }

    fun updateMyPoint(
        x : Double,
        y : Double
    ){
        viewModelScope.launch {
            _myPoint.emit(
                MapUtil.Point(x,y)
            )
        }
    }
}
