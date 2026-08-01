# For more information about using CMake with Android Studio, read the
# documentation: https://d.android.com/studio/projects/add-native-code.html

# Sets the minimum version of CMake required to build the native library.

cmake_minimum_required(VERSION 3.4.1)

set(CMAKE_BUILD_TYPE Debug)
set(CMAKE_C_FLAGS_DEBUG "${CMAKE_C_FLAGS_DEBUG} -g -O0")
set(CMAKE_CXX_FLAGS_DEBUG "${CMAKE_CXX_FLAGS_DEBUG} -g -O0")
set(CMAKE_STRIP "")

# Creates and names a library, sets it as either STATIC
# or SHARED, and provides the relative paths to its source code.
# You can define multiple libraries, and CMake builds them for you.
# Gradle automatically packages shared libraries with your APK.

SET(CMAKE_CXX_FLAGS "${CMAKE_CXX_FLAGS} -std=c++11 -Werror=return-type -Wl,--hash-style=sysv")
#set(CMAKE_C_FLAGS "${CMAKE_C_FLAGS} -fsanitize=address -fno-omit-frame-pointer")
#set(CMAKE_CXX_FLAGS "${CMAKE_CXX_FLAGS} -fsanitize=address -fno-omit-frame-pointer")
#set(CMAKE_SHARED_LINKER_FLAGS "${CMAKE_SHARED_LINKER_FLAGS} -fsanitize=address")
#set(CMAKE_STATIC_LINKER_FLAGS "${CMAKE_STATIC_LINKER_FLAGS} -fsanitize=address")

# Header file directories
include_directories(
        ${CMAKE_SOURCE_DIR}/inline32
        ${CMAKE_SOURCE_DIR}/inline64
        ${CMAKE_SOURCE_DIR}/xDL
        ${CMAKE_SOURCE_DIR}/xHook
        ${CMAKE_SOURCE_DIR}/cpp
        ${CMAKE_SOURCE_DIR}/unwind32
        ${CMAKE_SOURCE_DIR}/unwind64
)

if (${CMAKE_ANDROID_ARCH_ABI} STREQUAL "arm64-v8a")
    SET(
            # Sets the name of the library.
            PLATFORM_DIFF_CODE

            # And64InlineHook
            inline64/And64InlineHook.cpp

            # Unwind-64
            unwind64/backtrace_64.cpp
    )
else()
    SET(
            # Sets the name of the library.
            PLATFORM_DIFF_CODE

            # Android-Inline-Hook
            inline32/relocate.c
            inline32/inlineHook.c

            # Unwind-32
            unwind32/backtrace.c
            unwind32/map_info.c
            unwind32/backtrace-arm.c
            unwind32/ptrace.c
            unwind32/backtrace-helper.c
    )
endif()

# Source code directories
add_library(
        # Sets the name of the library.
        raphael

        # Sets the library as a shared library.
        SHARED

        # platform diff code
        ${PLATFORM_DIFF_CODE}

        # xDL
        xDL/xdl.c
        xDL/xdl_iterate.c
        xDL/xdl_lzma.c
        xDL/xdl_util.c

        # xHook
        xHook/xh_core.c
        xHook/xh_elf.c
        xHook/xh_log.c
        xHook/xh_util.c

        AllocPool.hpp
        Cache.h
        MemoryCache.cpp
        MapData.cpp
        Raphael.h
        Raphael.cpp
        xloader.cpp
)

# Searches for a specified prebuilt library and stores the path as a
# variable. Because CMake includes system libraries in the search path by
# default, you only need to specify the name of the public NDK library
# you want to add. CMake verifies that the library exists before
# completing its build.

find_library(
        # Sets the name of the path variable.
        log-lib

        # Specifies the name of the NDK library that you want CMake to locate.
        log
)

# Specifies libraries CMake should link to your target library. You
# can link multiple libraries, such as libraries you define in this
# build script, prebuilt third-party libraries, or system libraries.

target_link_libraries(
        # Specifies the target library.
        raphael

        # Links the target library to the log library included in the NDK.
        -latomic
        ${log-lib}
        -lz
)