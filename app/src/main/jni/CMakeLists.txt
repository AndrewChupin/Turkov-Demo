cmake_minimum_required(VERSION 3.4.1)

add_library(hello-jni SHARED
            hello-jni.c
            vssc
            lcsx-rand)


# Include libraries needed for hello-jni lib
target_link_libraries(hello-jni
                      android
                      log)