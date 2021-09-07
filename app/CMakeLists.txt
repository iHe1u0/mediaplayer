cmake_minimum_required(VERSION 3.4.1)


set(LIB_DIR ${CMAKE_SOURCE_DIR}/src/main/jniLibs/${ANDROID_ABI})

include_directories(
        ${CMAKE_SOURCE_DIR}/src/main/cpp/include
)

add_library(avcodec SHARED IMPORTED)
set_target_properties(avcodec PROPERTIES IMPORTED_LOCATION
        ${LIB_DIR}/libavcodec.so)

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

add_library(yuv SHARED IMPORTED)
set_target_properties(yuv PROPERTIES IMPORTED_LOCATION
        ${LIB_DIR}/libyuv.so)


# build application's shared lib
#add_library(play_video SHARED
#        ${CMAKE_SOURCE_DIR}/src/main/cpp/play_video.cpp)
#add_library(decode_video SHARED
#        ${CMAKE_SOURCE_DIR}/src/main/cpp/decode_video.cpp)
add_library(audioPlayer SHARED
        ${CMAKE_SOURCE_DIR}/src/main/cpp/player/audio/play_audio.cpp
        ${CMAKE_SOURCE_DIR}/src/main/cpp/player/audio/AudioDevice.c
        ${CMAKE_SOURCE_DIR}/src/main/cpp/player/audio/FFmpegAudioPlay.c)

add_library(yuvutils SHARED
        ${CMAKE_SOURCE_DIR}/src/main/cpp/yuv/yuvutils.cpp)
#特别注意，你想生成几个so，就要写几个link，写在一个link里面是错误的
#target_link_libraries(play_video
#        log
#        android
#        avcodec
#        avformat
#        avutil
#        swresample
#        swscale-4
#        )
#target_link_libraries(decode_video
#        log
#        android
#        avcodec
#        avformat
#        avutil
#        postproc
#        swresample
#        swscale
#        )

#注意加入opensl es
target_link_libraries(audioPlayer
        log
        OpenSLES
        android
        avcodec
        avformat
        avutil
        postproc
        swresample
        swscale
        )

target_link_libraries(yuvutils
        yuv
        log)