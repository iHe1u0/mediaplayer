//
// Created by iMorning on 2021/9/7.
//

#include <string.h>

#ifndef LOG_H
#define LOG_H

#define DEBUG

#define LOGN (void) 0

#ifndef WIN32

#include <android/log.h>

#define LOG_VERBOSE 1
#define LOG_DEBUG   2
#define LOG_INFO    3
#define LOG_WARNING 4
#define LOG_ERROR   5
#define LOG_FATAL   6
#define LOG_SILENT  7

#define code_line __LINE__

#ifndef LOG_TAG
//#define LOG_TAG __FILE__ // 输出C++文件名

#define CPP_FILE(x) strrchr(x,'/')?strrchr(x,'/')+1:x
#define LOG_TAG CPP_FILE( __FILE__ )

#endif

#ifndef LOG_LEVEL
#define LOG_LEVEL LOG_VERBOSE
#endif

#define LOG_PRINT(level, fmt, ...) __android_log_print(level, LOG_TAG, "%d:" fmt, code_line, ##__VA_ARGS__)
// __android_log_print(level, LOG_TAG, "%s:" fmt,  __PRETTY_FUNCTION__, ##__VA_ARGS__)

#if LOG_VERBOSE >= LOG_LEVEL
#define LOGV(fmt, ...) \
        LOG_PRINT(ANDROID_LOG_VERBOSE, fmt, ##__VA_ARGS__)
#else
#define LOGV(...) LOGN
#endif

#if LOG_DEBUG >= LOG_LEVEL
#define LOGD(fmt, ...) \
        LOG_PRINT(ANDROID_LOG_DEBUG, fmt, ##__VA_ARGS__)
#else
#define LOGD(...) LOGN
#endif

#if LOG_INFO >= LOG_LEVEL
#define LOGI(fmt, ...) \
        LOG_PRINT(ANDROID_LOG_INFO, fmt, ##__VA_ARGS__)
#else
#define LOGI(...) LOGN
#endif

#if LOG_WARNING >= LOG_LEVEL
#define LOGW(fmt, ...) \
        LOG_PRINT(ANDROID_LOG_WARN, fmt, ##__VA_ARGS__)
#else
#define LOGW(...) LOGN
#endif

#if LOG_ERROR >= LOG_LEVEL
#define LOGE(fmt, ...) \
        LOG_PRINT(ANDROID_LOG_ERROR, fmt, ##__VA_ARGS__)
#else
#define LOGE(...) LOGN
#endif

#if LOG_FATAL >= LOG_LEVEL
#define LOGF(fmt, ...) \
        LOG_PRINT(ANDROID_LOG_FATAL, fmt, ##__VA_ARGS__)
#else
#define LOGF(...) LOGN
#endif

#if LOG_FATAL >= LOG_LEVEL
#define LOGA(condition, fmt, ...) \
    if (!(condition)) \
    { \
        __android_log_assert(condition, LOG_TAG, "(%s:%u) %s: error:%s " fmt, \
            __FILE__, __LINE__, __PRETTY_FUNCTION__, condition, ##__VA_ARGS__); \
    }
#else
#define LOGA(...) LOGN
#endif

#else
#include <stdio.h>

#define LOG_PRINT(fmt, ...) printf("%s line:%d " fmt, __FILE__, __LINE__, ##__VA_ARGS__)

#define LOGV(fmt, ...) LOG_PRINT(fmt, ##__VA_ARGS__)

#define LOGD(fmt, ...) LOG_PRINT(fmt, ##__VA_ARGS__)

#define LOGI(fmt, ...) LOG_PRINT(fmt, ##__VA_ARGS__)

#define LOGW(fmt, ...) LOG_PRINT(fmt, ##__VA_ARGS__)

#define LOGE(fmt, ...) LOG_PRINT(fmt, ##__VA_ARGS__)

#define LOGF(fmt, ...) LOG_PRINT(fmt, ##__VA_ARGS__)

#define LOGA(...) LOGN


#endif // ANDROID_PROJECT

#endif //LOG_H
