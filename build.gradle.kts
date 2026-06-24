buildscript {
    repositories {
        google()
        mavenCentral()
    }
    dependencies {
        classpath(libs.hilt.gradle.plugin)
        classpath("com.google.devtools.ksp:com.google.devtools.ksp.gradle.plugin:1.9.22-1.0.17")
    }
}

plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.kotlin.android) apply false
    alias(libs.plugins.kotlin.serialization) apply false
}
