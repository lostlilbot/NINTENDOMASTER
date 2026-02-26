# CMakeLists.txt for NDS Emulator native code
# This file would contain the build configuration for the emulator core
# In a real implementation, this would integrate MelonDS or similar

cmake_minimum_required(VERSION 3.22.1)

project("ndsemulator")

# Set C++ standard
set(CMAKE_CXX_STANDARD 17)
set(CMAKE_CXX_STANDARD_REQUIRED ON)

# Add the native library
add_library(${CMAKE_PROJECT_NAME} SHARED
    native-emulator.cpp
)

# Find required libraries
find_library(log-lib log)
find_library(android-lib android)
find_library(opengl-lib GLESv2)
find_library(EGL-lib EGL)

# Link libraries
target_link_libraries(${CMAKE_PROJECT_NAME}
    ${log-lib}
    ${android-lib}
    ${opengl-lib}
    ${EGL-lib}
)

# Compiler flags
target_compile_options(${CMAKE_PROJECT_NAME} PRIVATE
    -Wall
    -Wextra
    -Wno-unused-parameter
)
