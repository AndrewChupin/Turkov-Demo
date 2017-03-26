cmake_minimum_required(VERSION 3.4.1)
project(app)


# Secure Library
add_library(secure SHARED
            vssc.c
            vssc.h

            lcsx-rand.h
            lcsx-rand.c

            vscb.h
            vscb.c)


# Stream Crypt Library
add_library(secure-wrapper SHARED
            stream-wrapper.c
            stream-wrapper.h
            key-wrapper.c
            key-wrapper.h)

target_link_libraries(secure-wrapper
                      secure
                      android
                      log)

