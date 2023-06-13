pluginManagement {
    repositories {
        gradlePluginPortal()
        google()
        mavenCentral()
    }
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
    }
    versionCatalogs {
        create("libs") {
            version("compilesdk", "33")
            version("minsdk", "26")
            version("targetsdk", "33")
            version("kotlinCompilerExtensionVersion", "1.4.3")

            version("coil_version", "2.4.0")
            version("gson_version", "2.10.1")
            version("retrofit_version", "2.9.0")
            version("media_version", "1.6.0")
            version("media3_version", "1.0.2")
            version("core_version", "1.9.0")
            version("appcompat_version", "1.6.1")

            library("androidx-core", "androidx.core", "core").versionRef("core_version")
            library("androidx-core-ktx", "androidx.core", "core-ktx").versionRef("core_version")
            library(
                "androidx-appcompat",
                "androidx.appcompat",
                "appcompat"
            ).versionRef("appcompat_version")

            library("coil", "io.coil-kt", "coil-compose").versionRef("coil_version")
            library("gson", "com.google.code.gson", "gson").versionRef("gson_version")
            library("retrofit", "com.squareup.retrofit2", "retrofit").versionRef("retrofit_version")
            library(
                "retrofit-converter-gson",
                "com.squareup.retrofit2",
                "converter-gson"
            ).versionRef("retrofit_version")

            library("androidx-media", "androidx.media", "media").versionRef("media_version")
            library(
                "media3-exoplayer",
                "androidx.media3",
                "media3-exoplayer"
            ).versionRef("media3_version")
            library("media3-exoplayer-dash", "androidx.media3", "media3-exoplayer-dash").versionRef(
                "media3_version"
            )
            library(
                "media3-exoplayer-hls",
                "androidx.media3",
                "media3-exoplayer-hls"
            ).versionRef("media3_version")
            library("media3-exoplayer-rtsp", "androidx.media3", "media3-exoplayer-rtsp").versionRef(
                "media3_version"
            )
            library(
                "media3-exoplayer-ima",
                "androidx.media3",
                "media3-exoplayer-ima"
            ).versionRef("media3_version")
            library(
                "media3-datasource-cronet",
                "androidx.media3",
                "media3-datasource-cronet"
            ).versionRef("media3_version")
            library(
                "media3-datasource-okhttp",
                "androidx.media3",
                "media3-datasource-okhttp"
            ).versionRef("media3_version")
            library(
                "media3-datasource-rtmp",
                "androidx.media3",
                "media3-datasource-rtmp"
            ).versionRef("media3_version")
            library("media3-ui", "androidx.media3", "media3-ui").versionRef("media3_version")
            library(
                "media3-ui-leanback",
                "androidx.media3",
                "media3-ui-leanback"
            ).versionRef("media3_version")
            library(
                "media3-session",
                "androidx.media3",
                "media3-session"
            ).versionRef("media3_version")
            library(
                "media3-extractor",
                "androidx.media3",
                "media3-extractor"
            ).versionRef("media3_version")
            library("media3-cast", "androidx.media3", "media3-cast").versionRef("media3_version")
            library(
                "media3-exoplayer-workmanager",
                "androidx.media3",
                "media3-exoplayer-workmanager"
            ).versionRef("media3_version")
            library(
                "media3-transformer",
                "androidx.media3",
                "media3-transformer"
            ).versionRef("media3_version")
            library(
                "media3-test-utils",
                "androidx.media3",
                "media3-test-utils"
            ).versionRef("media3_version")
            library(
                "media3-test-utils-robolectric",
                "androidx.media3",
                "media3-test-utils-robolectric"
            ).versionRef("media3_version")
            library(
                "media3-database",
                "androidx.media3",
                "media3-database"
            ).versionRef("media3_version")
            library(
                "media3-decoder",
                "androidx.media3",
                "media3-decoder"
            ).versionRef("media3_version")
            library(
                "media3-datasource",
                "androidx.media3",
                "media3-datasource"
            ).versionRef("media3_version")
            library(
                "media3-common",
                "androidx.media3",
                "media3-common"
            ).versionRef("media3_version")
        }
    }
}
rootProject.name = "媒体播放器"
include(":app")
include(":network")
include(":common")
