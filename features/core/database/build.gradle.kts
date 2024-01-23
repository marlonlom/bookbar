/*
 * Copyright 2024 Marlonlom
 * SPDX-License-Identifier: Apache-2.0
 */

@Suppress("DSL_SCOPE_VIOLATION")
plugins {
  alias(libs.plugins.android.library)
  alias(libs.plugins.kotlin.android)
  alias(libs.plugins.google.devtools.ksp)
}

android {
  namespace = "dev.marlonlom.apps.bookbar.core.database"
  compileSdk = 34

  defaultConfig {
    minSdk = 24

    testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    consumerProguardFiles("consumer-rules.pro")
  }

  buildTypes {
    release {
      isMinifyEnabled = false
      proguardFiles(
        getDefaultProguardFile("proguard-android-optimize.txt"),
        "proguard-rules.pro"
      )
    }
  }

  compileOptions {
    sourceCompatibility = JavaVersion.VERSION_17
    targetCompatibility = JavaVersion.VERSION_17
  }

  kotlinOptions {
    jvmTarget = JavaVersion.VERSION_17.toString()
  }
}

dependencies {

  implementation(libs.androidx.core.ktx)
  implementation(libs.bundles.database.room)

  ksp(libs.androidx.room.compiler)

  testImplementation(libs.junit)
  testImplementation(libs.androidx.room.testing)

  androidTestImplementation(libs.androidx.arch.core.testing)
  androidTestImplementation(libs.androidx.test.espresso.core)
  androidTestImplementation(libs.androidx.test.ext.junit)
  androidTestImplementation(libs.google.truth)
  androidTestImplementation(libs.kotlinx.coroutines.test)
}
