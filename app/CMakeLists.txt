cmake_minimum_required(VERSION 3.4.1)

find_library(log-lib
        log)

find_library(android
        android)

set(distribution_DIR ../../../../libs)

add_library(ffmpegutils
        SHARED
        src/main/cpp/ffmpegutils.cpp
        src/main/cpp/videoplayer.cpp)

add_library(avcodec
        SHARED
        IMPORTED)
set_target_properties(avcodec
        PROPERTIES IMPORTED_LOCATION
        ${distribution_DIR}/armeabi/libavcodec.so)

add_library(avfilter
        SHARED
        IMPORTED)
set_target_properties(avfilter
        PROPERTIES IMPORTED_LOCATION
        ${distribution_DIR}/armeabi/libavfilter.so)

add_library(avformat
        SHARED
        IMPORTED)
set_target_properties(avformat
        PROPERTIES IMPORTED_LOCATION
        ${distribution_DIR}/armeabi/libavformat.so)

add_library(avutil
        SHARED
        IMPORTED)
set_target_properties(avutil
        PROPERTIES IMPORTED_LOCATION
        ${distribution_DIR}/armeabi/libavutil.so)

add_library(postproc
        SHARED
        IMPORTED)
set_target_properties(postproc
        PROPERTIES IMPORTED_LOCATION
        ${distribution_DIR}/armeabi/libpostproc.so)

add_library(swresample
        SHARED
        IMPORTED)
set_target_properties(swresample
        PROPERTIES IMPORTED_LOCATION
        ${distribution_DIR}/armeabi/libswresample.so)

add_library(swscale
        SHARED
        IMPORTED)
set_target_properties(swscale
        PROPERTIES IMPORTED_LOCATION
        ${distribution_DIR}/armeabi/libswscale.so)

include_directories(libs/include)

target_link_libraries(ffmpegutils
        avcodec
        avfilter
        avformat
        avutil
        postproc
        swresample
        swscale
        ${log-lib}
        ${android})