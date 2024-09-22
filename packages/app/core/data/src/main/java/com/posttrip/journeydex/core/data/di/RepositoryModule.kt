package com.posttrip.journeydex.core.data.di

import com.posttrip.journeydex.core.data.repository.MissionRepository
import com.posttrip.journeydex.core.data.repository.MissionRepositoryImpl
import com.posttrip.journeydex.core.data.repository.TravelRepository
import com.posttrip.journeydex.core.data.repository.TravelRepositoryImpl
import com.posttrip.journeydex.core.data.repository.UserRepository
import com.posttrip.journeydex.core.data.repository.UserRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    abstract fun bindsUserRepository(
        userRepository: UserRepositoryImpl,
    ): UserRepository

    @Singleton
    @Binds
    abstract fun bindsTravelRepository(
        travelRepository: TravelRepositoryImpl,
    ): TravelRepository

    @Singleton
    @Binds
    abstract fun bindsMissionRepository(
        missionRepository: MissionRepositoryImpl,
    ): MissionRepository
}
