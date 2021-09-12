cmake_minimum_required(VERSION 3.4.1)


set(LIB_DIR ${CMAKE_SOURCE_DIR}/src/main/jniLibs/${ANDROID_ABI})

include_directories(
        ${CMAKE_SOURCE_DIR}/src/main/cpp/include
)

# ffmpeg library
add_library(avcodec SHARED IMPORTED)
set_target_properties(avcodec PROPERTIES IMPORTED_LOCATION
        ${LIB_DIR}/libavcodec.so)
add_library(avdevice SHARED IMPORTED)
set_target_properties(avdevice PROPERTIES IMPORTED_LOCATION
        ${LIB_DIR}/libavdevice.so)
add_library(avfilter SHARED IMPORTED)
set_target_properties(avfilter PROPERTIES IMPORTED_LOCATION
        ${LIB_DIR}/libavfilter.so)
add_library(avformat SHARED IMPORTED)
set_target_properties(avformat PROPERTIES IMPORTED_LOCATION
        ${LIB_DIR}/libavformat.so)
add_library(avutil SHARED IMPORTED)
set_target_properties(avutil PROPERTIES IMPORTED_LOCATION
        ${LIB_DIR}/libavutil.so)
add_library(postproc SHARED IMPORTED)
set_target_properties(postproc PROPERTIES IMPORTED_LOCATION
        ${LIB_DIR}/libpostproc.so)
add_library(swresample SHARED IMPORTED)
set_target_properties(swresample PROPERTIES IMPORTED_LOCATION
        ${LIB_DIR}/libswresample.so)
add_library(swscale SHARED IMPORTED)
set_target_properties(swscale PROPERTIES IMPORTED_LOCATION
        ${LIB_DIR}/libswscale.so)
# ffmpeg library end

# yuv library
add_library(yuv SHARED IMPORTED)
set_target_properties(yuv PROPERTIES IMPORTED_LOCATION
        ${LIB_DIR}/libyuv.so)
# yuv library end

# sdl libraey
add_library(sdl SHARED IMPORTED)
set_target_properties(sdl PROPERTIES IMPORTED_LOCATION
        ${LIB_DIR}/libSDL2.so)
add_library(hidapi SHARED IMPORTED)
set_target_properties(hidapi PROPERTIES IMPORTED_LOCATION
        ${LIB_DIR}/libhidapi.so)
# sdl library end

add_library(Player SHARED
        src/main/cpp/main.cpp)
target_link_libraries(Player
        log
        OpenSLES
        android
        jnigraphics
        avcodec
        avdevice
        avfilter
        avformat
        avutil
        postproc
        swresample
        swscale
        sdl
        hidapi)