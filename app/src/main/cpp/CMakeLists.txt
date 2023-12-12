cmake_minimum_required(VERSION 3.10.2)
#project("hello")
#include_directories(include)
include_directories(${CMAKE_CURRENT_SOURCE_DIR})

add_library(helloLib SHARED ${CMAKE_CURRENT_SOURCE_DIR}/lib/Hello.cpp)
find_library(AnLog log)
target_link_libraries(helloLib ${AnLog})







