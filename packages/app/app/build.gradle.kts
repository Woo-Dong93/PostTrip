import com.android.build.gradle.internal.cxx.configure.gradleLocalProperties

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.jetbrains.kotlin.android)
    alias(libs.plugins.hilt.android)
    alias(libs.plugins.kotlin.ksp)
    alias(libs.plugins.compose.compiler)
}

android {
    namespace = "com.posttrip.journeydex"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.posttrip.journeydex"
        minSdk = 26
        targetSdk = 34
        versionCode = 2
        versionName = "1.0.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables.useSupportLibrary = true

        buildConfigField("String", "KAKAO_APP_KEY", gradleLocalProperties(rootDir, providers).getProperty("KAKAO_APP_KEY"))

    }

    signingConfigs {
        create("release") {
            storeFile = file("./keystore.jks")
            storePassword = gradleLocalProperties(rootDir, providers).getProperty("SIGNING_STORE_PASSWORD")
            keyAlias =  gradleLocalProperties(rootDir, providers).getProperty("SIGNING_KEY_ALIAS")
            keyPassword =  gradleLocalProperties(rootDir, providers).getProperty("SIGNING_KEY_PASSWORD")
        }
    }

    buildTypes {
        release {
            signingConfig = signingConfigs.getByName("release")
            isMinifyEnabled = true
            isShrinkResources = true
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
            isDebuggable = false
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    kotlinOptions {
        jvmTarget = "17"
    }
    buildFeatures {
        compose = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.5"
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
}

dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)
    testImplementation(libs.junit)
    implementation(project(":core:data"))
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)

    //hilt
    implementation(libs.androidx.hilt.navigation.compose)
    implementation(libs.hilt.android)
    ksp(libs.hilt.compiler)

    //navigation-compose
    implementation(libs.androidx.navigation.compose)

    //kakao
    implementation(libs.kakao.login)
    implementation(libs.kakao.map)

    implementation(libs.coil.compose)
    implementation(libs.lottie.compose)


    implementation(project(":feature:login"))
    implementation(project(":feature:home"))
    implementation(project(":feature:dex"))
    implementation(project(":feature:map"))
    implementation(project(":feature:reward"))
    implementation(project(":core:auth"))

}
