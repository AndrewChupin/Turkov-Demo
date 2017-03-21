cmake_minimum_required(VERSION 3.4.1)
project(app)

add_library(secure SHARED
            lib-base64.c
            lib-base64.h
            lcsx-rand.c
            vssc.c
            vssc.h
            )

add_library(secure-wrapper SHARED
            secure-wrapper.c)

target_link_libraries(secure-wrapper
                      secure
                      android
                      log)

